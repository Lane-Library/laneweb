package edu.stanford.irt.laneweb.rest;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RESTClientTestConfiguration {

    @Bean
    RestClient restClient(RestClient.Builder builder) {
        return builder.build();
    }

    @Bean
    RESTService restService(RestClient restClient) {
        return new RESTService(restClient);
    }
}
