package edu.stanford.irt.laneweb.cocoon.expression;

import java.util.Map;

public interface Expression {

    Object getValue(Map<String, Object> model);
}
