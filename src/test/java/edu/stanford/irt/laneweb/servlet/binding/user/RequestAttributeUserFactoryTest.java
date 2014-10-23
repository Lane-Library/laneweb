package edu.stanford.irt.laneweb.servlet.binding.user;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import javax.servlet.http.HttpServletRequest;

import org.junit.Before;
import org.junit.Test;

import edu.stanford.irt.laneweb.user.User;
import edu.stanford.irt.laneweb.user.User.Status;

public class RequestAttributeUserFactoryTest {

    private RequestAttributeUserFactory factory;

    private HttpServletRequest request;

    @Before
    public void setUp() {
        this.factory = new RequestAttributeUserFactory("domain", "nameAttribute", "emailAttribute", "key");
        this.request = createMock(HttpServletRequest.class);
    }

    @Test
    public void testCreateUser() {
        expect(this.request.getRemoteUser()).andReturn("user");
        expect(this.request.getAttribute("nameAttribute")).andReturn("name");
        expect(this.request.getAttribute("emailAttribute")).andReturn("email");
        replay(this.request);
        User user = this.factory.createUser(this.request);
        assertEquals("email", user.getEmail());
        assertEquals("18b3f463d233d2e2764493fb5c951523@domain", user.getHashedId());
        assertEquals("user@domain", user.getId());
        assertEquals("name", user.getName());
        assertEquals(Status.UNKNOWN, user.getStatus());
        verify(this.request);
    }

    @Test
    public void testCreateUserDomain() {
        expect(this.request.getRemoteUser()).andReturn("user@somethingelse");
        expect(this.request.getAttribute("nameAttribute")).andReturn("name");
        expect(this.request.getAttribute("emailAttribute")).andReturn("email");
        replay(this.request);
        User user = this.factory.createUser(this.request);
        assertEquals("email", user.getEmail());
        assertEquals("18b3f463d233d2e2764493fb5c951523@domain", user.getHashedId());
        assertEquals("user@domain", user.getId());
        assertEquals("name", user.getName());
        assertEquals(Status.UNKNOWN, user.getStatus());
        verify(this.request);
    }

    @Test
    public void testCreateUserMultiValueName() {
        expect(this.request.getRemoteUser()).andReturn("id@domain");
        expect(this.request.getAttribute("nameAttribute")).andReturn("first name;another name");
        expect(this.request.getAttribute("emailAttribute")).andReturn("mail;mail2");
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
    public void testCreateUserNullRemoteUser() {
        expect(this.request.getRemoteUser()).andReturn(null);
        replay(this.request);
        assertNull(this.factory.createUser(this.request));
        verify(this.request);
    }
}
