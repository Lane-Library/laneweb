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

import edu.stanford.irt.laneweb.util.ServiceURIResolver;

public class HTTPGrandRoundsServiceTest {

    private HTTPGrandRoundsService service;

    private URI uri;

    private ServiceURIResolver uriResolver;

    @Before
    public void setUp() throws URISyntaxException {
        this.uri = getClass().getResource("").toURI();
        this.uriResolver = mock(ServiceURIResolver.class);
        this.service = new HTTPGrandRoundsService(this.uri, this.uriResolver);
    }

    @Test
    public void testGetGrandRounds() throws IOException {
        expect(this.uriResolver.getInputStream(this.uri.resolve("grandrounds?department=department&year=year")))
                .andReturn(getClass().getResourceAsStream("grandrounds"));
        replay(this.uriResolver);
        assertEquals(39, this.service.getGrandRounds("department", "year").size());
        verify(this.uriResolver);
    }
}
