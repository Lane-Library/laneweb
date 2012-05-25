package edu.stanford.irt.cocoon.sitemap.expression;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.cocoon.components.treeprocessor.InvokeContext;
import org.apache.cocoon.components.treeprocessor.variables.VariableResolver;

import edu.stanford.irt.laneweb.LanewebException;

public class VariableImpl extends VariableResolver {

    private List<Expression> expressions;

    private ExpressionParser parser;

    public VariableImpl(final ExpressionParser parser) {
        this.parser = parser;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public String resolve(final InvokeContext context, final Map objectModel) {
        Map<String, Object> compositeMap = new HashMap<String, Object>(objectModel);
        String entryPrefix = "";
        List<Map<String, String>> list = context.getMapStack();
        for (int i = list.size() - 1; i >= 0; i--) {
            Map<String, String> map = list.get(i);
            for (Entry<String, String> entry : map.entrySet()) {
                compositeMap.put(entryPrefix + entry.getKey(), entry.getValue());
            }
            entryPrefix = "../" + entryPrefix;
        }
        StringBuilder builder = new StringBuilder();
        for (Expression sub : this.expressions) {
            builder.append(sub.getValue(compositeMap));
        }
        return builder.toString();
    }

    @Override
    public void setExpression(final String expression) {
        try {
            this.expressions = this.parser.parseExpression(expression);
        } catch (IOException e) {
            throw new LanewebException(e);
        }
    }
}
