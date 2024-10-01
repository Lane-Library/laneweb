package edu.stanford.irt.laneweb.servlet.binding;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.mock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;

import org.junit.Before;
import org.junit.Test;

import edu.stanford.irt.laneweb.model.Model;

public class TemplateDataBinderTest {

    private TemplateDataBinder binder;

    private TemplateChooser chooser;

    private Map<String, Object> model;

    private HttpServletRequest request;

    @Before
    public void setUp() throws Exception {
        this.chooser = mock(TemplateChooser.class);
        this.binder = new TemplateDataBinder(this.chooser);
        this.model = new HashMap<>();
        this.request = mock(HttpServletRequest.class);
    }

    @Test
    public void testBind() {
        expect(this.chooser.getTemplate(this.request)).andReturn("template");
        replay(this.chooser, this.request);
        this.binder.bind(this.model, this.request);
        assertEquals("template", this.model.get(Model.TEMPLATE));
        verify(this.chooser, this.request);
    }

    @Test
    public void testBindDebug() {
        this.model.put(Model.DEBUG, Boolean.TRUE);
        replay(this.chooser, this.request);
        this.binder.bind(this.model, this.request);
        assertEquals("debug", this.model.get(Model.TEMPLATE));
        verify(this.chooser, this.request);
    }

}
