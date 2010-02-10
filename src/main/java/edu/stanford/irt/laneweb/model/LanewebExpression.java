package edu.stanford.irt.laneweb.model;

import java.util.Iterator;
import java.util.Map;

import org.apache.cocoon.el.ExpressionException;
import org.apache.cocoon.el.impl.AbstractExpression;
import org.apache.cocoon.el.objectmodel.ObjectModel;

/**
 * 
 * @author ceyates
 * $Id$
 */
public class LanewebExpression extends AbstractExpression {

    public LanewebExpression(String language, String expression) {
        super(language, expression);
    }

    public void assign(ObjectModel objectModel, Object value) throws ExpressionException {
        throw new UnsupportedOperationException();
    }

    @SuppressWarnings("unchecked")
    public Object evaluate(ObjectModel objectModel) throws ExpressionException {
        Map model = (Map) objectModel.get("contextBean");
        return model.get(getExpression());
    }

    public Object getNode(ObjectModel objectModel) throws ExpressionException {
        throw new UnsupportedOperationException();
    }

    @SuppressWarnings("unchecked")
    public Iterator iterate(ObjectModel objectModel) throws ExpressionException {
        return EMPTY_ITER;
    }
}
