package edu.stanford.irt.laneweb.servlet.binding;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.mock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import edu.stanford.irt.laneweb.model.Model;

public class ParameterMapDataBinderTest {

    private ParameterMapDataBinder binder;

    private Map<String, Object> model;

    private HttpServletRequest request;

    @BeforeEach
    public void setUp() {
        this.binder = new ParameterMapDataBinder();
        this.request = mock(HttpServletRequest.class);
        this.model = new HashMap<>();
    }

    @Test
    public void testBindEmptyMap() {
        expect(this.request.getParameterMap()).andReturn(Collections.emptyMap());
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
