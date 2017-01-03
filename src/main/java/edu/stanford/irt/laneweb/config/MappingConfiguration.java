package edu.stanford.irt.laneweb.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationConfig;

import edu.stanford.irt.laneweb.mapping.LanewebObjectMapper;

@Configuration
public class MappingConfiguration {

    @Bean
    public SerializationConfig jacksonSerializationConfig() {
        return new ObjectMapper().getSerializationConfig();
    }

    @Bean(name = "edu.stanford.irt.laneweb.mapping.LanewebObjectMapper")
    public ObjectMapper lanewebObjectMapper() {
        return new LanewebObjectMapper();
    }
}
