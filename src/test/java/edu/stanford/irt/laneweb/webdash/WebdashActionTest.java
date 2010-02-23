package edu.stanford.irt.laneweb.webdash;

import static org.easymock.EasyMock.expect;
import static org.easymock.classextension.EasyMock.createMock;
import static org.easymock.classextension.EasyMock.replay;
import static org.easymock.classextension.EasyMock.verify;
import static org.junit.Assert.assertEquals;

import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import edu.stanford.irt.laneweb.model.LanewebObjectModel;
import edu.stanford.irt.laneweb.model.Model;

public class WebdashActionTest {

    private WebdashAction action;
    
    private Model model;

    private WebdashLogin webdashLogin;

    @Before
    public void setUp() throws Exception {
        this.action = new WebdashAction();
        this.model = createMock(Model.class);
        this.webdashLogin = createMock(WebdashLogin.class);
        this.action.setModel(this.model);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testError() {
        expect(this.model.getString(LanewebObjectModel.NONCE)).andReturn(null);
        expect(this.model.getString(LanewebObjectModel.SYSTEM_USER_ID)).andReturn("ceyates");
        expect(this.model.getString(LanewebObjectModel.SUNETID)).andReturn(null);
        expect(this.model.getString(LanewebObjectModel.NAME)).andReturn(null);
        expect(this.model.getString(LanewebObjectModel.AFFILIATION)).andReturn(null);
        replay(this.model);
        expect(this.webdashLogin.getWebdashURL(null, null, null, null, "ceyates")).andReturn("broken");
        replay(this.webdashLogin);
        this.action.setWebdashLogin(this.webdashLogin);
        Map result = this.action.doAct();
        assertEquals("broken", result.get("webdash-url"));
        verify(this.model);
        verify(this.webdashLogin);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testLogin() {
        expect(this.model.getString(LanewebObjectModel.NONCE)).andReturn(LanewebObjectModel.NONCE);
        expect(this.model.getString(LanewebObjectModel.SYSTEM_USER_ID)).andReturn("ceyates");
        expect(this.model.getString(LanewebObjectModel.SUNETID)).andReturn(null);
        expect(this.model.getString(LanewebObjectModel.NAME)).andReturn(null);
        expect(this.model.getString(LanewebObjectModel.AFFILIATION)).andReturn(null);
        replay(this.model);
        expect(this.webdashLogin.getWebdashURL(null, null, null, LanewebObjectModel.NONCE, "ceyates")).andReturn("login");
        replay(this.webdashLogin);
        this.action.setWebdashLogin(this.webdashLogin);
        Map result = this.action.doAct();
        assertEquals("login", result.get("webdash-url"));
        verify(this.model);
        verify(this.webdashLogin);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testRegister() {
        expect(this.model.getString(LanewebObjectModel.NONCE)).andReturn(LanewebObjectModel.NONCE);
        expect(this.model.getString(LanewebObjectModel.SYSTEM_USER_ID)).andReturn(null);
        expect(this.model.getString(LanewebObjectModel.SUNETID)).andReturn(null);
        expect(this.model.getString(LanewebObjectModel.NAME)).andReturn(null);
        expect(this.model.getString(LanewebObjectModel.AFFILIATION)).andReturn(null);
        replay(this.model);
        expect(this.webdashLogin.getWebdashURL(null, null, null, LanewebObjectModel.NONCE, null)).andReturn("register");
        replay(this.webdashLogin);
        this.action.setWebdashLogin(this.webdashLogin);
        Map result = this.action.doAct();
        assertEquals("register", result.get("webdash-url"));
        verify(this.model);
        verify(this.webdashLogin);
    }
}
