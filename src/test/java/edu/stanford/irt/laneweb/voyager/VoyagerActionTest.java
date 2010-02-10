package edu.stanford.irt.laneweb.voyager;

import static org.easymock.EasyMock.expect;
import static org.easymock.classextension.EasyMock.createMock;
import static org.easymock.classextension.EasyMock.replay;
import static org.easymock.classextension.EasyMock.verify;
import static org.junit.Assert.assertEquals;

import org.apache.avalon.framework.parameters.Parameters;
import org.junit.Before;
import org.junit.Test;

public class VoyagerActionTest {

    private VoyagerAction action;

    private Parameters params;

    private VoyagerLogin voyagerLogin;

    @Before
    public void setUp() throws Exception {
        this.action = new VoyagerAction();
        this.voyagerLogin = createMock(VoyagerLogin.class);
        this.params = createMock(Parameters.class);
    }

    @Test
    public void testAct() throws Exception {
        expect(this.params.getParameter("pid", null)).andReturn("123");
        expect(this.params.getParameter("query-string", null)).andReturn("a=b");
        expect(this.params.getParameter("univid", null)).andReturn("1234");
        replay(this.params);
        expect(this.voyagerLogin.getVoyagerURL("1234", "123", "a=b")).andReturn("hello");
        replay(this.voyagerLogin);
        this.action.setVoyagerLogin(this.voyagerLogin);
        assertEquals(this.action.act(null, null, null, null, this.params).get("voyager-url"), "hello");
        verify(this.params);
        verify(this.voyagerLogin);
    }
}
