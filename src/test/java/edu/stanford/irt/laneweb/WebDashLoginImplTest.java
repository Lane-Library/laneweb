package edu.stanford.irt.laneweb;

import static org.easymock.EasyMock.expect;
import static org.easymock.classextension.EasyMock.createMock;
import static org.easymock.classextension.EasyMock.replay;
import static org.easymock.classextension.EasyMock.verify;
import static org.junit.Assert.assertEquals;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import org.apache.avalon.framework.parameters.ParameterException;
import org.apache.avalon.framework.parameters.Parameters;
import org.apache.cocoon.environment.Request;
import org.junit.Before;
import org.junit.Test;

import edu.stanford.irt.directory.LDAPPerson;

public class WebDashLoginImplTest {

    private WebDashLoginImpl webdashLogin;

    private LDAPPerson person;

    private Request request;

    private Parameters parameters;

    @Before
    public void setUp() throws Exception {
        this.webdashLogin = new WebDashLoginImpl();
        this.person = createMock(LDAPPerson.class);
        this.parameters = createMock(Parameters.class);
        this.request = createMock(Request.class);
    }

    @Test
    public void testRegisterUrl() throws InvalidKeyException,
            UnsupportedEncodingException, NoSuchAlgorithmException,
            ParameterException {
        expect(this.person.getDisplayName()).andReturn("Charles E Yates");
        expect(this.person.getDisplayName()).andReturn("Charles E Yates");
        expect(this.person.getAffilation()).andReturn(
                new String[] { "stanford:staff" });
        expect(this.person.getAffilation()).andReturn(
                new String[] { "stanford:staff" });
        expect(this.person.getUId()).andReturn("ceyates");
        expect(this.person.getUId()).andReturn("ceyates");
        expect(this.person.getUId()).andReturn("ceyates");
        expect(this.person.getDisplayName()).andReturn("Charles E Yates");
        expect(this.person.getUId()).andReturn("ceyates");
        expect(this.person.getAffilation()).andReturn(
                new String[] { "stanford:staff" });
        replay(this.person);
        expect(this.parameters.getParameter("webdashRegistrationURL"))
                .andReturn("https://webda.sh/auth/init_post?");
        expect(this.parameters.getParameter("webdashLoginURL")).andReturn(
                "https://webda.sh/auth/auth_post?");
        expect(this.parameters.getParameter("groupName")).andReturn(
                "stanford-sunet");
        replay(this.parameters);
        expect(this.request.getParameter("nonce")).andReturn(
                "4ca14d60146ddff8ca128a7121854933");
        expect(this.request.getParameter("nonce")).andReturn(
                "4ca14d60146ddff8ca128a7121854933");
        expect(this.request.getParameter("nonce")).andReturn(
                "4ca14d60146ddff8ca128a7121854933");
        expect(this.request.getParameter("system_user_id")).andReturn(null);
        replay(this.request);
        this.webdashLogin.parameterize(this.parameters);
        this.webdashLogin.setWebdashKey("webdashKey");
        assertEquals(
                "https://webda.sh/auth/init_post?email=ceyates%40stanford.edu&fullname=Charles%20E%20Yates&nonce=4ca14d60146ddff8ca128a7121854933&subgroup=staff&system_short_name=stanford-sunet&system_user_id=ceyates&token=7fbbcfd9e1af49678dbc3981be0ec418396cfe22",
                this.webdashLogin.getEncodedUrl(this.person, this.request));
        verify(this.person);
        verify(this.parameters);
        verify(this.request);
    }

    @Test
    public void testLoginUrl() throws InvalidKeyException,
            UnsupportedEncodingException, NoSuchAlgorithmException,
            ParameterException {

        expect(this.person.getDisplayName()).andReturn("Charles E Yates");
        expect(this.person.getDisplayName()).andReturn("Charles E Yates");
        expect(this.person.getAffilation()).andReturn(
                new String[] { "stanford:staff" });
        expect(this.person.getAffilation()).andReturn(
                new String[] { "stanford:staff" });
        expect(this.person.getUId()).andReturn("ceyates");
        expect(this.person.getUId()).andReturn("ceyates");
        expect(this.person.getUId()).andReturn("ceyates");
        expect(this.person.getDisplayName()).andReturn("Charles E Yates");
        expect(this.person.getUId()).andReturn("ceyates");
        expect(this.person.getAffilation()).andReturn(
                new String[] { "stanford:staff" });
        replay(this.person);
        expect(this.parameters.getParameter("webdashRegistrationURL"))
                .andReturn("https://webda.sh/auth/init_post?");
        expect(this.parameters.getParameter("webdashLoginURL")).andReturn(
                "https://webda.sh/auth/auth_post?");
        expect(this.parameters.getParameter("groupName")).andReturn(
                "stanford-sunet");
        replay(this.parameters);
        expect(this.request.getParameter("nonce")).andReturn(
                "4ca14d60146ddff8ca128a7121854933");
        expect(this.request.getParameter("nonce")).andReturn(
                "4ca14d60146ddff8ca128a7121854933");
        expect(this.request.getParameter("nonce")).andReturn(
                "4ca14d60146ddff8ca128a7121854933");
        expect(this.request.getParameter("system_user_id"))
                .andReturn("ceyates");
        replay(this.request);
        this.webdashLogin.parameterize(this.parameters);
        this.webdashLogin.setWebdashKey("webdashKey");
        assertEquals(
                "https://webda.sh/auth/auth_post?email=ceyates%40stanford.edu&fullname=Charles%20E%20Yates&nonce=4ca14d60146ddff8ca128a7121854933&subgroup=staff&system_short_name=stanford-sunet&system_user_id=ceyates&token=7fbbcfd9e1af49678dbc3981be0ec418396cfe22",
                this.webdashLogin.getEncodedUrl(this.person, this.request));
        verify(this.person);
        verify(this.parameters);
        verify(this.request);
    }

}
