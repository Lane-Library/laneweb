package edu.stanford.irt.laneweb.voyager;

import static org.easymock.EasyMock.expect;
import static org.easymock.classextension.EasyMock.createMock;
import static org.easymock.classextension.EasyMock.replay;
import static org.easymock.classextension.EasyMock.verify;
import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import edu.stanford.irt.laneweb.model.LanewebObjectModel;
import edu.stanford.irt.laneweb.model.Model;

public class VoyagerActionTest {

    private VoyagerAction action;

    private Model model;

    private VoyagerLogin voyagerLogin;

    @Before
    public void setUp() throws Exception {
        this.action = new VoyagerAction();
        this.voyagerLogin = createMock(VoyagerLogin.class);
        this.model = createMock(Model.class);
        this.action.setModel(this.model);
    }

    @Test
    public void testAct() throws Exception {
        expect(this.model.getString(LanewebObjectModel.PID)).andReturn("123");
        expect(this.model.getString(LanewebObjectModel.QUERY_STRING)).andReturn("a=b");
        expect(this.model.getString(LanewebObjectModel.UNIVID)).andReturn("1234");
        replay(this.model);
        expect(this.voyagerLogin.getVoyagerURL("1234", "123", "a=b")).andReturn("hello");
        replay(this.voyagerLogin);
        this.action.setVoyagerLogin(this.voyagerLogin);
        assertEquals(this.action.doAct().get("voyager-url"), "hello");
        verify(this.model);
        verify(this.voyagerLogin);
    }
}
