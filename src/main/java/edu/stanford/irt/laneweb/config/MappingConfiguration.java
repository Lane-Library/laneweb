package edu.stanford.irt.laneweb.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import tools.jackson.core.Version;
import tools.jackson.databind.DeserializationFeature;
import tools.jackson.databind.SerializationConfig;
import tools.jackson.databind.cfg.DateTimeFeature;
import tools.jackson.databind.json.JsonMapper;
import tools.jackson.databind.module.SimpleModule;

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
        return JsonMapper.builder().build().serializationConfig();
    }

    @Bean
    public JsonMapper lanewebObjectMapper() {
        SimpleModule module = new SimpleModule("lane model", new Version(1, 0, 0, null, null, null));

        module.addSerializer(new IPGroupSerializer());
        module.addSerializer(new TicketSerializer());
        module.addDeserializer(Result.class, new ResultDeserializer());
        module.addDeserializer(CourseReservesItemList.class, new CourseReservesItemListDeserializer());
        module.addDeserializer(Course.class, new CourseDeserializer());
        module.addDeserializer(ApplicationStatus.class, new ApplicationStatusDeserializer());

        return JsonMapper.builder()
                .addModule(module)
                .configure(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES, false)
                .configure(DateTimeFeature.WRITE_DATES_AS_TIMESTAMPS, false)
                .build();
    }
}
