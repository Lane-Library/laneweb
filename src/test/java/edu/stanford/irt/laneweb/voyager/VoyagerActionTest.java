package edu.stanford.irt.laneweb.voyager;

import static org.easymock.EasyMock.expect;
import static org.easymock.classextension.EasyMock.createMock;
import static org.easymock.classextension.EasyMock.replay;
import static org.easymock.classextension.EasyMock.verify;
import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import edu.stanford.irt.laneweb.model.Model;

public class VoyagerActionTest {

    private VoyagerAction action;

    private Map<String, Object> model;

    private VoyagerLogin voyagerLogin;

    @Before
    public void setUp() throws Exception {
        this.action = new VoyagerAction();
        this.voyagerLogin = createMock(VoyagerLogin.class);
        this.model = new HashMap<String, Object>();
    }

    @Test
    public void testAct() throws Exception {
        this.model.put(Model.PID, "123");
        this.model.put(Model.QUERY_STRING, "a=b");
        this.model.put(Model.UNIVID, "1234");
        expect(this.voyagerLogin.getVoyagerURL("1234", "123", "a=b")).andReturn("hello");
        replay(this.voyagerLogin);
        this.action.setVoyagerLogin(this.voyagerLogin);
        assertEquals(this.action.act(null, null, this.model, null, null).get("voyager-url"), "hello");
        verify(this.voyagerLogin);
    }
}
