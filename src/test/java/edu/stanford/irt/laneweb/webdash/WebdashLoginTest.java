package edu.stanford.irt.laneweb.webdash;

import static org.easymock.EasyMock.expect;
import static org.easymock.classextension.EasyMock.createMock;
import static org.easymock.classextension.EasyMock.replay;
import static org.easymock.classextension.EasyMock.verify;
import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import edu.stanford.irt.laneweb.user.User;

public class WebdashLoginTest {

    private User person;

    private WebdashLogin webdashLogin;

    @Before
    public void setUp() throws Exception {
        this.webdashLogin = new WebdashLogin();
        this.webdashLogin.setWebdashKey("webdashKey");
        this.person = createMock(User.class);
    }

    @Test
    public void testErrorURL() {
        replay(this.person);
        assertEquals("/webdashError.html", this.webdashLogin.getWebdashURL(null, "4ca14d60146ddff8ca128a7121854933", null));
        assertEquals("/webdashError.html", this.webdashLogin.getWebdashURL(this.person, null, null));
        verify(this.person);
    }

    @Test
    public void testLoginURL() {
        expect(this.person.getSunetId()).andReturn("ceyates");
        expect(this.person.getName()).andReturn("Charles E Yates");
        expect(this.person.getAffilation()).andReturn("stanford:staff");
        replay(this.person);
        assertEquals(
                "https://webda.sh/auth/auth_post?email=ceyates%40stanford.edu&fullname=Charles%20E%20Yates&nonce=4ca14d60146ddff8ca128a7121854933&subgroup=staff&system_short_name=stanford-sunet&system_user_id=ceyates&token=7fbbcfd9e1af49678dbc3981be0ec418396cfe22",
                this.webdashLogin.getWebdashURL(this.person, "4ca14d60146ddff8ca128a7121854933", "ceyates"));
        verify(this.person);
    }

    @Test
    public void testRegisterURL() {
        expect(this.person.getSunetId()).andReturn("ceyates");
        expect(this.person.getName()).andReturn("Charles E Yates");
        expect(this.person.getAffilation()).andReturn("stanford:staff");
        replay(this.person);
        assertEquals(
                "https://webda.sh/auth/init_post?email=ceyates%40stanford.edu&fullname=Charles%20E%20Yates&nonce=4ca14d60146ddff8ca128a7121854933&subgroup=staff&system_short_name=stanford-sunet&system_user_id=ceyates&token=7fbbcfd9e1af49678dbc3981be0ec418396cfe22",
                this.webdashLogin.getWebdashURL(this.person, "4ca14d60146ddff8ca128a7121854933", null));
        verify(this.person);
    }
}
