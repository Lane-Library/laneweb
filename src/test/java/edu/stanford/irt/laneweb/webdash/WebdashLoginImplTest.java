package edu.stanford.irt.laneweb.webdash;

import static org.easymock.EasyMock.expect;
import static org.easymock.classextension.EasyMock.createMock;
import static org.easymock.classextension.EasyMock.replay;
import static org.easymock.classextension.EasyMock.verify;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;

import edu.stanford.irt.directory.LDAPPerson;

public class WebdashLoginImplTest {

    private WebdashLoginImpl webdashLogin;

    private LDAPPerson person;

    @Before
    public void setUp() throws Exception {
        this.webdashLogin = new WebdashLoginImpl();
        this.person = createMock(LDAPPerson.class);
    }

    @Test
    public void testRegisterUrl() {
        try {
            this.webdashLogin.getRegistrationURL(null, "nonce");
            fail();
        } catch (IllegalArgumentException e) {
        }
        try {
            this.webdashLogin.getRegistrationURL(this.person, null);
            fail();
        } catch (IllegalArgumentException e) {
        }
        expect(this.person.getUId()).andReturn("ceyates");
        expect(this.person.getDisplayName()).andReturn("Charles E Yates");
        expect(this.person.getAffilation()).andReturn(
                new String[] { "stanford:staff" });
        replay(this.person);
        this.webdashLogin.setGroupName("stanford-sunet");
        this.webdashLogin.setWebdashKey("webdashKey");
        this.webdashLogin
                .setRegistrationURL("https://webda.sh/auth/init_post?");
        this.webdashLogin.setLoginURL("https://webda.sh/auth/auth_post?");
        assertEquals(
                "https://webda.sh/auth/init_post?email=ceyates%40stanford.edu&fullname=Charles%20E%20Yates&nonce=4ca14d60146ddff8ca128a7121854933&subgroup=staff&system_short_name=stanford-sunet&system_user_id=ceyates&token=7fbbcfd9e1af49678dbc3981be0ec418396cfe22",
                this.webdashLogin.getRegistrationURL(this.person,
                        "4ca14d60146ddff8ca128a7121854933"));
        verify(this.person);
    }

    @Test
    public void testLoginUrl() {
        try {
            this.webdashLogin.getLoginURL(null, "nonce");
            fail();
        } catch (IllegalArgumentException e) {
        }
        try {
            this.webdashLogin.getLoginURL(this.person, null);
            fail();
        } catch (IllegalArgumentException e) {
        }
        expect(this.person.getUId()).andReturn("ceyates");
        expect(this.person.getDisplayName()).andReturn("Charles E Yates");
        expect(this.person.getAffilation()).andReturn(
                new String[] { "stanford:staff" });
        replay(this.person);
        this.webdashLogin.setGroupName("stanford-sunet");
        this.webdashLogin.setWebdashKey("webdashKey");
        this.webdashLogin
                .setRegistrationURL("https://webda.sh/auth/init_post?");
        this.webdashLogin.setLoginURL("https://webda.sh/auth/auth_post?");
        assertEquals(
                "https://webda.sh/auth/auth_post?email=ceyates%40stanford.edu&fullname=Charles%20E%20Yates&nonce=4ca14d60146ddff8ca128a7121854933&subgroup=staff&system_short_name=stanford-sunet&system_user_id=ceyates&token=7fbbcfd9e1af49678dbc3981be0ec418396cfe22",
                this.webdashLogin.getLoginURL(this.person,
                        "4ca14d60146ddff8ca128a7121854933"));
        verify(this.person);
    }

}
