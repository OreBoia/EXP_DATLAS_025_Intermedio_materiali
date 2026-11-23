package com.shopflow.ordini.interno;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import com.shopflow.TestcontainersConfiguration;
import com.shopflow.comune.Denaro;
import com.shopflow.comune.audit.RegistroAudit;
import com.shopflow.ordini.RichiestaOrdine;
import com.shopflow.ordini.spi.ScaricoMagazzino;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.boot.test.mock.mockito.MockBean;

// MG4 Intermedio: chiamata da un altro bean (passa dal proxy) e chiamata con this (lo salta)
@SpringBootTest(properties = "shopflow.dati-demo=false")
@Import(TestcontainersConfiguration.class)
class AuditAspectTest {

    @MockBean RegistroAudit registro;         // collaboratore dell'aspect, sostituito da un mock
    @MockBean ScaricoMagazzino magazzino;     // il magazzino non serve per questa prova
    @Autowired CreaOrdineService servizio;       // il bean iniettato è il proxy

    @Test
    void chiamataEsternaVieneRegistrata() throws Exception {
        servizio.esegui(richiestaValida());
        verify(registro).scrivi(eq("CREA_ORDINE"), any(), any());     // l'aspect è scattato
    }

    @Test
    void autoChiamataNonVieneRegistrata() throws Exception {
        servizio.eseguiDaDentro(richiestaValida());
        verify(registro, never()).scrivi(any(), any(), any());        // l'aspect è stato saltato
    }

    private static RichiestaOrdine richiestaValida() {
        return new RichiestaOrdine("C-1", List.of(new RichiestaOrdine.Riga("ABC-1", 1, Denaro.euro("10.00"))));
    }
}
