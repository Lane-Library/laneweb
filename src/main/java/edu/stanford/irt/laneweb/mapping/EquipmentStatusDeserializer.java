package edu.stanford.irt.laneweb.mapping;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import edu.stanford.irt.laneweb.catalog.equipment.EquipmentStatus;

public class EquipmentStatusDeserializer extends JsonDeserializer<EquipmentStatus> {

    @Override
    public EquipmentStatus deserialize(final JsonParser p, final DeserializationContext ctxt)
            throws IOException, JsonProcessingException {
        JsonNode node = p.getCodec().readTree(p);
        return new EquipmentStatus(node.get("bibID").asText(), node.get("count").asText());
    }
}
