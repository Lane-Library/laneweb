package edu.stanford.irt.laneweb.webdash;

import static org.easymock.EasyMock.expect;
import static org.easymock.classextension.EasyMock.createMock;
import static org.easymock.classextension.EasyMock.replay;
import static org.easymock.classextension.EasyMock.verify;
import static org.junit.Assert.assertEquals;

import java.util.Map;

import org.junit.Before;
import org.junit.Test;

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

    @SuppressWarnings("rawtypes")
    @Test
    public void testError() {
        expect(this.model.getString(Model.NONCE)).andReturn(null);
        expect(this.model.getString(Model.SYSTEM_USER_ID)).andReturn("ceyates");
        expect(this.model.getString(Model.SUNETID)).andReturn(null);
        expect(this.model.getString(Model.NAME)).andReturn(null);
        expect(this.model.getString(Model.AFFILIATION)).andReturn(null);
        replay(this.model);
        expect(this.webdashLogin.getWebdashURL(null, null, null, null, "ceyates")).andReturn("broken");
        replay(this.webdashLogin);
        this.action.setWebdashLogin(this.webdashLogin);
        Map result = this.action.doAct();
        assertEquals("broken", result.get("webdash-url"));
        verify(this.model);
        verify(this.webdashLogin);
    }

    @SuppressWarnings("rawtypes")
    @Test
    public void testLogin() {
        expect(this.model.getString(Model.NONCE)).andReturn(Model.NONCE);
        expect(this.model.getString(Model.SYSTEM_USER_ID)).andReturn("ceyates");
        expect(this.model.getString(Model.SUNETID)).andReturn(null);
        expect(this.model.getString(Model.NAME)).andReturn(null);
        expect(this.model.getString(Model.AFFILIATION)).andReturn(null);
        replay(this.model);
        expect(this.webdashLogin.getWebdashURL(null, null, null, Model.NONCE, "ceyates")).andReturn("login");
        replay(this.webdashLogin);
        this.action.setWebdashLogin(this.webdashLogin);
        Map result = this.action.doAct();
        assertEquals("login", result.get("webdash-url"));
        verify(this.model);
        verify(this.webdashLogin);
    }

    @SuppressWarnings("rawtypes")
    @Test
    public void testRegister() {
        expect(this.model.getString(Model.NONCE)).andReturn(Model.NONCE);
        expect(this.model.getString(Model.SYSTEM_USER_ID)).andReturn(null);
        expect(this.model.getString(Model.SUNETID)).andReturn(null);
        expect(this.model.getString(Model.NAME)).andReturn(null);
        expect(this.model.getString(Model.AFFILIATION)).andReturn(null);
        replay(this.model);
        expect(this.webdashLogin.getWebdashURL(null, null, null, Model.NONCE, null)).andReturn("register");
        replay(this.webdashLogin);
        this.action.setWebdashLogin(this.webdashLogin);
        Map result = this.action.doAct();
        assertEquals("register", result.get("webdash-url"));
        verify(this.model);
        verify(this.webdashLogin);
    }
}
