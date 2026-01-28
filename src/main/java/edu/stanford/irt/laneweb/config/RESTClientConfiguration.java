package edu.stanford.irt.laneweb.config;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.ResourceHttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.JacksonJsonHttpMessageConverter;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestTemplate;

import tools.jackson.databind.json.JsonMapper;
import edu.stanford.irt.laneweb.rest.RESTService;

@Configuration
public class RESTClientConfiguration {

    private static final int HTTP_CONNECT_TIMEOUT = 5;

    private static final int HTTP_READ_TIMEOUT = 30;

    @Bean
    HttpComponentsClientHttpRequestFactory getRequestFactory() {
        RequestConfig requestConfig = RequestConfig.custom().setResponseTimeout(HTTP_READ_TIMEOUT, TimeUnit.SECONDS)
                .setConnectionRequestTimeout(HTTP_CONNECT_TIMEOUT, TimeUnit.SECONDS).build();
        CloseableHttpClient httpClient = HttpClientBuilder.create().setDefaultRequestConfig(requestConfig).build();
        HttpComponentsClientHttpRequestFactory hcchrf = new HttpComponentsClientHttpRequestFactory(httpClient);
        hcchrf.setConnectionRequestTimeout(Duration.ofSeconds(HTTP_CONNECT_TIMEOUT));
        hcchrf.setReadTimeout(Duration.ofSeconds(HTTP_READ_TIMEOUT));
        return hcchrf;
    }

    @Bean
    List<HttpMessageConverter<?>> getMessageConverters(final JsonMapper jsonMapper) {
        List<HttpMessageConverter<?>> messageConverters = new ArrayList<>();
        StringHttpMessageConverter stringConverter = new StringHttpMessageConverter();
        stringConverter.setWriteAcceptCharset(false);
        messageConverters.add(stringConverter);
        messageConverters.add(new JacksonJsonHttpMessageConverter(jsonMapper));
        messageConverters.add(new ResourceHttpMessageConverter());
        return messageConverters;
    }

    @Bean
    RestTemplate restTemplate(final List<HttpMessageConverter<?>> messageConverters,
            final HttpComponentsClientHttpRequestFactory httpComponentsClientHttpRequestFactory) {
        RestTemplate restTemplate = new RestTemplate(httpComponentsClientHttpRequestFactory);
        restTemplate.setMessageConverters(messageConverters);
        return restTemplate;
    }

    @Bean
    RestClient restClientCustomizer(final RestTemplate restTemplate) {
        return RestClient.builder(restTemplate).build();
    }

    @Bean
    RESTService restService(final RestClient restClient) {
        return new RESTService(restClient);
    }

    @Bean(name = "java.net.URI/oauth2-server")
    public URI getOauthUri(RestClient restClient,
            @Value("${edu.stanford.irt.laneweb.authentication-server.scheme}") final String scheme,
            @Value("${edu.stanford.irt.laneweb.authentication-server.host}") final String host,
            @Value("${edu.stanford.irt.laneweb.authentication-server.port}") final int port,
            @Value("${edu.stanford.irt.laneweb.authentication-server.path}") final String path)
            throws URISyntaxException {
        return new URI(scheme, null, host, port, path, null, null);
    }
}
