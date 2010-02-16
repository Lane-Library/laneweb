package edu.stanford.irt.laneweb.model;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;

import java.util.Collections;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.junit.Before;
import org.junit.Test;

public class TemplateChooserTest {

    private String defaultTemplate = "default";

    private HttpServletRequest request;

    private TemplateChooser templateChooser;

    private Map<String, String> templateMap;

    @Before
    public void setUp() throws Exception {
        this.request = createMock(HttpServletRequest.class);
        this.templateChooser = new TemplateChooser(this.defaultTemplate, this.templateMap);
    }

    @Test
    public void testDefaultTemplate() {
        expect(this.request.getParameter("template")).andReturn(null);
        replay(this.request);
        assertEquals(this.defaultTemplate, this.templateChooser.chooseTemplate(this.request));
        verify(this.request);
    }

    @Test
    public void testTemplateMap() {
        this.templateMap =
                Collections.singletonMap("^(?:/stage|)/bassett/raw/bassettLargerView.html", "bassettLargerView");
        this.templateChooser = new TemplateChooser(this.defaultTemplate, this.templateMap);
        expect(this.request.getParameter("template")).andReturn(null);
        expect(this.request.getRequestURI()).andReturn("/laneweb/stage/bassett/raw/bassettLargerView.html");
        expect(this.request.getContextPath()).andReturn("/laneweb");
        replay(this.request);
        assertEquals("bassettLargerView", this.templateChooser.chooseTemplate(this.request));
        verify(this.request);
    }

    @Test
    public void testTemplateParameter() {
        expect(this.request.getParameter("template")).andReturn("foo");
        replay(this.request);
        assertEquals("foo", this.templateChooser.chooseTemplate(this.request));
        verify(this.request);
    }
}
