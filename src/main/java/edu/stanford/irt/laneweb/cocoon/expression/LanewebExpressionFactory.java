package edu.stanford.irt.laneweb.cocoon.expression;

import org.apache.cocoon.el.Expression;
import org.apache.cocoon.el.ExpressionCompiler;
import org.apache.cocoon.el.ExpressionException;
import org.apache.cocoon.el.ExpressionFactory;

/**
 * A simplified expression factory, only knows one language, ignores language
 * parameter.
 * 
 * @author ceyates $Id$
 */
public class LanewebExpressionFactory implements ExpressionFactory {

    private ExpressionCompiler compiler;

    public Expression getExpression(final String expression) throws ExpressionException {
        return this.compiler.compile(null, expression);
    }

    public Expression getExpression(final String language, final String expression) throws ExpressionException {
        return this.compiler.compile(language, expression);
    }

    public void setExpressionCompiler(final ExpressionCompiler compiler) {
        this.compiler = compiler;
    }
}
