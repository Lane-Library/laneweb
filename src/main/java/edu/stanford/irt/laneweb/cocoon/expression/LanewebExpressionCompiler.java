package edu.stanford.irt.laneweb.cocoon.expression;

import org.apache.cocoon.el.Expression;
import org.apache.cocoon.el.ExpressionCompiler;

/**
 * @author ceyates $Id: LanewebExpressionCompiler.java 80185 2010-11-22
 *         22:01:29Z ceyates@stanford.edu $
 */
public class LanewebExpressionCompiler implements ExpressionCompiler {

    public Expression compile(final String language, final String expression) {
        return new LanewebExpression(expression);
    }
}
