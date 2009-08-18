package edu.stanford.irt.laneweb.el;

import org.apache.cocoon.el.Expression;
import org.apache.cocoon.el.ExpressionCompiler;
import org.apache.cocoon.el.ExpressionException;


public class JXPathCompiler implements ExpressionCompiler {

    public Expression compile(String language, String expression) throws ExpressionException {
        return new JXPathExpression(language, expression);
    }
}
