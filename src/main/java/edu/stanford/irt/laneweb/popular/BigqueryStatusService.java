package edu.stanford.irt.laneweb.popular;

import java.net.URI;

import edu.stanford.irt.laneweb.rest.RESTService;
import edu.stanford.irt.status.ApplicationStatus;
import edu.stanford.irt.status.StatusService;

public class BigqueryStatusService implements StatusService {

    private RESTService restService;

    private URI uri;

    public BigqueryStatusService(final URI bigqueryServiceURI, final RESTService restService) {
        this.uri = bigqueryServiceURI.resolve("status.json");
        this.restService = restService;
    }

    @Override
    public ApplicationStatus getStatus() {
        return this.restService.getObject(this.uri, ApplicationStatus.class);
    }
}
