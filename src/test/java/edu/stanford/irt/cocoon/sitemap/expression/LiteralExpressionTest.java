package edu.stanford.irt.cocoon.sitemap.expression;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import edu.stanford.irt.cocoon.sitemap.expression.LiteralExpression;

public class LiteralExpressionTest {

    private LiteralExpression expression;

    @Before
    public void setUp() throws Exception {
        this.expression = new LiteralExpression("foo");
    }

    @Test
    public void testGetValue() {
        assertEquals("foo", this.expression.getValue(null));
    }
}
