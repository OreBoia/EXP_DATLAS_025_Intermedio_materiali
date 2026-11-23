package com.shopflow.comune.web;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.shopflow.comune.Denaro;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Currency;
import org.springframework.boot.jackson.JsonComponent;

// Formato JSON di Denaro deciso a mano: {"importo": 25.50, "valuta": "EUR"}.
// @JsonComponent registra serializzatore e deserializzatore nel mapper di Boot 3.5 (Jackson 2)
@JsonComponent
public class DenaroJson {

    public static class Serializzatore extends JsonSerializer<Denaro> {
        @Override
        public void serialize(Denaro denaro, JsonGenerator generatore, SerializerProvider provider) throws IOException {
            generatore.writeStartObject();
            generatore.writeNumberField("importo", denaro.importo());
            generatore.writeStringField("valuta", denaro.valuta().getCurrencyCode());
            generatore.writeEndObject();
        }
    }

    public static class Deserializzatore extends JsonDeserializer<Denaro> {
        @Override
        public Denaro deserialize(JsonParser parser, DeserializationContext contesto) throws IOException {
            JsonNode nodo = parser.getCodec().readTree(parser);
            if (!nodo.hasNonNull("importo") || !nodo.hasNonNull("valuta"))
                return contesto.reportInputMismatch(Denaro.class, "importo e valuta sono obbligatori");
            return new Denaro(new BigDecimal(nodo.get("importo").asText()),
                    Currency.getInstance(nodo.get("valuta").asText()));
        }
    }
}
