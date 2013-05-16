package edu.stanford.irt.laneweb.mapping;

import org.codehaus.jackson.Version;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.module.SimpleModule;

public class LanewebObjectMapper extends ObjectMapper {

    public LanewebObjectMapper() {
        SimpleModule module = new SimpleModule("lane model", new Version(1, 0, 0, null));
        module.addSerializer(new IPGroupSerializer());
        module.addSerializer(new TicketSerializer());
        registerModule(module);
    }
}
