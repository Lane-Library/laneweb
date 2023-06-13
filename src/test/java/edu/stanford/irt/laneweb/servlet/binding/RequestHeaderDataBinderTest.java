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

public class RequestHeaderDataBinderTest {

    private RequestHeaderDataBinder binder;

    private Map<String, Object> model;

    private HttpServletRequest request;

    @Before
    public void setUp() throws Exception {
        this.binder = new RequestHeaderDataBinder();
        this.model = new HashMap<>();
        this.request = mock(HttpServletRequest.class);
    }

    @Test
    public void testBind() {
        expect(this.request.getHeader("referer")).andReturn("referer");
        expect(this.request.getHeader("user-agent")).andReturn("user-agent");
        replay(this.request);
        this.binder.bind(this.model, this.request);
        assertEquals("referer", this.model.get(Model.REFERRER));
        assertEquals("user-agent", this.model.get(Model.USER_AGENT));
        verify(this.request);
    }

    @Test
    public void testBindNullValues() {
        expect(this.request.getHeader("referer")).andReturn(null);
        expect(this.request.getHeader("user-agent")).andReturn(null);
        replay(this.request);
        this.binder.bind(this.model, this.request);
        assertNull(this.model.get(Model.REFERRER));
        assertNull(this.model.get(Model.USER_AGENT));
        verify(this.request);
    }
}
