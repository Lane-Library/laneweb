package edu.stanford.irt.laneweb;

import static org.easymock.EasyMock.expect;
import static org.easymock.classextension.EasyMock.createMock;
import static org.easymock.classextension.EasyMock.replay;
import static org.easymock.classextension.EasyMock.verify;
import static org.junit.Assert.assertEquals;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

import org.apache.cocoon.environment.Request;
import org.junit.Before;
import org.junit.Test;

public class FormRedirectActionTest {

    private FormRedirectAction action;

    private Map objectModel;

    private Request request;

    @Before
    public void setUp() throws Exception {
        this.action = new FormRedirectAction();
        this.objectModel = createMock(Map.class);
        this.request = createMock(Request.class);
    }

    @Test
    public void testAct() {
        expect(this.objectModel.get("request")).andReturn(this.request);
        replay(this.objectModel);
        expect(this.request.getParameter("q")).andReturn("cardiology");
        expect(this.request.getParameter("source"))
                .andReturn(
                        "http://lane.stanford.edu/search.html?q={search-terms}&source=all");
        replay(this.request);
        Map result = this.action.act(null, null, this.objectModel, null, null);
        assertEquals(
                "http://lane.stanford.edu/search.html?q=cardiology&source=all",
                result.get("form-redirect-key"));
        verify(this.objectModel);
        verify(this.request);
    }

    @Test
    public void testActRegexChars() throws UnsupportedEncodingException {
        expect(this.objectModel.get("request")).andReturn(this.request);
        replay(this.objectModel);
        expect(this.request.getParameter("q")).andReturn("$2cardiology");
        expect(this.request.getParameter("source"))
                .andReturn(
                        "http://lane.stanford.edu/search.html?q={search-terms}&source=all");
        replay(this.request);
        Map result = this.action.act(null, null, this.objectModel, null, null);
        assertEquals("http://lane.stanford.edu/search.html?q="
                + URLEncoder.encode("$2cardiology", "UTF-8") + "&source=all",
                result.get("form-redirect-key"));
        verify(this.objectModel);
        verify(this.request);
    }

    @Test
    public void testActBackslash() throws UnsupportedEncodingException {
        expect(this.objectModel.get("request")).andReturn(this.request);
        replay(this.objectModel);
        expect(this.request.getParameter("q")).andReturn("\\cardiology");
        expect(this.request.getParameter("source"))
                .andReturn(
                        "http://lane.stanford.edu/search.html?q={search-terms}&source=all");
        replay(this.request);
        Map result = this.action.act(null, null, this.objectModel, null, null);
        assertEquals("http://lane.stanford.edu/search.html?q="
                + URLEncoder.encode("\\cardiology", "UTF-8") + "&source=all",
                result.get("form-redirect-key"));
        verify(this.objectModel);
        verify(this.request);
    }

}
