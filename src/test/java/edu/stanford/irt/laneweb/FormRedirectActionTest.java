package edu.stanford.irt.laneweb;

import static org.easymock.EasyMock.expect;
import static org.easymock.classextension.EasyMock.createMock;
import static org.easymock.classextension.EasyMock.replay;
import static org.easymock.classextension.EasyMock.verify;
import static org.junit.Assert.assertEquals;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

import org.apache.avalon.framework.parameters.Parameters;
import org.junit.Before;
import org.junit.Test;

public class FormRedirectActionTest {

    private FormRedirectAction action;

    private Parameters params;

    @Before
    public void setUp() throws Exception {
        this.action = new FormRedirectAction();
        this.params = createMock(Parameters.class);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testAct() {
        expect(this.params.getParameter("q", null)).andReturn("cardiology");
        expect(this.params.getParameter("source", null)).andReturn("http://lane.stanford.edu/search.html?q={search-terms}&source=all");
        replay(this.params);
        Map result = this.action.act(null, null, null, null, this.params);
        assertEquals("http://lane.stanford.edu/search.html?q=cardiology&source=all", result.get("form-redirect-key"));
        verify(this.params);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testActRegexChars() throws UnsupportedEncodingException {
        expect(this.params.getParameter("q", null)).andReturn("$2cardiology");
        expect(this.params.getParameter("source", null)).andReturn("http://lane.stanford.edu/search.html?q={search-terms}&source=all");
        replay(this.params);
        Map result = this.action.act(null, null, null, null, this.params);
        assertEquals("http://lane.stanford.edu/search.html?q=" + URLEncoder.encode("$2cardiology", "UTF-8") + "&source=all", result
                .get("form-redirect-key"));
        verify(this.params);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testActBackslash() throws UnsupportedEncodingException {
        expect(this.params.getParameter("q", null)).andReturn("\\cardiology");
        expect(this.params.getParameter("source", null)).andReturn("http://lane.stanford.edu/search.html?q={search-terms}&source=all");
        replay(this.params);
        Map result = this.action.act(null, null, null, null, this.params);
        assertEquals("http://lane.stanford.edu/search.html?q=" + URLEncoder.encode("\\cardiology", "UTF-8") + "&source=all", result
                .get("form-redirect-key"));
        verify(this.params);
    }

}
