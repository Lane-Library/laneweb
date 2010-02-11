package edu.stanford.irt.laneweb.model;

import org.apache.cocoon.el.Expression;
import org.apache.cocoon.el.ExpressionCompiler;
import org.apache.cocoon.el.ExpressionException;
import org.apache.cocoon.el.ExpressionFactory;

/**
 * A simplified expression factory, only knows one language, ignores language parameter.
 * 
 * @author ceyates
 *
 * $Id$
 */
public class LanewebExpressionFactory implements ExpressionFactory {
    
    private ExpressionCompiler compiler;
    
    public void setExpressionCompiler(ExpressionCompiler compiler) {
        this.compiler = compiler;
    }

    public Expression getExpression(String expression) throws ExpressionException {
        return this.compiler.compile(null, expression);
    }

    public Expression getExpression(String language, String expression) throws ExpressionException {
        return this.compiler.compile(language, expression);
    }
}
