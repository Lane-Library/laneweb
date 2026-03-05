package edu.stanford.irt.laneweb.resource;

import java.io.ByteArrayInputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;

import edu.stanford.irt.laneweb.LanewebException;
import edu.stanford.irt.laneweb.rest.RESTService;
import tools.jackson.databind.json.JsonMapper;

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
            JsonMapper mapper = new JsonMapper();

            tools.jackson.databind.JsonNode json = mapper.readTree(content);
            if (json != null && json.get("main") != null) {
                return new ByteArrayInputStream(json.get("main").stringValue().getBytes(Charset.forName("UTF-8")));
            }
            return new ByteArrayInputStream("".getBytes(Charset.forName("UTF-8")));

        } catch (URISyntaxException e) {
            new LanewebException(e);
        }
        return null;
    }

}
