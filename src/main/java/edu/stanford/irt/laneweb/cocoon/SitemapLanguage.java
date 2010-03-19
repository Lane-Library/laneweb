package edu.stanford.irt.laneweb.cocoon;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import org.apache.avalon.framework.activity.Initializable;
import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.ConfigurationException;
import org.apache.avalon.framework.context.Context;
import org.apache.avalon.framework.context.ContextException;
import org.apache.avalon.framework.service.ServiceException;
import org.apache.avalon.framework.service.ServiceManager;
import org.apache.cocoon.components.LifecycleHelper;
import org.apache.cocoon.components.pipeline.impl.PipelineComponentInfo;
import org.apache.cocoon.components.treeprocessor.AbstractProcessingNode;
import org.apache.cocoon.components.treeprocessor.ParameterizableProcessingNode;
import org.apache.cocoon.components.treeprocessor.ProcessingNode;
import org.apache.cocoon.components.treeprocessor.ProcessingNodeBuilder;
import org.apache.cocoon.components.treeprocessor.variables.VariableResolver;
import org.apache.cocoon.components.treeprocessor.variables.VariableResolverFactory;
import org.apache.cocoon.sitemap.PatternException;
import org.apache.cocoon.util.location.Location;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.web.context.WebApplicationContext;

public class SitemapLanguage extends org.apache.cocoon.components.treeprocessor.sitemap.SitemapLanguage implements ApplicationContextAware {

    private ApplicationContext applicationContext;

    private LifecycleHelper itsLifecycle;

    private Map registeredNodes = Collections.emptyMap();

    private ServiceManager serviceManager;

    public SitemapLanguage(final Context context, final ServiceManager serviceManager, final PipelineComponentInfo info) throws ContextException,
            ServiceException {
        contextualize(context);
        service(serviceManager);
        this.serviceManager = serviceManager;
        this.itsNamespace = "http://apache.org/cocoon/sitemap/1.0";
        this.itsComponentInfo = info;
        this.itsLifecycle = new LifecycleHelper(null, context, serviceManager, null);
    }

    /**
     * Build a processing tree from a <code>Configuration</code>.
     */
    @Override
    public ProcessingNode build(final Configuration tree, final String location) throws Exception {
        return createTree(tree);
    }

    @Override
    public ProcessingNodeBuilder createNodeBuilder(final Configuration config) throws Exception {
        // FIXME : check namespace
        String nodeName = config.getName();
        if (getLogger().isDebugEnabled()) {
            getLogger().debug("Creating node builder for " + nodeName);
        }
        ProcessingNodeBuilder builder = (ProcessingNodeBuilder) this.applicationContext.getBean(nodeName);
        builder.setBuilder(this);
        return builder;
    }

    @Override
    public Map getHintsForStatement(final String role, final String hint, final Configuration statement) throws Exception {
        return Collections.emptyMap();
    }

    /**
     * @see org.apache.cocoon.components.treeprocessor.TreeBuilder#getRegisteredNode(java.lang.String)
     */
    @Override
    public ProcessingNode getRegisteredNode(final String name) {
        return (ProcessingNode) this.registeredNodes.get(name);
    }

    @Override
    public String getTypeForStatement(final Configuration statement, final String role) throws ConfigurationException {
        // Get the component type for the statement
        String type = statement.getAttribute("type", null);
        if (type == null) {
            type = this.itsComponentInfo.getDefaultType(role);
        }
        if (type == null) {
            throw new ConfigurationException("No default type exists for 'map:" + statement.getName() + "' at " + statement.getLocation());
        }
        final String beanName = role + '/' + type;
        if (!this.applicationContext.containsBean(beanName)) {
            throw new ConfigurationException("Type '" + type + "' does not exist for 'map:" + statement.getName() + "' at " + statement.getLocation());
        }
        return type;
    }

    @Override
    public Collection getViewsForStatement(final String role, final String hint, final Configuration statement) throws Exception {
        return Collections.emptyList();
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
        if (this.registeredNodes.containsKey(name)) {
            return false;
        }
        this.registeredNodes.put(name, node);
        return true;
    }

    public void setApplicationContext(final ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public ProcessingNode setupNode(final ProcessingNode node, final Configuration config) throws Exception {
        Location location = getLocation(config);
        if (node instanceof AbstractProcessingNode) {
            ((AbstractProcessingNode) node).setLocation(location);
            ((AbstractProcessingNode) node).setSitemapExecutor(this.processor.getSitemapExecutor());
        }
        this.itsLifecycle.setupComponent(node, false);
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
