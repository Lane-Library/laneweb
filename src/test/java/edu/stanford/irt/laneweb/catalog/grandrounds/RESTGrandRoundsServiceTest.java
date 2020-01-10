package edu.stanford.irt.laneweb.catalog.grandrounds;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.mock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;

import java.net.URI;
import java.net.URISyntaxException;

import org.junit.Before;
import org.junit.Test;

import edu.stanford.irt.laneweb.rest.RESTService;

public class RESTGrandRoundsServiceTest {

    private RESTService restService;

    private RESTGrandRoundsService service;

    private URI uri;

    @Before
    public void setUp() throws URISyntaxException {
        this.uri = getClass().getResource("").toURI();
        this.restService = mock(RESTService.class);
        this.service = new RESTGrandRoundsService(this.uri, this.restService);
    }

    @Test
    public void testGetGrandRounds() {
        expect(this.restService.getInputStream(this.uri.resolve("grandrounds?department=department&year=year")))
                .andReturn(getClass().getResourceAsStream("grandrounds"));
        replay(this.restService);
        assertEquals(39, this.service.getByYear("department", "year").size());
        verify(this.restService);
    }

    @Test
    public void testGetGrandRoundsRecent() {
        expect(this.restService
                .getInputStream(this.uri.resolve("grandrounds/recent?department=department&limit=limit")))
                        .andReturn(getClass().getResourceAsStream("grandrounds"));
        replay(this.restService);
        assertEquals(39, this.service.getRecent("department", "limit").size());
        verify(this.restService);
    }
}
