package edu.stanford.irt.laneweb.servlet.binding.user;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.mock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import javax.servlet.http.HttpServletRequest;

import org.junit.Before;
import org.junit.Test;

import edu.stanford.irt.laneweb.user.User;

public class RequestAttributeUserFactoryTest {

    private RequestAttributeUserFactory factory;

    private HttpServletRequest request;

    @Before
    public void setUp() {
        this.factory = new RequestAttributeUserFactory("key");
        this.request = mock(HttpServletRequest.class);
    }

    @Test
    public void testCreateUser() {
        expect(this.request.getRemoteUser()).andReturn("user@domain.org");
        expect(this.request.getHeader("LANE_OIDC_displayName")).andReturn("name").times(2);
        expect(this.request.getHeader("LANE_OIDC_mail")).andReturn("email").times(2);
        expect(this.request.getRemoteUser()).andReturn("anotheruser@domain.org");
        replay(this.request);
        User user = this.factory.createUser(this.request);
        assertEquals("email", user.getEmail());
        assertEquals("18b3f463d233d2e2764493fb5c951523@domain.org", user.getHashedId());
        assertEquals("user@domain.org", user.getId());
        assertEquals("name", user.getName());
        user = this.factory.createUser(this.request);
        assertEquals("email", user.getEmail());
        assertEquals("320d15847395fb9aea0792150f9f8e8d@domain.org", user.getHashedId());
        assertEquals("anotheruser@domain.org", user.getId());
        assertEquals("name", user.getName());
        verify(this.request);
    }

    @Test
    public void testCreateUserNullEmail() {
        expect(this.request.getRemoteUser()).andReturn("user@domain");
        expect(this.request.getHeader("LANE_OIDC_displayName")).andReturn("name");
        expect(this.request.getHeader("LANE_OIDC_mail")).andReturn(null);
        replay(this.request);
        User user = this.factory.createUser(this.request);
        assertNull(user.getEmail());
        assertEquals("18b3f463d233d2e2764493fb5c951523@domain", user.getHashedId());
        assertEquals("user@domain", user.getId());
        assertEquals("name", user.getName());
        verify(this.request);
    }

    @Test
    public void testCreateUserNullProvider() {
        expect(this.request.getRemoteUser()).andReturn("user");
        expect(this.request.getHeader("LANE_OIDC_displayName")).andReturn("name");
        expect(this.request.getHeader("LANE_OIDC_mail")).andReturn("email");
        replay(this.request);
        User user = this.factory.createUser(this.request);
        assertEquals("email", user.getEmail());
        assertEquals("18b3f463d233d2e2764493fb5c951523@unknown", user.getHashedId());
        assertEquals("user@unknown", user.getId());
        assertEquals("name", user.getName());
        verify(this.request);
    }

    @Test
    public void testCreateUserNullRemoteUser() {
        expect(this.request.getRemoteUser()).andReturn(null);
        replay(this.request);
        assertNull(this.factory.createUser(this.request));
        verify(this.request);
    }
}
