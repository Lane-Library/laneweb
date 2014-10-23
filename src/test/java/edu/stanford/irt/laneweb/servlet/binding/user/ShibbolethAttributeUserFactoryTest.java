package edu.stanford.irt.laneweb.servlet.binding.user;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.util.Collections;

import javax.servlet.http.HttpServletRequest;

import org.junit.Before;
import org.junit.Test;

import edu.stanford.irt.laneweb.user.User;

public class ShibbolethAttributeUserFactoryTest {

    private ShibbolethAttributeUserFactory factory;

    private HttpServletRequest request;

    private User user;

    private UserFactory userFactory;

    @Before
    public void setUp() {
        this.userFactory = createMock(UserFactory.class);
        this.factory = new ShibbolethAttributeUserFactory(Collections.singletonMap("provider", this.userFactory));
        this.request = createMock(HttpServletRequest.class);
        this.user = createMock(User.class);
    }

    @Test
    public void testCreateUser() {
        expect(this.request.getAttribute("Shib-Identity-Provider")).andReturn("provider");
        expect(this.userFactory.createUser(this.request)).andReturn(this.user);
        replay(this.userFactory, this.request, this.user);
        assertSame(this.user, this.factory.createUser(this.request));
        verify(this.userFactory, this.request, this.user);
    }

    @Test
    public void testCreateUserNoProvider() {
        expect(this.request.getAttribute("Shib-Identity-Provider")).andReturn(null);
        replay(this.userFactory, this.request, this.user);
        assertNull(this.factory.createUser(this.request));
        verify(this.userFactory, this.request, this.user);
    }
}
