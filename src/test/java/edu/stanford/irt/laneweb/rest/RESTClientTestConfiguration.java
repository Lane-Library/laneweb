package edu.stanford.irt.laneweb.rest;

import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestClient;

public class RESTClientTestConfiguration {

    @Bean
    BasicAuthRESTService basicAuthRestService(final RestClient restClient) {
        return new BasicAuthRESTService(restClient, "userInfo");
    }

    @Bean
    RestClient restClient(final RestClient.Builder builder) {
        return builder.build();
    }

    @Bean
    RESTService restService(final RestClient restClient) {
        return new RESTService(restClient);
    }
}
