package edu.stanford.irt.laneweb.cocoon;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import edu.stanford.irt.cocoon.sitemap.SitemapContext;
import edu.stanford.irt.cocoon.sitemap.SitemapNode;
import edu.stanford.irt.cocoon.sitemap.expression.Variable;
import edu.stanford.irt.cocoon.sitemap.node.AbstractNode;
import edu.stanford.irt.cocoon.sitemap.select.Selector;

public class SelectNode extends AbstractNode {

    private List<SitemapNode> otherwiseNodes;

    private Selector selector;

    private Map<Variable, List<SitemapNode>> whenNodes;

    public boolean invoke(final SitemapContext context) {
        Map<String, Object> model = context.getModel();
        Map<String, String> params = getResolvedParameters(context);
        boolean whenNodeSelected = false;
        for (Entry<Variable, List<SitemapNode>> entry : this.whenNodes.entrySet()) {
            if (this.selector.select(entry.getKey().resolve(context), model, params)) {
                whenNodeSelected = true;
                for (SitemapNode child : this.whenNodes.get(entry.getKey())) {
                    if (child.invoke(context)) {
                        return true;
                    }
                }
            }
        }
        if (!whenNodeSelected) {
            for (SitemapNode child : this.otherwiseNodes) {
                if (child.invoke(context)) {
                    return true;
                }
            }
        }
        return false;
    }

    public void setOtherwiseNodes(final List<SitemapNode> otherwiseNodes) {
        this.otherwiseNodes = otherwiseNodes;
    }

    public void setSelector(final Selector selector) {
        this.selector = selector;
    }

    public void setWhenNodes(final Map<Variable, List<SitemapNode>> whenNodes) {
        this.whenNodes = whenNodes;
    }
}
