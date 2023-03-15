package edu.stanford.irt.laneweb.livechat;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.mock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.net.URI;
import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;

import edu.stanford.irt.laneweb.rest.RESTException;
import edu.stanford.irt.laneweb.rest.RESTService;

public class LiveChatAvailabilityServiceTest {

    private RESTService restService;

    private LiveChatAvailabilityService service;

    private URI uri;
    
    private HashMap<String, Object> chatAvailalibity;

    @Before
    public void setUp() throws Exception {
    	this.chatAvailalibity = new HashMap<>();
    	this.chatAvailalibity.put("online", true);
        this.uri = new URI("/");
        this.restService = mock(RESTService.class);
        this.service = new LiveChatAvailabilityService();
        this.service.setNextUpdate(0);
        this.service.setRestService(this.restService);
        this.service.setLiveChatServiceURI(this.uri.getScheme(), this.uri.getHost(), this.uri.getPort(),
                this.uri.getPath());
    }

    @Test
    public final void testIsAvailable() throws Exception {
        expect(this.restService.getObject(this.uri, HashMap.class)).andReturn(this.chatAvailalibity);
        replay(this.restService);
        assertTrue(this.service.isAvailable());
        assertTrue(this.service.isAvailable());
        verify(this.restService);
    }

    @Test
    public final void testIsAvailableAndThenIsNot() throws Exception {
    	HashMap<String, Object> chatNotAvailalibity = new HashMap<>();
    	chatNotAvailalibity.put("online", false);
    	expect(this.restService.getObject(this.uri, HashMap.class)).andReturn(this.chatAvailalibity);
        expect(this.restService.getObject(this.uri, HashMap.class)).andReturn(chatNotAvailalibity);
        replay(this.restService);
        assertTrue(this.service.isAvailable());
        this.service.setNextUpdate(0);
        assertFalse(this.service.isAvailable());
        verify(this.restService);
    }

    @Test
    public final void testIsAvailableRESTException() throws Exception {
        expect(this.restService.getObject(this.uri, HashMap.class)).andThrow(new RESTException(new IOException("oops")));
        replay(this.restService);
        assertFalse(this.service.isAvailable());
        verify(this.restService);
    }
}
