package edu.stanford.irt.laneweb.model;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import java.util.Collections;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.junit.Before;
import org.junit.Test;

import edu.stanford.irt.laneweb.servlet.TemplateChooser;

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
        this.request.setAttribute("template", "default");
        replay(this.request);
        this.templateChooser.setupTemplate(this.request);
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
        this.request.setAttribute("template", "bassettLargerView");
        replay(this.request);
        this.templateChooser.setupTemplate(this.request);
        verify(this.request);
    }

    @Test
    public void testTemplateParameter() {
        expect(this.request.getParameter("template")).andReturn("foo");
        this.request.setAttribute("template", "foo");
        replay(this.request);
        this.templateChooser.setupTemplate(this.request);
        verify(this.request);
    }
}
