package edu.stanford.irt.laneweb.rest;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.client.RestClient;

@Configuration
@Profile("test")
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
