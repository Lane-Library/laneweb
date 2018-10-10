package edu.stanford.irt.laneweb.drupal;

import java.net.URI;
import java.util.List;
import java.util.Map;

import edu.stanford.irt.laneweb.rest.RESTService;
import edu.stanford.irt.laneweb.rest.TypeReference;

public class DrupalAPIService {

    private static final TypeReference<Map<String, List<Map<String, String>>>> TYPE = new TypeReference<Map<String, List<Map<String, String>>>>() {
    };

    private URI drupalBaseURI;

    private RESTService restService;

    public DrupalAPIService(final URI drupalBaseURI, final RESTService restService) {
        this.drupalBaseURI = drupalBaseURI;
        this.restService = restService;
    }

    public String getNodeContent(final String node) {
        Map<String, List<Map<String, String>>> json = this.restService
                .getObject(this.drupalBaseURI.resolve(node + "?_format=json"), TYPE);
        return json.get("body").get(0).get("value");
    }
}
