package edu.stanford.irt.laneweb.mapping;

import tools.jackson.core.JacksonException;
import tools.jackson.core.JsonParser;
import tools.jackson.databind.DeserializationContext;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ValueDeserializer;

import edu.stanford.irt.coursereserves.Course;

public class CourseDeserializer extends ValueDeserializer<Course> {

    @Override
    public Course deserialize(final JsonParser p, final DeserializationContext ctxt) throws JacksonException {
        JsonNode node = p.readValueAsTree();
        return new Course(node.get("id").asString(), node.get("name").asString(), node.get("number").asString(),
                node.get("instructor").asString(), node.get("department").asString());
    }
}
