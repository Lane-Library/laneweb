package edu.stanford.irt.laneweb.bookcovers;

import java.util.List;
import java.util.Map;

public class CompositeBookCoverService implements BookCoverService {

    private BookCoverService cacheService;

    private BookCoverService googleService;

    public CompositeBookCoverService(final BookCoverService cacheService, final BookCoverService googleService) {
        this.cacheService = cacheService;
        this.googleService = googleService;
    }

    @Override
    public Map<Integer, String> getBookCoverURLs(final List<Integer> bibids) {
        Map<Integer, String> bookCovers = this.cacheService.getBookCoverURLs(bibids);
        bibids.removeAll(bookCovers.keySet());
        bookCovers.putAll(this.googleService.getBookCoverURLs(bibids));
        return bookCovers;
    }
}
