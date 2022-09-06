package edu.stanford.irt.laneweb.mapping;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import edu.stanford.irt.coursereserves.Course;

public class CourseDeserializer extends JsonDeserializer<Course> {

    @Override
    public Course deserialize(final JsonParser p, final DeserializationContext ctxt) throws IOException {
        JsonNode node = p.getCodec().readTree(p);
        return new Course(node.get("id").asText(), node.get("name").asText(), node.get("number").asText(),
                node.get("instructor").asText(), node.get("department").asText());
    }
}
