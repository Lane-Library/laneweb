package edu.stanford.irt.laneweb.bookcovers;

import java.util.Collection;
import java.util.Map;

import edu.stanford.irt.status.ApplicationStatus;

public interface BookCoverService {

    Map<Integer, String> getBookCoverURLs(Collection<Integer> bibids);

    ApplicationStatus getStatus();
}