package edu.stanford.irt.laneweb.servlet.binding;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.mock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import edu.stanford.irt.laneweb.model.Model;

public class RequestParameterDataBinderTest {

    private RequestParameterDataBinder binder;

    private Map<String, Object> model;

    private Enumeration<String> names;

    private HttpServletRequest request;

    @BeforeEach
    public void setUp() throws Exception {
        this.binder = new RequestParameterDataBinder();
        this.model = new HashMap<>();
        this.request = mock(HttpServletRequest.class);
        this.names = mock(Enumeration.class);
    }

    @Test
    public void testBind() {
        expect(this.request.getParameterNames()).andReturn(this.names);
        expect(this.names.hasMoreElements()).andReturn(true);
        expect(this.names.nextElement()).andReturn(Model.SOURCE);
        expect(this.request.getParameter(Model.SOURCE)).andReturn("foo");
        expect(this.names.hasMoreElements()).andReturn(false);
        replay(this.request, this.names);
        this.binder.bind(this.model, this.request);
        assertEquals("foo", this.model.get(Model.SOURCE));
        verify(this.request, this.names);
    }

    @Test
    public void testBindList() {
        expect(this.request.getParameterNames()).andReturn(this.names);
        expect(this.names.hasMoreElements()).andReturn(true);
        expect(this.names.nextElement()).andReturn("e");
        expect(this.request.getParameter("e")).andReturn("foo");
        expect(this.request.getParameterValues("e")).andReturn(new String[] { "foo", "bar" });
        expect(this.names.hasMoreElements()).andReturn(false);
        replay(this.request, this.names);
        this.binder.bind(this.model, this.request);
        assertEquals(this.model.get(Model.ENGINES), Arrays.asList(new String[] { "foo", "bar" }));
        verify(this.request, this.names);
    }

    @Test
    public void testBindQuery() {
        expect(this.request.getParameterNames()).andReturn(this.names);
        expect(this.names.hasMoreElements()).andReturn(true);
        expect(this.names.nextElement()).andReturn("q");
        expect(this.request.getParameter("q")).andReturn("foo?bar");
        expect(this.names.hasMoreElements()).andReturn(false);
        replay(this.request, this.names);
        this.binder.bind(this.model, this.request);
        assertEquals("foo?bar", this.model.get(Model.QUERY));
        assertEquals("foo%3Fbar", this.model.get("url-encoded-query"));
        verify(this.request, this.names);
    }

    @Test
    public void testBothSingleAndMultiple() {
        expect(this.request.getParameterNames()).andReturn(this.names);
        expect(this.names.hasMoreElements()).andReturn(true);
        expect(this.names.nextElement()).andReturn("r");
        expect(this.request.getParameter("r")).andReturn("foo");
        expect(this.request.getParameterValues("r")).andReturn(new String[] { "foo", "bar" });
        expect(this.names.hasMoreElements()).andReturn(false);
        replay(this.request, this.names);
        this.binder.bind(this.model, this.request);
        assertEquals(this.model.get(Model.RESOURCES), Arrays.asList(new String[] { "foo", "bar" }));
        assertEquals("foo", this.model.get(Model.REGION));
        verify(this.request, this.names);
    }
}
