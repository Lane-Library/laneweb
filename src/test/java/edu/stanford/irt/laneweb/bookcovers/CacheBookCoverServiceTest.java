package edu.stanford.irt.laneweb.bookcovers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;

public class CacheBookCoverServiceTest {

    private Map<Integer, Optional<String>> cache;

    private BookCoverService service;

    @Before
    public void setUp() {
        this.cache = new HashMap<>();
        this.service = new CacheBookCoverService(this.cache);
    }

    @Test
    public void testGetBookCoverURLsNotPresent() {
        assertTrue(this.service.getBookCoverURLs(Collections.singletonList(Integer.valueOf(12))).isEmpty());
    }

    @Test
    public void testGetBookCoverURLsNull() {
        this.cache.put(Integer.valueOf(12), Optional.empty());
        assertNull(this.service.getBookCoverURLs(Collections.singletonList(Integer.valueOf(12))).get(12));
    }

    @Test
    public void testGetBookCoverURLsPresent() {
        this.cache.put(Integer.valueOf(12), Optional.of("url"));
        assertEquals("url", this.service.getBookCoverURLs(Collections.singletonList(Integer.valueOf(12))).get(12));
    }
}
