package edu.stanford.irt.laneweb.config;

import java.net.URI;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import edu.stanford.irt.laneweb.livechat.LiveChatAvailabilityService;
import edu.stanford.irt.laneweb.rest.RESTService;

@Configuration
public class LiveChatAvailabilityConfiguration {

    @Bean
    public LiveChatAvailabilityService liveChatService(
            @Value("${edu.stanford.irt.laneweb.live-chat-api.url}") final URI uri,
            RESTService restService) {
        return new LiveChatAvailabilityService(uri, restService);
    }
}
