package edu.stanford.irt.laneweb.mapping;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import edu.stanford.irt.coursereserves.Course;
import edu.stanford.irt.coursereserves.CourseReservesItem;
import edu.stanford.irt.coursereserves.CourseReservesItemList;

public class CourseReservesItemListDeserializer extends JsonDeserializer<CourseReservesItemList> {

    @Override
    public CourseReservesItemList deserialize(final JsonParser p, final DeserializationContext ctxt)
            throws IOException, JsonProcessingException {
        JsonNode node = p.getCodec().readTree(p);
        JsonNode itemsNode = node.get("items");
        List<CourseReservesItem> itemList = new ArrayList<>();
        itemsNode.forEach(n -> itemList.add(new CourseReservesItem(
                getTextFromNode(n.get("author")),
                getTextFromNode(n.get("callNumber")),
                n.get("id").asInt(),
                n.get("availableCount").asInt(),
                getTextFromNode(n.get("title")),
                n.get("digital").asBoolean(),
                getTextFromNode(n.get("url")))));
        if (node.hasNonNull("course")) {
            JsonNode n = node.get("course");
            Course course = new Course(
                    n.get("id").asInt(),
                    n.get("name").asText(),
                    n.get("number").asText(),
                    n.get("instructor").asText());
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
