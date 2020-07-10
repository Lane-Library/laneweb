package edu.stanford.irt.laneweb.config;

import java.net.URI;
import java.net.URISyntaxException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LiveChatAvailabilityConfiguration {

    @Bean("java.net.URI/live-chat-service")
    public URI liveChatServiceURI(@Value("${edu.stanford.irt.laneweb.live-chat-service.scheme}") final String scheme,
            @Value("${edu.stanford.irt.laneweb.live-chat-service.host}") final String host,
            @Value("${edu.stanford.irt.laneweb.live-chat-service.port}") final int port,
            @Value("${edu.stanford.irt.laneweb.live-chat-service.path}") final String path) throws URISyntaxException {
        return new URI(scheme, null, host, port, path, null, null);
    }
}
