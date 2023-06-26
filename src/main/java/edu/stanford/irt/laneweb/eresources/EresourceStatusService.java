package edu.stanford.irt.laneweb.eresources;

import java.net.URI;

import edu.stanford.irt.laneweb.rest.RESTService;
import edu.stanford.irt.status.ApplicationStatus;
import edu.stanford.irt.status.StatusService;

public class EresourceStatusService implements StatusService{

    RESTService restService;

    private URI eresourceServiceURI;
    
    public EresourceStatusService(final URI uri, final RESTService restService) {
       this.eresourceServiceURI = uri;
        this.restService = restService;
    }


    @Override
    public ApplicationStatus getStatus() {
        URI uri = this.eresourceServiceURI.resolve("status.json");
        return this.restService.getObject(uri, ApplicationStatus.class);
    }
}
