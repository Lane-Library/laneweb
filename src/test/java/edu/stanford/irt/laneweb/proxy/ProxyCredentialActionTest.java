package edu.stanford.irt.laneweb.proxy;

import static org.easymock.EasyMock.expect;
import static org.easymock.classextension.EasyMock.createMock;
import static org.easymock.classextension.EasyMock.replay;
import static org.easymock.classextension.EasyMock.verify;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;

import edu.stanford.irt.laneweb.model.Model;

public class ProxyCredentialActionTest {

    private ProxyCredentialAction action;

    private Model model;

    @Before
    public void setUp() {
        this.action = new ProxyCredentialAction();
        this.model = createMock(Model.class);
    }

    @Test
    public void testDoAct() {
        this.action.setModel(this.model);
        expect(this.model.getString(Model.QUERY_STRING)).andReturn("foo");
        expect(this.model.getString(Model.SUNETID)).andReturn("foo");
        expect(this.model.getObject(Model.TICKET, Ticket.class)).andReturn(null);
        replay(this.model);
        try {
            this.action.doAct();
            fail("should throw IllegalStateException, null ticket");
        } catch (IllegalStateException e) {
        }
        verify(this.model);
    }
}
