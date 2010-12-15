package edu.stanford.irt.laneweb.cocoon.expression;

import org.apache.cocoon.el.ExpressionCompiler;
import org.apache.cocoon.el.ExpressionException;
import org.junit.Before;
import org.junit.Test;

public class LanewebExpressionCompilerTest {

    private ExpressionCompiler compiler;

    @Before
    public void setUp() throws Exception {
        this.compiler = new LanewebExpressionCompiler();
    }

    @Test
    public void testCompile() throws ExpressionException {
        this.compiler.compile("default", "foo");
    }
}
