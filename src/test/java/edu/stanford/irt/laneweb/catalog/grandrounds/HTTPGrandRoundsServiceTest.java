package edu.stanford.irt.laneweb.catalog.grandrounds;

import static org.junit.Assert.assertEquals;

import java.net.URISyntaxException;

import org.junit.Before;
import org.junit.Test;

public class HTTPGrandRoundsServiceTest {

    private HTTPGrandRoundsService service;

    @Before
    public void setUp() throws URISyntaxException {
        this.service = new HTTPGrandRoundsService(getClass().getResource("").toURI());
    }

    @Test
    public void testGetGrandRounds() {
        assertEquals(39, this.service.getGrandRounds("department", "year").size());
    }
}
