package edu.stanford.irt.laneweb.mapping;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import tools.jackson.core.Version;
import tools.jackson.databind.json.JsonMapper;
import tools.jackson.databind.module.SimpleModule;

import edu.stanford.irt.status.ApplicationStatus;

public class ApplicationStatusDeserializerTest {

    private JsonMapper jsonMapper;

    @BeforeEach
    public void setUp() {
        SimpleModule module = new SimpleModule("lane model", new Version(1, 0, 0, null, null, null));
        module.addDeserializer(ApplicationStatus.class, new ApplicationStatusDeserializer());
        this.jsonMapper = JsonMapper.builder().addModule(module).build();
    }

    @Test
    public void testDeserialize() throws IOException {
        ApplicationStatus status = this.jsonMapper.readValue(getClass().getResourceAsStream("bookcovers-status.json"),
                ApplicationStatus.class);
        assertEquals("indlovu.local", status.getHost());
        assertEquals("bookcovers", status.getName());
        assertEquals(7037, status.getPid());
        assertEquals(3, status.getItems().size());
    }
}
