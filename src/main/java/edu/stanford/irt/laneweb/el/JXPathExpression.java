package edu.stanford.irt.laneweb.el;

import org.apache.cocoon.el.ExpressionException;
import org.apache.cocoon.el.objectmodel.ObjectModel;
import org.apache.commons.jxpath.JXPathException;
import org.apache.log4j.Logger;


public class JXPathExpression extends org.apache.cocoon.el.impl.jxpath.JXPathExpression {
    
    private static final Logger LOGGER = Logger.getLogger(JXPathExpression.class);

    @Override
    public Object evaluate(ObjectModel objectModel) throws ExpressionException {
        try {
            return super.evaluate(objectModel);
        } catch (JXPathException e) {
            LOGGER.error(e.getMessage(), e);
            return "";
        }
    }

    public JXPathExpression(String language, String expression) throws ExpressionException {
        super(language, expression);
    }
}
