package edu.stanford.irt.laneweb.catalog.grandrounds;

import static org.junit.Assert.assertNotNull;

import java.net.URI;
import java.net.URISyntaxException;

import org.junit.Before;
import org.junit.Test;

public class HTTPGrandRoundsServiceTest {

    private HTTPGrandRoundsService service;

    private URI uri;

    @Before
    public void setUp() throws URISyntaxException {
        this.uri = getClass().getResource("grandrounds").toURI();
        this.service = new HTTPGrandRoundsService(this.uri);
    }

    @Test
    public void testGetGrandRounds() {
        assertNotNull(this.service.getGrandRounds("department", "year"));
    }
}
