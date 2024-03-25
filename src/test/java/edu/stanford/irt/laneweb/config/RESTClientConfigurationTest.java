package edu.stanford.irt.laneweb.config;

import static org.junit.Assert.assertNotNull;

import java.util.Collections;

import org.junit.jupiter.api.Test;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.StringHttpMessageConverter;

import com.fasterxml.jackson.databind.ObjectMapper;

class RESTClientConfigurationTest {

    private RESTClientConfiguration configuration = new RESTClientConfiguration();
    
    @Test
    void testClientHttpRequestFactory() {
        assertNotNull(this.configuration.getRequestFactory());
    }

    @Test
    void testMessageConverters() {
        assertNotNull(this.configuration.getMessageConverters(new ObjectMapper()));
    }

    @Test
    void testRestClientCustomizer() {
        assertNotNull(
                this.configuration.restClientCustomizer(Collections.singletonList(new StringHttpMessageConverter()),
                        new HttpComponentsClientHttpRequestFactory()));
    }

    @Test
    void testRestService() {
        assertNotNull(this.configuration.restService(null));
    }
}
