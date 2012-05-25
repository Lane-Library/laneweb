package edu.stanford.irt.cocoon.sitemap.expression;

import static org.junit.Assert.assertEquals;

import java.util.Collections;

import org.junit.Before;
import org.junit.Test;

import edu.stanford.irt.cocoon.sitemap.expression.VariableExpression;

public class VariableExpressionTest {

    private VariableExpression expression;

    @Before
    public void setUp() throws Exception {
        this.expression = new VariableExpression("foo");
    }

    @Test
    public void testGetValue() {
        assertEquals("bar", this.expression.getValue(Collections.<String, Object> singletonMap("foo", "bar")));
    }
}
