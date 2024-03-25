package edu.stanford.irt.laneweb.config;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.springframework.boot.web.client.RestClientCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.ResourceHttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestClient;

import com.fasterxml.jackson.databind.ObjectMapper;

import edu.stanford.irt.laneweb.rest.RESTService;

@Configuration
public class RESTClientConfiguration {

    private static final int HTTP_CONNECT_TIMEOUT = 5;

    private static final int HTTP_READ_TIMEOUT = 30;

    @Bean
    HttpComponentsClientHttpRequestFactory getRequestFactory() {
        RequestConfig requestConfig = RequestConfig.custom()
                .setResponseTimeout(HTTP_READ_TIMEOUT, TimeUnit.SECONDS).build();
        CloseableHttpClient httpClient = HttpClientBuilder.create().setDefaultRequestConfig(requestConfig).build();
        HttpComponentsClientHttpRequestFactory hcchrf =  new HttpComponentsClientHttpRequestFactory(httpClient);
        hcchrf.setConnectionRequestTimeout(Duration.ofSeconds(HTTP_CONNECT_TIMEOUT));
        hcchrf.setConnectTimeout(Duration.ofSeconds(HTTP_CONNECT_TIMEOUT));
        return hcchrf;
    }

    @Bean
    List<HttpMessageConverter<?>> getMessageConverters(ObjectMapper objectMapper) {
        List<HttpMessageConverter<?>> messageConverters = new ArrayList<>();
        StringHttpMessageConverter stringConverter = new StringHttpMessageConverter();
        stringConverter.setWriteAcceptCharset(false);
        messageConverters.add(stringConverter);
        messageConverters.add(new MappingJackson2HttpMessageConverter(objectMapper));
        messageConverters.add(new ResourceHttpMessageConverter());
        return messageConverters;
    }

    @Bean
    RestClientCustomizer restClientCustomizer(final List<HttpMessageConverter<?>> messageConverters,
            HttpComponentsClientHttpRequestFactory httpComponentsClientHttpRequestFactory) {
        return (restClientBuilder) -> restClientBuilder.requestFactory(httpComponentsClientHttpRequestFactory)
                .messageConverters(converters -> converters.addAll(0, messageConverters)).build();
    }

    @Bean
    RestClient restClient(RestClient.Builder builder) {
        return builder.build();
    }
    
    @Bean
    RESTService restService(RestClient restClient) {
        return new RESTService(restClient);
    }
    
}
