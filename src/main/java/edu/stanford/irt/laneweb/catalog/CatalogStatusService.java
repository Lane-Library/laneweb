package edu.stanford.irt.laneweb.catalog;

import java.net.URI;

import edu.stanford.irt.laneweb.rest.Oauth.OauthRESTService;
import edu.stanford.irt.status.ApplicationStatus;
import edu.stanford.irt.status.StatusService;

public class CatalogStatusService implements StatusService {

    private OauthRESTService restService;

    private URI uri;

    public CatalogStatusService(final URI catalogServiceURI, final OauthRESTService restService) {
        this.uri = catalogServiceURI.resolve("status.json");
        this.restService = restService;
    }

    @Override
    public ApplicationStatus getStatus() {
        return this.restService.getObject(this.uri, ApplicationStatus.class);
    }
}
