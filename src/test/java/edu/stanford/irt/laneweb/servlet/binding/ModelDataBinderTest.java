package edu.stanford.irt.laneweb.servlet.binding;

import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.mock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;

import edu.stanford.irt.laneweb.LanewebException;

public class ModelDataBinderTest {

    private ModelDataBinder binder;

    private JsonFactory factory;

    private JsonGenerator generator;

    private ObjectMapper mapper;

    private Map<String, Object> model;

    private HttpServletRequest request;

    @BeforeEach
    public void setUp() {
        this.mapper = mock(ObjectMapper.class);
        this.binder = new ModelDataBinder(Collections.singleton("foo"), this.mapper);
        this.model = new HashMap<>();
        this.request = mock(HttpServletRequest.class);
        this.factory = mock(JsonFactory.class);
        this.generator = mock(JsonGenerator.class);
    }

    @Test
    public void testBind() throws IOException {
        this.model.put("foo", "bar");
        expect(this.mapper.getFactory()).andReturn(this.factory);
        expect(this.factory.createGenerator(isA(StringWriter.class))).andReturn(this.generator);
        this.mapper.writeValue(eq(this.generator), eq(Collections.singletonMap("foo", "bar")));
        replay(this.request, this.mapper, this.factory, this.generator);
        this.binder.bind(this.model, this.request);
        verify(this.request, this.mapper, this.factory, this.generator);
    }

    @Test
    public void testBindEmpty() throws IOException {
        this.model.put("bar", "baz");
        expect(this.mapper.getFactory()).andReturn(this.factory);
        expect(this.factory.createGenerator(isA(StringWriter.class))).andReturn(this.generator);
        this.mapper.writeValue(eq(this.generator), eq(Collections.emptyMap()));
        replay(this.request, this.mapper, this.factory, this.generator);
        this.binder.bind(this.model, this.request);
        verify(this.request, this.mapper, this.factory, this.generator);
    }

    @Test
    public void testBindThrowsException() throws IOException {
        expect(this.mapper.getFactory()).andReturn(this.factory);
        expect(this.factory.createGenerator(isA(StringWriter.class))).andThrow(new IOException());
        replay(this.request, this.mapper, this.factory, this.generator);
        assertThrows(LanewebException.class, () -> {
            this.binder.bind(this.model, this.request);
        });
    }
}
