package edu.stanford.irt.laneweb.mapping;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import edu.stanford.irt.status.ApplicationStatus;
import edu.stanford.irt.status.Status;
import edu.stanford.irt.status.StatusItem;

public class ApplicationStatusDeserializer extends JsonDeserializer<ApplicationStatus> {

    private static List<StatusItem> getItems(final JsonNode node) {
        List<StatusItem> items = new ArrayList<>();
        node.forEach((final JsonNode n) -> items
                .add(new StatusItem(Status.valueOf(n.get("status").asText()), n.get("message").asText())));
        return items;
    }

    @Override
    public ApplicationStatus deserialize(final JsonParser p, final DeserializationContext ctxt) throws IOException {
        JsonNode node = p.getCodec().readTree(p);
        String jvmVersion = null != node.get("jvmVersion") ? node.get("jvmVersion").asText() : "?";
        return new ApplicationStatus(node.get("name").asText(), node.get("version").asText(), node.get("host").asText(),
                jvmVersion, node.get("pid").asInt(),
                ZonedDateTime.parse(node.get("time").asText(), DateTimeFormatter.ISO_ZONED_DATE_TIME),
                getItems(node.get("items")));
    }
}
