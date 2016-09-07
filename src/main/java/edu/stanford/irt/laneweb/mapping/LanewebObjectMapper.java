package edu.stanford.irt.laneweb.mapping;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

import edu.stanford.irt.search.impl.Result;

public class LanewebObjectMapper extends ObjectMapper {

    private static final long serialVersionUID = 1L;

    public LanewebObjectMapper() {
        SimpleModule module = new SimpleModule("lane model", new Version(1, 0, 0, null, null, null));
        module.addSerializer(new IPGroupSerializer());
        module.addSerializer(new TicketSerializer());
        module.addDeserializer(Result.class, new ResultDeserializer());
        registerModule(module);
    }
}
