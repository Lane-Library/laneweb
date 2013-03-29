package edu.stanford.irt.laneweb.cocoon;

import java.beans.PropertyEditorSupport;

import edu.stanford.irt.cocoon.sitemap.expression.ExpressionParser;


public class VariablePropertyEditor extends PropertyEditorSupport {
    
    private ExpressionParser parser = new ExpressionParser();
    
    public void setAsText(String text) {
        setValue(this.parser.parseExpression(text));
    }
}
