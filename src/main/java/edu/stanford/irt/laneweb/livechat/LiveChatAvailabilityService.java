package edu.stanford.irt.laneweb.livechat;

import java.net.URI;
import java.time.Clock;
import java.time.Duration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import edu.stanford.irt.laneweb.rest.RESTException;
import edu.stanford.irt.laneweb.rest.RESTService;

@Service
public class LiveChatAvailabilityService {

    @Value("${edu.stanford.irt.laneweb.live-chat-api.url}")
    private URI liveChatServiceURI;

    @Autowired
    private RESTService restservice;

    private long expires = Duration.ofMinutes(5).toMillis();

    private Clock clock = Clock.systemDefaultZone();

    private long nextUpdate;

    private boolean available;

    private static final Logger log = LoggerFactory.getLogger(LiveChatAvailabilityService.class);

    public boolean isAvailable() {
        long now = this.clock.millis();
        if (now >= this.nextUpdate) {
            this.available = checkChatPresence();
            this.nextUpdate = now + this.expires;
        }
        return this.available;
    }

    private boolean checkChatPresence() {
        try {
           return "available".equalsIgnoreCase(this.restservice.getObject(this.liveChatServiceURI, String.class));
        } catch (RESTException e) {
            log.error("problem fetching availability from live chat service", e);
        }
        return false;
    }
}
