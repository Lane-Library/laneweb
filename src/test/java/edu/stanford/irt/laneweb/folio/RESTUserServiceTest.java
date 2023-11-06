package edu.stanford.irt.laneweb.folio;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.mock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertTrue;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import edu.stanford.irt.laneweb.rest.RESTService;

public class RESTUserServiceTest {

    private RESTService restService;

    private RESTUserService service;

    private URI uri;

    @Before
    public void setUp() throws Exception {
        this.uri = new URI("/");
        this.restService = mock(RESTService.class);
        this.service = new RESTUserService(this.uri, this.restService);
    }

    @Test
    public final void testAddUser() throws Exception {
        Map<String, Object> user = new HashMap<>();
        expect(this.restService.postObject(new URI("/users/user"), user, Boolean.class)).andReturn(Boolean.TRUE);
        replay(this.restService);
        assertTrue(this.service.addUser(user));
        verify(this.restService);
    }
}
