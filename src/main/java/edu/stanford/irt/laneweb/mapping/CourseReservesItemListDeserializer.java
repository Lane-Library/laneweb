package edu.stanford.irt.laneweb.mapping;

import java.util.ArrayList;
import java.util.List;

import tools.jackson.core.JacksonException;
import tools.jackson.core.JsonParser;
import tools.jackson.databind.DeserializationContext;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ValueDeserializer;

import edu.stanford.irt.coursereserves.Course;
import edu.stanford.irt.coursereserves.CourseReservesItem;
import edu.stanford.irt.coursereserves.CourseReservesItemList;
import edu.stanford.irt.coursereserves.ItemType;

public class CourseReservesItemListDeserializer extends ValueDeserializer<CourseReservesItemList> {

    @Override
    public CourseReservesItemList deserialize(final JsonParser p, final DeserializationContext ctxt)
            throws JacksonException {
        JsonNode node = p.readValueAsTree();
        JsonNode itemsNode = node.get("items");
        List<CourseReservesItem> itemList = new ArrayList<>();
        itemsNode.forEach((final JsonNode n) -> itemList.add(new CourseReservesItem(
                getTextFromNode(n.get("author")),
                getTextFromNode(n.get("callNumber")),
                // strip leading L, a, or in chars from FOLIO hrid
                n.get("id").asString().replaceFirst("^(L|a|in)", ""),
                n.get("availableCount").asInt(),
                getTextFromNode(n.get("title")),
                getTextFromNode(n.get("url")),
                ItemType.valueOf(n.get("type").asText()),
                getTextFromNode(n.get("versionNote")))));
        if (node.hasNonNull("course")) {
            JsonNode n = node.get("course");
            Course course = new Course(
                    n.get("id").asString(),
                    n.get("name").asString(),
                    n.get("number").asString(),
                    n.get("instructor").asString(),
                    n.get("department").asString());
            return new CourseReservesItemList(course, itemList);
        } else {
            return new CourseReservesItemList(itemList);
        }
    }

    private String getTextFromNode(final JsonNode node) {
        if (node.isNull()) {
            return null;
        } else {
            return node.asString();
        }
    }
}
