package edu.stanford.irt.laneweb.servlet.binding.user;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.mock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import jakarta.servlet.http.HttpServletRequest;

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
        expect(this.request.getRemoteUser()).andReturn("user");
        expect(this.request.getAttribute("Shib-Identity-Provider")).andReturn("https://host.domain.org/").times(2);
        expect(this.request.getAttribute("displayName")).andReturn("name").times(2);
        expect(this.request.getAttribute("mail")).andReturn("email").times(2);
        expect(this.request.getRemoteUser()).andReturn("anotheruser");
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
    public void testCreateUserBadURI() {
        expect(this.request.getRemoteUser()).andReturn("user");
        expect(this.request.getAttribute("Shib-Identity-Provider")).andReturn("\u0000");
        expect(this.request.getAttribute("displayName")).andReturn("name");
        expect(this.request.getAttribute("mail")).andReturn("email");
        replay(this.request);
        User user = this.factory.createUser(this.request);
        assertEquals("email", user.getEmail());
        assertEquals("18b3f463d233d2e2764493fb5c951523@unknown", user.getHashedId());
        assertEquals("user@unknown", user.getId());
        assertEquals("name", user.getName());
        verify(this.request);
    }

    @Test
    public void testCreateUserMultiValueName() {
        expect(this.request.getRemoteUser()).andReturn("id@domain");
        expect(this.request.getAttribute("displayName")).andReturn("first name;another name");
        expect(this.request.getAttribute("mail")).andReturn("mail;mail2");
        replay(this.request);
        User user = this.factory.createUser(this.request);
        assertNotNull(user);
        assertEquals("id@domain", user.getId());
        assertEquals("mail", user.getEmail());
        assertEquals("first name", user.getName());
        assertEquals("911531548a5ea68cf13f5e0506367956@domain", user.getHashedId());
        verify(this.request);
    }

    @Test
    public void testCreateUserNullEmail() {
        expect(this.request.getRemoteUser()).andReturn("user@domain");
        expect(this.request.getAttribute("displayName")).andReturn("name");
        expect(this.request.getAttribute("mail")).andReturn(null);
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
        expect(this.request.getAttribute("Shib-Identity-Provider")).andReturn(null);
        expect(this.request.getAttribute("displayName")).andReturn("name");
        expect(this.request.getAttribute("mail")).andReturn("email");
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

    @Test
    public void testCreateUserOneDot() {
        expect(this.request.getRemoteUser()).andReturn("user");
        expect(this.request.getAttribute("Shib-Identity-Provider")).andReturn("https://domain.org/");
        expect(this.request.getAttribute("displayName")).andReturn("name");
        expect(this.request.getAttribute("mail")).andReturn("email");
        replay(this.request);
        User user = this.factory.createUser(this.request);
        assertEquals("email", user.getEmail());
        assertEquals("18b3f463d233d2e2764493fb5c951523@domain.org", user.getHashedId());
        assertEquals("user@domain.org", user.getId());
        assertEquals("name", user.getName());
        verify(this.request);
    }
}
