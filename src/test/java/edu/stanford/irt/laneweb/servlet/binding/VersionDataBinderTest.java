package edu.stanford.irt.laneweb.servlet.binding;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.reset;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;

import org.junit.Before;
import org.junit.Test;

import edu.stanford.irt.laneweb.LanewebException;
import edu.stanford.irt.laneweb.model.Model;

public class VersionDataBinderTest {

    private VersionDataBinder binder;

    private ServletContext context;

    @Before
    public void setUp() throws Exception {
        this.context = createMock(ServletContext.class);
        expect(this.context.getInitParameter("laneweb.context.version")).andReturn("version");
        replay(this.context);
        this.binder = new VersionDataBinder(this.context);
        verify(this.context);
    }

    @Test
    public void testBind() {
        Map<String, Object> model = new HashMap<String, Object>();
        this.binder.bind(model, null);
        assertEquals("version", model.get(Model.VERSION));
    }

    @Test(expected = LanewebException.class)
    public void testNoVersion() {
        reset(this.context);
        expect(this.context.getInitParameter("laneweb.context.version")).andReturn(null);
        replay(this.context);
        new VersionDataBinder(this.context);
    }
}
