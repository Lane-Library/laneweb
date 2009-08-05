package edu.stanford.irt.laneweb.webdash;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class WebdashLoginTest {

    private WebdashLogin webdashLogin;

    @Before
    public void setUp() throws Exception {
        this.webdashLogin = new WebdashLogin();
        this.webdashLogin.setWebdashKey("webdashKey");
    }

    @Test
    public void testErrorURL() {
        assertEquals("/webdashError.html", this.webdashLogin.getWebdashURL(null, null, null, "4ca14d60146ddff8ca128a7121854933", null));
        assertEquals("/webdashError.html", this.webdashLogin.getWebdashURL("ceyates", "Charles E Yates", "stanford:staff", null, null));
    }

    @Test
    public void testLoginURL() {
        assertEquals(
                "https://webda.sh/auth/auth_post?email=ceyates%40stanford.edu&fullname=Charles%20E%20Yates&nonce=4ca14d60146ddff8ca128a7121854933&subgroup=staff&system_short_name=stanford-sunet&system_user_id=ceyates&token=7fbbcfd9e1af49678dbc3981be0ec418396cfe22",
                this.webdashLogin.getWebdashURL("ceyates", "Charles E Yates", "stanford:staff", "4ca14d60146ddff8ca128a7121854933", "ceyates"));
    }

    @Test
    public void testRegisterURL() {
        assertEquals(
                "https://webda.sh/auth/init_post?email=ceyates%40stanford.edu&fullname=Charles%20E%20Yates&nonce=4ca14d60146ddff8ca128a7121854933&subgroup=staff&system_short_name=stanford-sunet&system_user_id=ceyates&token=7fbbcfd9e1af49678dbc3981be0ec418396cfe22",
                this.webdashLogin.getWebdashURL("ceyates", "Charles E Yates", "stanford:staff", "4ca14d60146ddff8ca128a7121854933", null));
    }
}
