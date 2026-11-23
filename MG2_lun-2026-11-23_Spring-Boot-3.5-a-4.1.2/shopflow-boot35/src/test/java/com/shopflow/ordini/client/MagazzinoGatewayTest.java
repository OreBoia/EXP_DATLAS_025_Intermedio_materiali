package com.shopflow.ordini.client;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.shopflow.TestcontainersConfiguration;
import com.shopflow.comune.Result;
import com.shopflow.ordini.ErroreOrdine;
import com.shopflow.ordini.client.MagazzinoClient.RichiestaRiserva;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.web.client.ResourceAccessException;

// MG2 Difficile / MG4 Intermedio: client dichiarativo e Spring Retry contro un server HTTP finto (JDK, nessuna libreria)
@SpringBootTest(properties = "shopflow.dati-demo=false")
@Import(TestcontainersConfiguration.class)
class MagazzinoGatewayTest {

    static final HttpServer server = avvia();
    static final AtomicInteger chiamate = new AtomicInteger();
    static volatile Consumer<HttpExchange> risposta = e -> {};

    @DynamicPropertySource
    static void indirizzo(DynamicPropertyRegistry registry) {
        registry.add("shopflow.magazzino.base-url",
                () -> "http://localhost:" + server.getAddress().getPort());
        registry.add("shopflow.magazzino.read-timeout", () -> "300ms");
    }

    @Autowired MagazzinoGateway gateway;

    @BeforeEach
    void azzera() {
        chiamate.set(0);
    }

    @Test
    void parametroNullableNonVieneInviato() {
        var query = new java.util.concurrent.atomic.AtomicReference<String>("non chiamato");
        risposta = e -> {
            query.set(e.getRequestURI().getRawQuery());
            json(e, 200, "{\"sku\":\"ABC-1\",\"quantita\":7}");
        };
        assertThat(gateway.giacenza("ABC-1").quantita()).isEqualTo(7);
        assertThat(query.get()).as("deposito null: nessun parametro nella query").isNull();
    }

    @Test
    void retryDopoTreTimeoutRiesceAlQuartoTentativo() {
        risposta = e -> {
            if (chiamate.get() < 4) rispondiInRitardo(e);         // timeout di lettura: ResourceAccessException
            else json(e, 200, "{\"sku\":\"ABC-1\",\"quantita\":7}");
        };
        assertThat(gateway.giacenza("ABC-1").quantita()).isEqualTo(7);
        assertThat(chiamate.get()).as("maxAttempts = 4: il primo tentativo + 3 ripetizioni").isEqualTo(4);
    }

    @Test
    void dopoQuattroTentativiFallitiLEccezioneArrivaAlChiamante() {
        risposta = MagazzinoGatewayTest::rispondiInRitardo;
        assertThatThrownBy(() -> gateway.giacenza("ABC-1")).isInstanceOf(ResourceAccessException.class);
        assertThat(chiamate.get()).isEqualTo(4);
    }

    // Attenzione: se il server CHIUDE la connessione senza rispondere, il client HTTP del JDK (quello usato
    // da Boot quando non ci sono Apache, Jetty o Reactor Netty) ripete da solo la GET una volta:
    // con maxAttempts = 4 il server riceve 8 richieste, non 4. Con un timeout di lettura il JDK non ripete.
    @Test
    void connessioneChiusaRaddoppiaLeRichiesteColClientJdk() {
        risposta = MagazzinoGatewayTest::chiudiSenzaRisposta;
        assertThatThrownBy(() -> gateway.giacenza("ABC-1")).isInstanceOf(ResourceAccessException.class);
        assertThat(chiamate.get()).isEqualTo(8);
    }

    @Test
    void erroreHttpNonVieneRipetuto() {
        risposta = e -> json(e, 500, "{}");                          // 5xx: non è ResourceAccessException
        assertThatThrownBy(() -> gateway.giacenza("ABC-1"));
        assertThat(chiamate.get()).isEqualTo(1);
    }

    @Test
    void riservaSicuraTraduceGliErroriHttp() {
        risposta = e -> json(e, 404, "{}");
        assertThat(gateway.riservaSicura(new RichiestaRiserva("XYZ", 1)))
                .isEqualTo(new Result.Errore<>(new ErroreOrdine.ArticoloSconosciuto("XYZ")));

        risposta = e -> json(e, 503, "{}");
        assertThat(gateway.riservaSicura(new RichiestaRiserva("ABC-1", 1)))
                .isEqualTo(new Result.Errore<>(new ErroreOrdine.MagazzinoNonDisponibile()));

        risposta = e -> json(e, 200, "{\"id\":\"R-1\",\"sku\":\"ABC-1\",\"quantita\":1}");
        assertThat(gateway.riservaSicura(new RichiestaRiserva("ABC-1", 1)))
                .isEqualTo(new Result.Ok<>(new MagazzinoClient.Riserva("R-1", "ABC-1", 1)));
    }

    @AfterAll
    static void ferma() {
        server.stop(0);
    }

    private static HttpServer avvia() {
        try {
            var s = HttpServer.create(new InetSocketAddress("localhost", 0), 0);
            s.createContext("/giacenze", e -> {
                chiamate.incrementAndGet();
                risposta.accept(e);
            });
            s.setExecutor(java.util.concurrent.Executors.newVirtualThreadPerTaskExecutor());   // un ritardo non blocca le altre
            s.start();
            return s;
        } catch (IOException ex) {
            throw new IllegalStateException(ex);
        }
    }

    private static void json(HttpExchange e, int stato, String corpo) {
        try {
            var bytes = corpo.getBytes(StandardCharsets.UTF_8);
            e.getResponseHeaders().add("Content-Type", "application/json");
            e.sendResponseHeaders(stato, bytes.length);
            e.getResponseBody().write(bytes);
            e.close();
        } catch (IOException ex) {
            throw new IllegalStateException(ex);
        }
    }

    // Chiude la connessione senza mandare la risposta: per il client è un errore di I/O
    private static void chiudiSenzaRisposta(HttpExchange e) {
        e.close();
    }

    // Risponde dopo il read-timeout del client (300ms): per il client è un timeout
    private static void rispondiInRitardo(HttpExchange e) {
        try {
            Thread.sleep(1_000);
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
        e.close();
    }
}
