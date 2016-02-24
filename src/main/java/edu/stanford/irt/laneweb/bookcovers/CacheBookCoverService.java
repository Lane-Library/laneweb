package edu.stanford.irt.laneweb.bookcovers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * <p>
 * A caching BookCoverServce. The cache map values are Optionals because ConcurrentHashMap doesn't take null values. The
 * ConcurrentHashMap is the simplest caching mechanism for a multi-threaded environment.
 * </p>
 * <p>
 * This class does not put entries into the cache, that happens elsewhere.
 * </p>
 */
public class CacheBookCoverService implements BookCoverService {

    private Map<Integer, Optional<String>> cache;

    /**
     * Create a CacheBookCoverService
     * 
     * @param cache
     *            should be a ConcurrentHashMap for a webapp environment
     */
    public CacheBookCoverService(final Map<Integer, Optional<String>> cache) {
        this.cache = cache;
    }

    /**
     * get a map of bookcover urls for the list of bibids. If the bibid is not in the cache the returned map will not
     * have it as one of its keys. If it is, the value will be a url for the book cover image, or null.
     */
    @Override
    public Map<Integer, String> getBookCoverURLs(final List<Integer> bibids) {
        Map<Integer, String> covers = new HashMap<>();
        for (Integer bibid : bibids) {
            if (this.cache.containsKey(bibid)) {
                covers.put(bibid, this.cache.get(bibid).orElse(null));
            }
        }
        return covers;
    }
}
