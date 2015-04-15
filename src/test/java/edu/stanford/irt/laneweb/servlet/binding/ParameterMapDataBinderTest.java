package edu.stanford.irt.laneweb.servlet.binding;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.junit.Before;
import org.junit.Test;

import edu.stanford.irt.laneweb.model.Model;

public class ParameterMapDataBinderTest {

    private ParameterMapDataBinder binder;

    private Map<String, Object> model;

    private HttpServletRequest request;

    @Before
    public void setUp() {
        this.binder = new ParameterMapDataBinder();
        this.request = createMock(HttpServletRequest.class);
        this.model = new HashMap<String, Object>();
    }

    @Test
    public void testBindEmptyMap() {
        expect(this.request.getParameterMap()).andReturn(Collections.<String, String[]> emptyMap());
        replay(this.request);
        this.binder.bind(this.model, this.request);
        verify(this.request);
        assertFalse(this.model.containsKey(Model.PARAMETER_MAP));
    }

    @Test
    public void testBindNotEmptyMap() {
        expect(this.request.getParameterMap()).andReturn(Collections.singletonMap("foo", new String[] { "bar" }));
        replay(this.request);
        this.binder.bind(this.model, this.request);
        verify(this.request);
        assertTrue(this.model.containsKey(Model.PARAMETER_MAP));
    }
}
