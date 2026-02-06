package edu.stanford.irt.laneweb.resource;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import edu.stanford.irt.laneweb.LanewebException;
import edu.stanford.irt.laneweb.rest.RESTService;

public class BannerResourceOutageService {

    private final URI uri;
    private final RESTService restService;

    private final static String resourceOutageRequest = "?m=statusposts";

    public BannerResourceOutageService(URI libanswerOutageServiceURI, RESTService restService) {
        this.uri = libanswerOutageServiceURI;

        this.restService = restService;
    }

    public ByteArrayInputStream getHtmlResourceOutages() {
        try {
            URI libanswerOutageURI = new URI(this.uri.toString().concat(resourceOutageRequest));
            String content = this.restService.getObject(libanswerOutageURI, String.class);
            ObjectMapper mapper = new ObjectMapper();

            JsonNode json = mapper.readTree(content.getBytes(Charset.forName("UTF-8")));
            return new ByteArrayInputStream(json.get("main").textValue().getBytes(Charset.forName("UTF-8")));

        } catch (IOException | URISyntaxException e) {
            new LanewebException(e);
        }
        return null;
    }

}
