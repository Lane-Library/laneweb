package edu.stanford.irt.laneweb.livechat;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.Clock;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import edu.stanford.irt.laneweb.rest.RESTException;
import edu.stanford.irt.laneweb.rest.RESTService;

@Service
public class LiveChatAvailabilityService {

    private static final Logger log = LoggerFactory.getLogger(LiveChatAvailabilityService.class);

    private boolean available;

    private Clock clock = Clock.systemDefaultZone();

    private long expires = Duration.ofMinutes(5).toMillis();

    private URI liveChatServiceURI;

    private long nextUpdate;

    @Autowired
    private RESTService restService;

    public boolean isAvailable() {
        long now = this.clock.millis();
        if (now >= this.nextUpdate) {
            this.available = checkChatPresence();
            this.nextUpdate = now + this.expires;
        }
        return this.available;
    }

    @Autowired
    public void setLiveChatServiceURI(
            @Value("${edu.stanford.irt.laneweb.live-chat-service.scheme}") final String scheme,
            @Value("${edu.stanford.irt.laneweb.live-chat-service.host}") final String host,
            @Value("${edu.stanford.irt.laneweb.live-chat-service.port}") final int port,
            @Value("${edu.stanford.irt.laneweb.live-chat-service.path}") final String path) throws URISyntaxException {
        this.liveChatServiceURI = new URI(scheme, null, host, port, path, null, null);
    }

    private boolean checkChatPresence() {
        try {
        	return (boolean)restService.getObject(this.liveChatServiceURI, HashMap.class).get("online");            
        } catch (RESTException e) {
            log.error("problem fetching availability from live chat service", e);
        }
        return false;
    }

    // for unit testing
    protected void setNextUpdate(final long nextUpdate) {
        this.nextUpdate = nextUpdate;
    }

    // for unit testing
    protected void setRestService(final RESTService restService) {
        this.restService = restService;
    }
}
