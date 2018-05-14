package edu.stanford.irt.laneweb.voyager;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.mock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import edu.stanford.irt.laneweb.LanewebException;

public class VoyagerLoginTest {

    private LoginService service;

    private VoyagerLogin voyagerLogin;

    @Before
    public void setUp() throws Exception {
        this.service = mock(LoginService.class);
        this.voyagerLogin = new VoyagerLogin(this.service);
    }

    @Test
    public void testDeleteError() {
        expect(this.service.login("0999", "123")).andThrow(new LanewebException(""));
        replay(this.service);
        assertEquals("/voyagerError.html", this.voyagerLogin.getVoyagerURL("999", "123", "a=b"));
        verify(this.service);
    }

    @Test
    public void testErrorURL() {
        assertEquals("/voyagerError.html", this.voyagerLogin.getVoyagerURL(null, "123", "a=b"));
        assertEquals("/voyagerError.html", this.voyagerLogin.getVoyagerURL("", null, "a=b"));
        assertEquals("/voyagerError.html", this.voyagerLogin.getVoyagerURL("", null, "a=b"));
    }

    @Test
    public void testLaneLoginURL() {
        expect(this.service.login("0999", "123")).andReturn(true);
        replay(this.service);
        assertEquals("http://lmldb.stanford.edu/cgi-bin/Pwebrecon.cgi?a=b&authenticate=Y",
                this.voyagerLogin.getVoyagerURL("999", "123", "a=b"));
        verify(this.service);
    }

    @Test
    public void testMissingUnivid() {
        expect(this.service.login("0999", "123")).andReturn(false);
        replay(this.service);
        assertEquals("/voyagerError.html", this.voyagerLogin.getVoyagerURL("999", "123", "a=b"));
        verify(this.service);
    }

    @Test
    public void testUpdateError() {
        expect(this.service.login("0999", "123")).andThrow(new LanewebException(""));
        replay(this.service);
        assertEquals("/voyagerError.html", this.voyagerLogin.getVoyagerURL("999", "123", "a=b"));
        verify(this.service);
    }
}
