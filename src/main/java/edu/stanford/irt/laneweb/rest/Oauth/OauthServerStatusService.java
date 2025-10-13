package edu.stanford.irt.laneweb.rest.Oauth;

import java.net.URI;

import edu.stanford.irt.laneweb.rest.RESTService;
import edu.stanford.irt.status.ApplicationStatus;
import edu.stanford.irt.status.StatusService;

public class OauthServerStatusService implements StatusService {

    private RESTService restService;

    private URI uri;

    public OauthServerStatusService(final URI statusURI, final RESTService restService) {
        this.uri = statusURI;
        this.restService = restService;
    }

    @Override
    public ApplicationStatus getStatus() {
        return this.restService.getObject(this.uri, ApplicationStatus.class);
    }

}
