package edu.stanford.irt.laneweb.status;

import java.net.URI;

import edu.stanford.irt.laneweb.rest.RESTService;
import edu.stanford.irt.status.ApplicationStatus;
import edu.stanford.irt.status.StatusService;

public class ClassesStatusService implements StatusService {

    private RESTService restService;

    private URI statusUri;

    public ClassesStatusService(final URI classesServiceURI, final RESTService restService) {
        this.statusUri = classesServiceURI.resolve("status.json");
        this.restService = restService;
    }

    @Override
    public ApplicationStatus getStatus() {
        return this.restService.getObject(this.statusUri, ApplicationStatus.class);
    }
}
