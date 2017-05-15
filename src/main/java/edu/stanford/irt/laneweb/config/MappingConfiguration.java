package edu.stanford.irt.laneweb.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.module.SimpleModule;

import edu.stanford.irt.laneweb.mapping.IPGroupSerializer;
import edu.stanford.irt.laneweb.mapping.ResultDeserializer;
import edu.stanford.irt.laneweb.mapping.TicketSerializer;
import edu.stanford.irt.search.impl.Result;

@Configuration
public class MappingConfiguration {

    @Bean
    public SerializationConfig jacksonSerializationConfig() {
        return new ObjectMapper().getSerializationConfig();
    }

    @Bean(name = "edu.stanford.irt.laneweb.mapping.LanewebObjectMapper")
    public ObjectMapper lanewebObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        SimpleModule module = new SimpleModule("lane model", new Version(1, 0, 0, null, null, null));
        module.addSerializer(new IPGroupSerializer());
        module.addSerializer(new TicketSerializer());
        module.addDeserializer(Result.class, new ResultDeserializer());
        objectMapper.registerModule(module);
        return objectMapper;
    }
}
