package edu.stanford.irt.laneweb.cocoon.expression;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class ExpressionParserTest {

    private Map<String, Object> model;

    private ExpressionParser parser;

    @Before
    public void setUp() throws Exception {
        this.parser = new ExpressionParser();
        this.model = new HashMap<String, Object>();
    }

    @Test
    public void testParseComposite() throws IOException {
        this.model.put("foo", "baz");
        this.model.put("baz", "foo");
        List<Expression> expressions = this.parser.parseExpression("{foo}bar{baz}");
        assertEquals("baz", expressions.get(0).getValue(this.model));
        assertEquals("bar", expressions.get(1).getValue(this.model));
        assertEquals("foo", expressions.get(2).getValue(this.model));
    }

    @Test
    public void testParseExpression() throws IOException {
        this.model.put("foo", "bar");
        List<Expression> expressions = this.parser.parseExpression("{foo}");
        assertEquals("bar", expressions.get(0).getValue(this.model));
    }

    @Test
    public void testParseLiteral() throws IOException {
        List<Expression> expressions = this.parser.parseExpression("foo");
        assertEquals("foo", expressions.get(0).getValue(this.model));
    }
}
