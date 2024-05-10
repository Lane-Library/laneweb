package edu.stanford.irt.laneweb.status;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.stanford.irt.laneweb.rest.RESTException;
import edu.stanford.irt.status.ApplicationStatus;
import edu.stanford.irt.status.StatusService;

public class LanewebStatusService {

    private static final Logger log = LoggerFactory.getLogger(LanewebStatusService.class);

    private List<StatusService> services;

    public LanewebStatusService(final List<StatusService> services) {
        List<StatusService> sorted = new ArrayList<>(services);
        sorted.sort(Comparator.comparing(service -> service.getClass().getSimpleName()));
        this.services = sorted;
    }

    public List<ApplicationStatus> getStatus() {
        List<ApplicationStatus> status = new ArrayList<>();
        for (StatusService service : this.services) {
            try {
                status.add(service.getStatus());
            } catch (RESTException e) {
                log.error(e.getCause().getMessage());
                status.add(new FailedApplicationStatus(service.getClass().getName(), e.getMessage()));
            }
        }
        return status;
    }
}
