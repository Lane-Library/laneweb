package edu.stanford.irt.laneweb.rest;

import java.net.URI;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.client.RestClient;

@Configuration @Profile("test")
public class OauthTestConfiguration {

    @Bean
    OauthRESTService oauthRESTService(final RestClient restClient) {
        return new OauthRESTService(restClient, "lane.lane:LANE", URI.create("http://localhost:8080/oauth2/token"));
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
