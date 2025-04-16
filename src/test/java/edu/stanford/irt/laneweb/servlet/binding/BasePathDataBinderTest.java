package edu.stanford.irt.laneweb.servlet.binding;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.mock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashMap;
import java.util.Map;

import jakarta.servlet.ServletContext;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import edu.stanford.irt.laneweb.model.Model;

public class BasePathDataBinderTest {

    private BasePathDataBinder dataBinder;

    private ServletContext servletContext;

    @BeforeEach
    public void setUp() {
        this.servletContext = mock(ServletContext.class);
        expect(this.servletContext.getContextPath()).andReturn("/base-path");
        replay(this.servletContext);
        this.dataBinder = new BasePathDataBinder(this.servletContext);
        verify(this.servletContext);
    }

    @Test
    public void testBind() {
        Map<String, Object> model = new HashMap<>();
        this.dataBinder.bind(model, null);
        assertEquals("/base-path", model.get(Model.BASE_PATH));
    }
}
