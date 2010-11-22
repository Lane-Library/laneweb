package edu.stanford.irt.laneweb.cocoon.expression;

import org.apache.cocoon.el.Expression;
import org.apache.cocoon.el.ExpressionCompiler;

/**
 * @author ceyates $Id$
 */
public class LanewebExpressionCompiler implements ExpressionCompiler {

    public Expression compile(final String language, final String expression) {
        return new LanewebExpression(expression);
    }
}
