package edu.stanford.irt.laneweb.model;

import org.apache.cocoon.el.Expression;
import org.apache.cocoon.el.ExpressionCompiler;


/**
 * 
 * @author ceyates
 * $Id$
 */
public class LanewebExpressionCompiler implements ExpressionCompiler {

    public Expression compile(String language, String expression) {
        return new LanewebExpression(expression);
    }
}
