package edu.stanford.irt.laneweb.webdash;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

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
    public void testError() {
        try {
            this.webdashLogin.getWebdashURL(null, null, null, "4ca14d60146ddff8ca128a7121854933", null);
            fail();
        } catch (IllegalArgumentException e) {
        }
        try {
            this.webdashLogin.getWebdashURL("ceyates", "Charles E Yates", "stanford:staff", null, null);
            fail();
        } catch (IllegalArgumentException e) {
        }
    }

    @Test
    public void testLoginURL() {
        assertEquals(
                "https://webda.sh/auth/auth_post?email=ceyates%40stanford.edu&fullname=Charles%20E%20Yates&nonce=4ca14d60146ddff8ca128a7121854933&subgroup=staff&system_short_name=stanford-sunet&system_user_id=ceyates&token=7fbbcfd9e1af49678dbc3981be0ec418396cfe22",
                this.webdashLogin.getWebdashURL("ceyates", "Charles E Yates", "stanford:staff",
                        "4ca14d60146ddff8ca128a7121854933", "ceyates"));
    }

    @Test
    public void testLoginURLSpecialGroup() {
        assertEquals(
                "https://webda.sh/auth/auth_post?email=ceyates%40stanford.edu&fullname=Charles%20E%20Yates&nonce=4ca14d60146ddff8ca128a7121854933&subgroup=staff%3Aacademic&system_short_name=stanford-sunet&system_user_id=ceyates&token=e9a39d1f8b4a29598b2fe01e9b9cbd970f116d32",
                this.webdashLogin.getWebdashURL("ceyates", "Charles E Yates", "stanford:staff:academic",
                        "4ca14d60146ddff8ca128a7121854933", "ceyates"));
    }

    @Test
    public void testRegisterURL() {
        assertEquals(
                "https://webda.sh/auth/init_post?email=ceyates%40stanford.edu&fullname=Charles%20E%20Yates&nonce=4ca14d60146ddff8ca128a7121854933&subgroup=staff&system_short_name=stanford-sunet&system_user_id=ceyates&token=7fbbcfd9e1af49678dbc3981be0ec418396cfe22",
                this.webdashLogin.getWebdashURL("ceyates", "Charles E Yates", "stanford:staff",
                        "4ca14d60146ddff8ca128a7121854933", null));
    }

    @Test
    public void testRegisterURLSpecialGroup() {
        assertEquals(
                "https://webda.sh/auth/init_post?email=ceyates%40stanford.edu&fullname=Charles%20E%20Yates&nonce=4ca14d60146ddff8ca128a7121854933&subgroup=staff%3Aacademic&system_short_name=stanford-sunet&system_user_id=ceyates&token=e9a39d1f8b4a29598b2fe01e9b9cbd970f116d32",
                this.webdashLogin.getWebdashURL("ceyates", "Charles E Yates", "stanford:staff:academic",
                        "4ca14d60146ddff8ca128a7121854933", null));
    }
}
