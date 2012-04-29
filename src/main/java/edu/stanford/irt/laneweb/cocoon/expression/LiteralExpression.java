package edu.stanford.irt.laneweb.cocoon.expression;

import java.util.Map;

public class LiteralExpression implements Expression {

    private String value;

    public LiteralExpression(final String value) {
        this.value = value;
    }

    public Object getValue(final Map<String, Object> model) {
        return this.value;
    }
}
