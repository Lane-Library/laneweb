package edu.stanford.irt.laneweb.rest;

import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestClient;

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
