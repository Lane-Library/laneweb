package edu.stanford.irt.laneweb.cocoon.expression;

import java.util.Iterator;
import java.util.Map;

import org.apache.cocoon.el.Expression;
import org.apache.cocoon.el.ExpressionException;
import org.apache.cocoon.el.objectmodel.ObjectModel;

/**
 * @author ceyates $Id: LanewebExpression.java 80185 2010-11-22 22:01:29Z
 *         ceyates@stanford.edu $
 */
public class LanewebExpression implements Expression {

    private String expression;

    public LanewebExpression(final String expression) {
        this.expression = expression;
    }

    public void assign(final ObjectModel objectModel, final Object value) throws ExpressionException {
        throw new UnsupportedOperationException();
    }

    @SuppressWarnings("rawtypes")
    public Object evaluate(final ObjectModel objectModel) throws ExpressionException {
        Map model = (Map) objectModel.get("laneweb");
        return model.get(getExpression());
    }

    public String getExpression() {
        return this.expression;
    }

    public String getLanguage() {
        throw new UnsupportedOperationException();
    }

    public Object getNode(final ObjectModel objectModel) throws ExpressionException {
        throw new UnsupportedOperationException();
    }

    @SuppressWarnings("rawtypes")
    public Iterator iterate(final ObjectModel objectModel) throws ExpressionException {
        throw new UnsupportedOperationException();
    }

    public void setProperty(final String property, final Object value) {
        throw new UnsupportedOperationException();
    }
}
