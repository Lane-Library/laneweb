package edu.stanford.irt.laneweb.catalog;

import java.net.URI;

import edu.stanford.irt.laneweb.rest.RESTService;
import edu.stanford.irt.status.ApplicationStatus;
import edu.stanford.irt.status.StatusService;

public class CatalogStatusService implements StatusService {

    private RESTService restService;

    private URI uri;

    public CatalogStatusService(final URI catalogServiceURI, final RESTService restService) {
        this.uri = catalogServiceURI.resolve("status.json");
        this.restService = restService;
    }

    @Override
    public ApplicationStatus getStatus() {
        return this.restService.getObject(this.uri, ApplicationStatus.class);
    }
}
