package edu.stanford.irt.laneweb.servlet.binding;

import static org.easymock.EasyMock.mock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CompositeDataBinderTest {

    private CompositeDataBinder binder;

    private DataBinder child;

    private Map<String, Object> model;

    private HttpServletRequest request;

    @BeforeEach
    public void setUp() {
        this.child = mock(DataBinder.class);
        this.binder = new CompositeDataBinder(Collections.singletonList(this.child));
        this.request = mock(HttpServletRequest.class);
        this.model = new HashMap<>();
    }

    @Test
    public void testBind() {
        this.child.bind(this.model, this.request);
        replay(this.request, this.child);
        this.binder.bind(this.model, this.request);
        verify(this.request, this.child);
    }
}
