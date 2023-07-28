package edu.stanford.irt.laneweb.voyager;

import java.net.URI;

import edu.stanford.irt.laneweb.rest.BasicAuthRESTService;

public class RESTLoginService implements LoginService {

    private static final String ENDPOINT_PATH_FORMAT = "login?univid=%s&pid=%s";

    private URI catalogServiceURI;

    private BasicAuthRESTService restService;

    public RESTLoginService(final URI catalogServiceURI, final BasicAuthRESTService restService) {
        this.catalogServiceURI = catalogServiceURI;
        this.restService = restService;
    }

    @Override
    public boolean login(final String voyagerUnivId, final String pid) {
        String endpointPath = String.format(ENDPOINT_PATH_FORMAT, voyagerUnivId, pid);
        URI uri = this.catalogServiceURI.resolve(endpointPath);
        return this.restService.getObject(uri, Boolean.class).booleanValue();
    }
}
