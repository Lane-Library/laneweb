package edu.stanford.irt.cocoon.sitemap.expression;

import java.util.Map;

public class VariableExpression implements Expression {

    private String value;

    public VariableExpression(final String value) {
        this.value = value;
    }

    public Object getValue(final Map<String, Object> model) {
        return model.get(this.value);
    }
}
