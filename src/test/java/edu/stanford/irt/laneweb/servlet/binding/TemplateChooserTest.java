package edu.stanford.irt.laneweb.servlet.binding;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.mock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import jakarta.servlet.http.HttpServletRequest;

import org.junit.Before;
import org.junit.Test;

import edu.stanford.irt.laneweb.model.Model;

public class TemplateChooserTest {

    private String defaultTemplate = "default";

    private Set<String> existingTemplates = new HashSet<>(Arrays.asList(new String[] { "default", "foo" }));

    private HttpServletRequest request;

    private TemplateChooser templateChooser;

    private Map<String, String> templateMap;

    @Before
    public void setUp() throws Exception {
        this.request = mock(HttpServletRequest.class);
        this.templateChooser = new TemplateChooser(this.defaultTemplate, this.existingTemplates, this.templateMap);
    }

    @Test
    public void testDefaultTemplate() {
        expect(this.request.getParameter(Model.TEMPLATE)).andReturn(null);
        replay(this.request);
        assertEquals(this.defaultTemplate, this.templateChooser.getTemplate(this.request));
        verify(this.request);
    }

    @Test
    public void testTemplateMap() {
        this.templateMap = Collections.singletonMap("^(?:/stage|)/bassett/raw/bassettLargerView.html",
                "bassettLargerView");
        this.templateChooser = new TemplateChooser(this.defaultTemplate, this.existingTemplates, this.templateMap);
        expect(this.request.getParameter(Model.TEMPLATE)).andReturn(null);
        expect(this.request.getServletPath()).andReturn("/stage/bassett/raw/bassettLargerView.html");
        replay(this.request);
        assertEquals("bassettLargerView", this.templateChooser.getTemplate(this.request));
        verify(this.request);
    }

    @Test
    public void testTemplateParameter() {
        expect(this.request.getParameter(Model.TEMPLATE)).andReturn("foo");
        replay(this.request);
        assertEquals("foo", this.templateChooser.getTemplate(this.request));
        verify(this.request);
    }
}
