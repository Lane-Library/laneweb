package edu.stanford.irt.laneweb.servlet.binding;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.junit.Before;
import org.junit.Test;

public class SessionParameterDataBinderTest {

    private static class TestSessionParameterDataBinder extends SessionParameterDataBinder<Object> {

        private Object object;

        public TestSessionParameterDataBinder(final String modelKey, final String parameterName) {
            super(modelKey, parameterName);
        }

        public void setObject(final Object object) {
            this.object = object;
        }

        @Override
        protected Object getParameterAsObject(final String parameterValue) {
            return this.object;
        }
    }

    private TestSessionParameterDataBinder binder;

    private Map<String, Object> model;

    private Object object;

    private HttpServletRequest request;

    private HttpSession session;

    @Before
    public void setUp() throws Exception {
        this.binder = new TestSessionParameterDataBinder("key", "name");
        this.request = createMock(HttpServletRequest.class);
        this.model = new HashMap<String, Object>();
        this.session = createMock(HttpSession.class);
        this.object = new Object();
    }

    @Test
    public void testBind() {
        expect(this.request.getParameter("name")).andReturn(null);
        expect(this.request.getSession()).andReturn(this.session);
        expect(this.session.getAttribute("key")).andReturn(null);
        replay(this.request, this.session);
        this.binder.bind(this.model, this.request);
        assertFalse(this.model.containsKey("key"));
        verify(this.request, this.session);
    }

    @Test
    public void testBindValueInParameter() {
        this.binder.setObject(this.object);
        expect(this.request.getParameter("name")).andReturn("name");
        expect(this.request.getSession()).andReturn(this.session);
        this.session.setAttribute("key", this.object);
        replay(this.request, this.session);
        this.binder.bind(this.model, this.request);
        assertSame(this.object, this.model.get("key"));
        verify(this.request, this.session);
    }

    @Test
    public void testBindValueInSession() {
        expect(this.request.getParameter("name")).andReturn(null);
        expect(this.request.getSession()).andReturn(this.session);
        expect(this.session.getAttribute("key")).andReturn(this.object);
        replay(this.request, this.session);
        this.binder.bind(this.model, this.request);
        assertSame(this.object, this.model.get("key"));
        verify(this.request, this.session);
    }
}
