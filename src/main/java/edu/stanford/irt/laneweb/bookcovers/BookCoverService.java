package edu.stanford.irt.laneweb.bookcovers;

import java.util.Collection;
import java.util.Map;

import edu.stanford.irt.status.StatusService;

public interface BookCoverService extends StatusService {

    Map<Integer, String> getBookCoverURLs(Collection<Integer> bibids);
}
