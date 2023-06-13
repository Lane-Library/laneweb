package edu.stanford.irt.laneweb.servlet.binding;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.mock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.HashMap;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;

import org.junit.Before;
import org.junit.Test;

import edu.stanford.irt.laneweb.model.Model;

public class RequestMethodDataBinderTest {

    private RequestMethodDataBinder binder;

    private Map<String, Object> model;

    private HttpServletRequest request;

    @Before
    public void setUp() throws Exception {
        this.binder = new RequestMethodDataBinder();
        this.model = new HashMap<>();
        this.request = mock(HttpServletRequest.class);
    }

    @Test
    public void testBind() {
        expect(this.request.getRequestURI()).andReturn("requestURI");
        expect(this.request.getQueryString()).andReturn("queryString");
        expect(this.request.getServletPath()).andReturn("servletPath");
        replay(this.request);
        this.binder.bind(this.model, this.request);
        assertEquals("requestURI", this.model.get(Model.REQUEST_URI));
        assertEquals("queryString", this.model.get(Model.QUERY_STRING));
        assertEquals("servletPath", this.model.get(Model.SERVLET_PATH));
        verify(this.request);
    }

    @Test
    public void testBindNullValues() {
        expect(this.request.getRequestURI()).andReturn(null);
        expect(this.request.getQueryString()).andReturn(null);
        expect(this.request.getServletPath()).andReturn(null);
        replay(this.request);
        this.binder.bind(this.model, this.request);
        assertNull(this.model.get(Model.REQUEST_URI));
        assertNull(this.model.get(Model.QUERY_STRING));
        assertNull(this.model.get(Model.SERVLET_PATH));
        verify(this.request);
    }
}
