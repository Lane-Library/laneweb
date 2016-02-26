package edu.stanford.irt.laneweb.bookcovers;

import java.util.List;
import java.util.Map;

@FunctionalInterface
public interface BookCoverService {

    Map<Integer, String> getBookCoverURLs(List<Integer> bibids);
}
