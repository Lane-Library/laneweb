package edu.stanford.irt.laneweb.status;

import java.time.ZonedDateTime;
import java.util.Collections;

import edu.stanford.irt.status.ApplicationStatus;
import edu.stanford.irt.status.Status;
import edu.stanford.irt.status.StatusItem;

public class FailedApplicationStatus extends ApplicationStatus {

    private static final String UNKNOWN = "unknown";

    public FailedApplicationStatus(final String name, final String message) {
        super(name, UNKNOWN, UNKNOWN, UNKNOWN, -1, ZonedDateTime.now(),
                Collections.singletonList(new StatusItem(Status.ERROR, message)));
    }
}
