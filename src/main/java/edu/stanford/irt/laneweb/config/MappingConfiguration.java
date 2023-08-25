package edu.stanford.irt.laneweb.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import edu.stanford.irt.coursereserves.Course;
import edu.stanford.irt.coursereserves.CourseReservesItemList;
import edu.stanford.irt.laneweb.mapping.ApplicationStatusDeserializer;
import edu.stanford.irt.laneweb.mapping.CourseDeserializer;
import edu.stanford.irt.laneweb.mapping.CourseReservesItemListDeserializer;
import edu.stanford.irt.laneweb.mapping.IPGroupSerializer;
import edu.stanford.irt.laneweb.mapping.ResultDeserializer;
import edu.stanford.irt.laneweb.mapping.TicketSerializer;
import edu.stanford.irt.search.impl.Result;
import edu.stanford.irt.status.ApplicationStatus;

@Configuration
public class MappingConfiguration {

    @Bean
    public SerializationConfig jacksonSerializationConfig() {
        return new ObjectMapper().getSerializationConfig();
    }

    @Bean
    public ObjectMapper lanewebObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        SimpleModule module = new SimpleModule("lane model", new Version(1, 0, 0, null, null, null));
        module.addSerializer(new IPGroupSerializer());
        module.addSerializer(new TicketSerializer());
        module.addDeserializer(Result.class, new ResultDeserializer());
        module.addDeserializer(CourseReservesItemList.class, new CourseReservesItemListDeserializer());
        module.addDeserializer(Course.class, new CourseDeserializer());
        module.addDeserializer(ApplicationStatus.class, new ApplicationStatusDeserializer());
        objectMapper.registerModule(module);
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        return objectMapper;
    }
}
