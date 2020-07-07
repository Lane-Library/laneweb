package edu.stanford.irt.laneweb.livechat;

import java.net.URI;
import java.time.Clock;
import java.time.Duration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.stanford.irt.laneweb.rest.RESTException;
import edu.stanford.irt.laneweb.rest.RESTService;

public class LiveChatAvailabilityService {

    private URI liveChatServiceURI;

    public static final long TIME_BETWEEN_REQUESTS = Duration.ofMinutes(5).toMillis();

    private long nextUpdate;

    private Clock clock;

    private long expires;

    private boolean available;

    private RESTService restservice;

    private static final Logger log = LoggerFactory.getLogger(LiveChatAvailabilityService.class);

    public LiveChatAvailabilityService(final URI liveChatServiceURI, final RESTService restservice) {
        this(liveChatServiceURI, restservice, Clock.systemDefaultZone(), TIME_BETWEEN_REQUESTS);
    }

    LiveChatAvailabilityService(final URI liveChatServiceURI, final RESTService restservice, final Clock clock,
            final long expires) {
        this.liveChatServiceURI = liveChatServiceURI;
        this.restservice = restservice;
        this.clock = clock;
        this.expires = expires;
        this.nextUpdate = clock.millis();
    }

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
