package edu.stanford.irt.cocoon.sitemap.expression;

import java.util.Map;

public interface Expression {

    Object getValue(Map<String, Object> model);
}
