package edu.stanford.irt.laneweb.cocoon;

import static org.easymock.EasyMock.expect;
import static org.easymock.classextension.EasyMock.createMock;
import static org.easymock.classextension.EasyMock.replay;
import static org.easymock.classextension.EasyMock.verify;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import edu.stanford.irt.laneweb.model.Model;

public class RequestSchemeSelectorTest {

    private Model model;

    private RequestSchemeSelector selector;

    @Before
    public void setUp() throws Exception {
        this.selector = new RequestSchemeSelector();
        this.model = createMock(Model.class);
        this.selector.setModel(this.model);
    }

    @Test
    public void testSelectHTTP() {
        expect(this.model.getString(Model.REQUEST_SCHEME)).andReturn("http");
        replay(this.model);
        assertTrue(this.selector.select("http", null, null));
        verify(this.model);
    }
    
    @Test
    public void testSelectHTTPS() {
        expect(this.model.getString(Model.REQUEST_SCHEME)).andReturn("https");
        replay(this.model);
        assertTrue(this.selector.select("https", null, null));
        verify(this.model);
    }
    
}
