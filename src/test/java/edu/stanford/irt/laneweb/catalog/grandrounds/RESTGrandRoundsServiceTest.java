package edu.stanford.irt.laneweb.catalog.grandrounds;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.mock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.junit.Before;
import org.junit.Test;

import edu.stanford.irt.laneweb.rest.RESTService;

public class RESTGrandRoundsServiceTest {

    private RESTGrandRoundsService service;

    private URI uri;

    private RESTService restService;

    @Before
    public void setUp() throws URISyntaxException {
        this.uri = getClass().getResource("").toURI();
        this.restService = mock(RESTService.class);
        this.service = new RESTGrandRoundsService(this.uri, this.restService);
    }

    @Test
    public void testGetGrandRounds() throws IOException {
        expect(this.restService.getInputStream(this.uri.resolve("grandrounds?department=department&year=year")))
                .andReturn(getClass().getResourceAsStream("grandrounds"));
        replay(this.restService);
        assertEquals(39, this.service.getGrandRounds("department", "year").size());
        verify(this.restService);
    }
}
