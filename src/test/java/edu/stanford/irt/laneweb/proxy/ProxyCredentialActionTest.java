package edu.stanford.irt.laneweb.proxy;

import static org.easymock.EasyMock.expect;
import static org.easymock.classextension.EasyMock.createMock;
import static org.easymock.classextension.EasyMock.replay;
import static org.easymock.classextension.EasyMock.verify;
import static org.junit.Assert.fail;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import edu.stanford.irt.laneweb.model.Model;

public class ProxyCredentialActionTest {

    private ProxyCredentialAction action;

    private Map<String, Object> model;

    @Before
    public void setUp() {
        this.action = new ProxyCredentialAction();
        this.model = new HashMap<String, Object>();
    }

    @Test
    public void testDoAct() throws Exception {
        this.model.put(Model.QUERY_STRING, "foo");
        this.model.put(Model.SUNETID, "foo");
        try {
            this.action.act(null, null, this.model, null, null);
            fail("should throw IllegalStateException, null ticket");
        } catch (IllegalStateException e) {
        }
    }
}
