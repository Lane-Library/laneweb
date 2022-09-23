package edu.stanford.irt.laneweb.mapping;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import edu.stanford.irt.coursereserves.Course;
import edu.stanford.irt.coursereserves.CourseReservesItem;
import edu.stanford.irt.coursereserves.CourseReservesItemList;
import edu.stanford.irt.coursereserves.ItemType;

public class CourseReservesItemListDeserializer extends JsonDeserializer<CourseReservesItemList> {

    @Override
    public CourseReservesItemList deserialize(final JsonParser p, final DeserializationContext ctxt)
            throws IOException {
        JsonNode node = p.getCodec().readTree(p);
        JsonNode itemsNode = node.get("items");
        List<CourseReservesItem> itemList = new ArrayList<>();
        itemsNode.forEach((final JsonNode n) -> itemList.add(new CourseReservesItem(
                getTextFromNode(n.get("author")),
                getTextFromNode(n.get("callNumber")),
                n.get("id").asText(),
                n.get("availableCount").asInt(),
                getTextFromNode(n.get("title")),
                getTextFromNode(n.get("url")),
                ItemType.valueOf(n.get("type").asText()),
                getTextFromNode(n.get("versionNote")))));
        if (node.hasNonNull("course")) {
            JsonNode n = node.get("course");
            Course course = new Course(
                    n.get("id").asText(),
                    n.get("name").asText(),
                    n.get("number").asText(),
                    n.get("instructor").asText(),
                    n.get("department").asText());
            return new CourseReservesItemList(course, itemList);
        } else {
            return new CourseReservesItemList(itemList);
        }
    }

    private String getTextFromNode(final JsonNode node) {
        if (node.isNull()) {
            return null;
        } else {
            return node.asText();
        }
    }
}
