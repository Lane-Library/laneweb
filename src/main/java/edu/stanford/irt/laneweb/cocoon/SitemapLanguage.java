package edu.stanford.irt.laneweb.cocoon;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import org.apache.avalon.framework.activity.Initializable;
import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.ConfigurationException;
import org.apache.avalon.framework.service.ServiceException;
import org.apache.avalon.framework.service.ServiceManager;
import org.apache.avalon.framework.service.Serviceable;
import org.apache.cocoon.components.pipeline.impl.PipelineComponentInfo;
import org.apache.cocoon.components.treeprocessor.AbstractProcessingNode;
import org.apache.cocoon.components.treeprocessor.ParameterizableProcessingNode;
import org.apache.cocoon.components.treeprocessor.ProcessingNode;
import org.apache.cocoon.components.treeprocessor.ProcessingNodeBuilder;
import org.apache.cocoon.components.treeprocessor.variables.VariableResolver;
import org.apache.cocoon.components.treeprocessor.variables.VariableResolverFactory;
import org.apache.cocoon.sitemap.PatternException;
import org.apache.cocoon.util.location.Location;
import org.apache.cocoon.util.location.LocationImpl;
import org.apache.cocoon.util.location.LocationUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.web.context.WebApplicationContext;

import edu.stanford.irt.laneweb.LanewebException;

public class SitemapLanguage extends org.apache.cocoon.components.treeprocessor.sitemap.SitemapLanguage implements
        ApplicationContextAware {

    // cut and pasted from AvalonNamespaceHandler which is no longer loaded
    private static final LocationUtils.LocationFinder confLocFinder = new LocationUtils.LocationFinder() {

        public Location getLocation(final Object obj, final String description) {
            if (obj instanceof Configuration) {
                Configuration config = (Configuration) obj;
                String locString = config.getLocation();
                Location result = LocationUtils.parse(locString);
                if (LocationUtils.isKnown(result)) {
                    // Add description
                    StringBuffer desc = new StringBuffer().append('<');
                    // Unfortunately Configuration.getPrefix() is not public
                    try {
                        if (config.getNamespace().startsWith("http://apache.org/cocoon/sitemap/")) {
                            desc.append("map:");
                        }
                    } catch (ConfigurationException e) {
                        throw new LanewebException(e);
                    }
                    desc.append(config.getName()).append('>');
                    return new LocationImpl(desc.toString(), result);
                } else {
                    return result;
                }
            }
            // Try next finders.
            return null;
        }
    };
    static {
        LocationUtils.addFinder(confLocFinder);
    }

    private ApplicationContext applicationContext;

    private ServiceManager serviceManager;

    public SitemapLanguage(final ServiceManager serviceManager, final PipelineComponentInfo info) throws ServiceException {
        service(serviceManager);
        this.serviceManager = serviceManager;
        this.itsNamespace = "http://apache.org/cocoon/sitemap/1.0";
        this.itsComponentInfo = info;
    }

    /**
     * Build a processing tree from a <code>Configuration</code>.
     */
    @Override
    public ProcessingNode build(final Configuration tree, final String location) throws Exception {
        return createTree(tree);
    }

    @Override
    public ProcessingNodeBuilder createNodeBuilder(final Configuration config) {
        String nodeName = config.getName();
        ProcessingNodeBuilder builder = (ProcessingNodeBuilder) this.applicationContext.getBean(nodeName);
        builder.setBuilder(this);
        return builder;
    }

    @SuppressWarnings("rawtypes")
    @Override
    public Map getHintsForStatement(final String role, final String hint, final Configuration statement) {
        return Collections.emptyMap();
    }

    /**
     * @see org.apache.cocoon.components.treeprocessor.TreeBuilder#getRegisteredNode(java.lang.String)
     */
    @Override
    public ProcessingNode getRegisteredNode(final String name) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getTypeForStatement(final Configuration configuration, final String role) {
        return configuration.getAttribute("type", null);
    }

    @SuppressWarnings("rawtypes")
    @Override
    public Collection getViewsForStatement(final String role, final String hint, final Configuration statement) {
        return Collections.emptySet();
    }

    @Override
    public WebApplicationContext getWebApplicationContext() {
        return (WebApplicationContext) this.applicationContext;
    }

    /**
     * @see org.apache.cocoon.components.treeprocessor.TreeBuilder#registerNode(java.lang.String,
     *      org.apache.cocoon.components.treeprocessor.ProcessingNode)
     */
    @Override
    public boolean registerNode(final String name, final ProcessingNode node) {
        throw new UnsupportedOperationException();
    }

    public void setApplicationContext(final ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @SuppressWarnings("rawtypes")
    @Override
    public ProcessingNode setupNode(final ProcessingNode node, final Configuration config) throws Exception {
        Location location = getLocation(config);
        if (node instanceof AbstractProcessingNode) {
            ((AbstractProcessingNode) node).setLocation(location);
            ((AbstractProcessingNode) node).setSitemapExecutor(this.processor.getSitemapExecutor());
        }
        if (node instanceof Serviceable) {
            ((Serviceable) node).service(this.serviceManager);
        }
        if (node instanceof ParameterizableProcessingNode) {
            Map params = getParameters(config, location);
            ((ParameterizableProcessingNode) node).setParameters(params);
        }
        if (node instanceof Initializable) {
            ((Initializable) node).initialize();
        }
        return node;
    }

    @Override
    protected VariableResolver resolve(final String expression) throws PatternException {
        return VariableResolverFactory.getResolver(expression, this.serviceManager);
    }
}
