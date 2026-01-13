package edu.stanford.irt.laneweb.mapping;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import tools.jackson.core.JacksonException;
import tools.jackson.core.JsonParser;
import tools.jackson.databind.DeserializationContext;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ValueDeserializer;

import edu.stanford.irt.status.ApplicationStatus;
import edu.stanford.irt.status.Status;
import edu.stanford.irt.status.StatusItem;

public class ApplicationStatusDeserializer extends ValueDeserializer<ApplicationStatus> {

    private static List<StatusItem> getItems(final JsonNode node) {
        List<StatusItem> items = new ArrayList<>();
        node.forEach((final JsonNode n) -> items
                .add(new StatusItem(Status.valueOf(n.get("status").asString()), n.get("message").asString())));
        return items;
    }

    @Override
    public ApplicationStatus deserialize(final JsonParser p, final DeserializationContext ctxt)
            throws JacksonException {
        JsonNode node = p.readValueAsTree();
        String jvmVersion = null != node.get("jvmVersion") ? node.get("jvmVersion").asString() : "?";
        return new ApplicationStatus(node.get("name").asString(), node.get("version").asString(),
                node.get("host").asString(),
                jvmVersion, node.get("pid").asInt(),
                ZonedDateTime.parse(node.get("time").asString(), DateTimeFormatter.ISO_ZONED_DATE_TIME),
                getItems(node.get("items")));
    }
}
