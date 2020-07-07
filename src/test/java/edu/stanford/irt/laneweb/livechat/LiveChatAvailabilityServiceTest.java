package edu.stanford.irt.laneweb.livechat;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.mock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.net.URI;
import java.time.Clock;

import org.junit.Before;
import org.junit.Test;

import edu.stanford.irt.laneweb.rest.RESTException;
import edu.stanford.irt.laneweb.rest.RESTService;

public class LiveChatAvailabilityServiceTest {

    private RESTService restService;

    private LiveChatAvailabilityService service;

    private URI uri;

    @Before
    public void setUp() throws Exception {
        this.uri = new URI("/");
        this.restService = mock(RESTService.class);
        this.service = new LiveChatAvailabilityService(this.uri, this.restService);
    }

    @Test
    public final void testIsAvailable() throws Exception {
        expect(this.restService.getObject(new URI("/"), String.class)).andReturn("aVailable");
        replay(this.restService);
        assertTrue(this.service.isAvailable());
        assertTrue(this.service.isAvailable());
        verify(this.restService);
    }

    @Test
    public final void testIsAvailableAndThenIsNot() throws Exception {
        this.service = new LiveChatAvailabilityService(this.uri, this.restService, Clock.systemDefaultZone(), 0);
        expect(this.restService.getObject(new URI("/"), String.class)).andReturn("aVailable");
        expect(this.restService.getObject(new URI("/"), String.class)).andReturn("unavailable");
        replay(this.restService);
        assertTrue(this.service.isAvailable());
        assertFalse(this.service.isAvailable());
        verify(this.restService);
    }

    @Test
    public final void testIsAvailableRESTException() throws Exception {
        expect(this.restService.getObject(new URI("/"), String.class))
                .andThrow(new RESTException(new IOException("oops")));
        replay(this.restService);
        assertFalse(this.service.isAvailable());
        verify(this.restService);
    }
}
