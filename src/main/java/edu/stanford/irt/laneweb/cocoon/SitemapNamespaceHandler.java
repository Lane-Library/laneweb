package edu.stanford.irt.laneweb.cocoon;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.config.ConstructorArgumentValues;
import org.springframework.beans.factory.config.ConstructorArgumentValues.ValueHolder;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.parsing.BeanComponentDefinition;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.ManagedList;
import org.springframework.beans.factory.support.ManagedMap;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.springframework.beans.factory.xml.NamespaceHandlerSupport;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.util.xml.DomUtils;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import edu.stanford.irt.cocoon.sitemap.node.AggregateNode;
import edu.stanford.irt.cocoon.sitemap.node.GenerateNode;
import edu.stanford.irt.cocoon.sitemap.node.MatchNode;
import edu.stanford.irt.cocoon.sitemap.node.PipelineNode;
import edu.stanford.irt.cocoon.sitemap.node.RootNode;
import edu.stanford.irt.cocoon.sitemap.node.SelectNode;
import edu.stanford.irt.cocoon.sitemap.node.SerializeNode;
import edu.stanford.irt.cocoon.sitemap.node.TransformNode;

public class SitemapNamespaceHandler extends NamespaceHandlerSupport {

    private abstract static class AbstractSitemapBeanDefinitionParser extends AbstractSingleBeanDefinitionParser {

        protected static final String TYPE = "type";

        protected void addAttributes(final MutablePropertyValues values, final Element element,
                final Map<String, String> attributeMap) {
            for (Entry<String, String> entry : attributeMap.entrySet()) {
                values.add(entry.getKey(), element.getAttribute(entry.getValue()));
            }
        }

        protected void addChildren(final MutablePropertyValues values, final ParserContext parserContext,
                final AbstractBeanDefinition def, final Element element, final String... childNames) {
            List<Element> children = DomUtils.getChildElementsByTagName(element, childNames);
            ManagedList<Object> target = new ManagedList<Object>(children.size());
            target.setSource(parserContext.getReaderContext().extractSource(element));
            for (Element child : children) {
                target.add(parserContext.getDelegate().parseCustomElement(child, def));
            }
            values.add("children", target);
        }

        protected void addParameters(final MutablePropertyValues values, final Element element) {
            List<Element> params = DomUtils.getChildElementsByTagName(element, "parameter");
            ManagedMap<Object, Object> map = new ManagedMap<Object, Object>(params.size());
            for (Element param : params) {
                map.put(param.getAttribute("name"), param.getAttribute("value"));
            }
            values.add("parameters", map);
        }

        @Override
        protected void doParse(final Element element, final ParserContext parserContext,
                final BeanDefinitionBuilder builder) {
            AbstractBeanDefinition def = builder.getBeanDefinition();
            def.setBeanClass(getBeanClass());
            def.setPropertyValues(getPropertyValues(element, parserContext, def));
        }

        protected abstract Map<String, String> getAttributeMap();

        protected abstract Class<?> getBeanClass();

        protected abstract String[] getChildNames();

        protected MutablePropertyValues getPropertyValues(final Element element, final ParserContext parserContext,
                final AbstractBeanDefinition def) {
            MutablePropertyValues values = new MutablePropertyValues();
            addAttributes(values, element, getAttributeMap());
            addParameters(values, element);
            addChildren(values, parserContext, def, element, getChildNames());
            return values;
        }
    }

    private abstract static class AbstractSitemapComponentBeanDefinitionParser extends
            AbstractSitemapBeanDefinitionParser {

        private static final Map<String, String> ATTRIBUTE_MAP = new HashMap<String, String>();

        private static final String[] CHILD_ELEMENTS = new String[] {};
        static {
            ATTRIBUTE_MAP.put(TYPE, TYPE);
            ATTRIBUTE_MAP.put("source", "src");
        }

        @Override
        protected void addChildren(final MutablePropertyValues values, final ParserContext parserContext,
                final AbstractBeanDefinition def, final Element element, final String... childNames) {
        }

        @Override
        protected Map<String, String> getAttributeMap() {
            return ATTRIBUTE_MAP;
        }

        @Override
        protected String[] getChildNames() {
            return CHILD_ELEMENTS;
        }
    }

    private static class AggregateBeanDefinitionParser extends AbstractSitemapComponentBeanDefinitionParser {

        private static final Map<String, String> ATTRIBUTE_MAP = new HashMap<String, String>();

        private static final String[] CHILD_ELEMENTS = new String[] { "generate" };
        static {
            ATTRIBUTE_MAP.put("element", "element");
            ATTRIBUTE_MAP.put("namespace", "ns");
            ATTRIBUTE_MAP.put("prefix", "prefix");
        }

        @Override
        protected void addChildren(final MutablePropertyValues values, final ParserContext parserContext,
                final AbstractBeanDefinition def, final Element element, final String... childNames) {
            List<Element> children = DomUtils.getChildElementsByTagName(element, childNames);
            ManagedList<Object> target = new ManagedList<Object>(children.size());
            target.setSource(parserContext.getReaderContext().extractSource(element));
            for (Element child : children) {
                target.add(parserContext.getDelegate().parseCustomElement(child, def));
            }
            values.add("children", target);
            values.add(TYPE, "<aggregator>");
        }

        @Override
        protected Map<String, String> getAttributeMap() {
            return ATTRIBUTE_MAP;
        }

        @Override
        protected Class<?> getBeanClass() {
            return AggregateNode.class;
        }

        @Override
        protected String[] getChildNames() {
            return CHILD_ELEMENTS;
        }
    }

    private static class GenerateBeanDefinitionParser extends AbstractSitemapComponentBeanDefinitionParser {

        @Override
        protected Class<?> getBeanClass() {
            return GenerateNode.class;
        }
    }

    private static class MatchBeanDefinitionParser extends AbstractSitemapBeanDefinitionParser {

        private static final Map<String, String> ATTRIBUTE_MAP = Collections.singletonMap("pattern", "pattern");

        private static final String[] CHILD_ELEMENTS = new String[] { "aggregate", "generate", "match", "select",
                "serialize", "transform" };

        @Override
        protected void doParse(final Element element, final ParserContext parserContext,
                final BeanDefinitionBuilder builder) {
            super.doParse(element, parserContext, builder);
            builder.getBeanDefinition()
                    .getPropertyValues()
                    .add("matcher",
                            new RuntimeBeanReference("edu.stanford.irt.cocoon.sitemap.match.Matcher/"
                                    + element.getAttribute(TYPE)));
        }

        @Override
        protected Map<String, String> getAttributeMap() {
            return ATTRIBUTE_MAP;
        }

        @Override
        protected Class<?> getBeanClass() {
            return MatchNode.class;
        }

        @Override
        protected String[] getChildNames() {
            return CHILD_ELEMENTS;
        }
    }

    private static class PipelineBeanDefinitionParser extends AbstractSitemapBeanDefinitionParser {

        private static final Map<String, String> ATTRIBUTE_MAP = Collections.singletonMap(TYPE, TYPE);

        private static final String[] CHILD_ELEMENTS = new String[] { "aggregate", "generate", "match", "select",
                "serialize", "transform" };

        @Override
        protected Map<String, String> getAttributeMap() {
            return ATTRIBUTE_MAP;
        }

        @Override
        protected Class<?> getBeanClass() {
            return PipelineNode.class;
        }

        @Override
        protected String[] getChildNames() {
            return CHILD_ELEMENTS;
        }
    }

    private static class SelectBeanDefinitionParser extends AbstractSingleBeanDefinitionParser {

        @Override
        protected void doParse(final Element element, final ParserContext parserContext,
                final BeanDefinitionBuilder builder) {
            AbstractBeanDefinition def = builder.getBeanDefinition();
            def.setBeanClass(SelectNode.class);
            List<Element> whens = DomUtils.getChildElementsByTagName(element, "when");
            ManagedMap<Object, Object> map = new ManagedMap<Object, Object>(whens.size());
            map.setSource(parserContext.getReaderContext().extractSource(element));
            for (Element when : whens) {
                NodeList nl = when.getChildNodes();
                ManagedList<Object> target = new ManagedList<Object>(nl.getLength());
                target.setSource(parserContext.getReaderContext().extractSource(element));
                for (int i = 0; i < nl.getLength(); i++) {
                    Node node = nl.item(i);
                    if (node instanceof Element && !"parameter".equals(node.getLocalName())) {
                        target.add(parserContext.getDelegate().parseCustomElement((Element) node, def));
                    }
                }
                map.put(when.getAttribute("test"), target);
            }
            List<Element> otherwises = DomUtils.getChildElementsByTagName(element, "otherwise");
            ManagedList<Object> target = new ManagedList<Object>();
            if (otherwises.size() == 1) {
                Element otherwise = otherwises.get(0);
                NodeList nl = otherwise.getChildNodes();
                target.setSource(parserContext.getReaderContext().extractSource(otherwise));
                for (int i = 0; i < nl.getLength(); i++) {
                    Node node = nl.item(i);
                    if (node instanceof Element && !"parameter".equals(node.getLocalName())) {
                        target.add(parserContext.getDelegate().parseCustomElement((Element) node, def));
                    }
                }
            }
            MutablePropertyValues values = new MutablePropertyValues();
            values.add("selector", new RuntimeBeanReference("edu.stanford.irt.cocoon.sitemap.select.Selector/"
                    + element.getAttribute("type")));
            values.add("whenNodes", map);
            values.add("otherwiseNodes", target);
            def.setPropertyValues(values);
        }
    }

    private static class SerializeBeanDefinitionParser extends AbstractSitemapComponentBeanDefinitionParser {

        @Override
        protected Class<?> getBeanClass() {
            return SerializeNode.class;
        }
    }

    private static class SitemapBeanDefinitionParser extends AbstractSitemapBeanDefinitionParser {

        private static final Map<String, String> ATTRIBUTE_MAP = Collections.emptyMap();

        private static final String[] CHILD_ELEMENTS = new String[] { "pipeline" };

        @Override
        protected void doParse(final Element element, final ParserContext parserContext,
                final BeanDefinitionBuilder builder) {
            super.doParse(element, parserContext, builder);
            builder.getBeanDefinition().setConstructorArgumentValues(getConstructorArgumentValues());
        }

        private ConstructorArgumentValues getConstructorArgumentValues() {
            ConstructorArgumentValues cav = new ConstructorArgumentValues();
            ValueHolder fact = new ValueHolder(new RuntimeBeanReference(
                    "edu.stanford.irt.cocoon.sitemap.ComponentFactory"));
            ValueHolder resolver = new ValueHolder(
                    new RuntimeBeanReference("edu.stanford.irt.cocoon.source.SourceResolver"));
            cav.addGenericArgumentValue(resolver);
            cav.addGenericArgumentValue(fact);
            return cav;
        }

        @Override
        protected Map<String, String> getAttributeMap() {
            return ATTRIBUTE_MAP;
        }

        @Override
        protected Class<?> getBeanClass() {
            return RootNode.class;
        }

        @Override
        protected String[] getChildNames() {
            return CHILD_ELEMENTS;
        }

        @Override
        protected void postProcessComponentDefinition(final BeanComponentDefinition componentDefinition) {
            MutablePropertyValues mpv = componentDefinition.getBeanDefinition().getPropertyValues();
            mpv.removePropertyValue("schemaLocation");
        }

        @Override
        protected String resolveId(final Element element, final AbstractBeanDefinition definition,
                final ParserContext parserContext) {
            String filename = parserContext.getReaderContext().getResource().getFilename();
            return "edu.stanford.irt.cocoon.sitemap.Sitemap/" + filename.substring(0, filename.indexOf('.'));
        }
    }

    private static class TransformBeanDefinitionParser extends AbstractSitemapComponentBeanDefinitionParser {

        @Override
        protected Class<?> getBeanClass() {
            return TransformNode.class;
        }
    }

    @Override
    public void init() {
        registerBeanDefinitionParser("sitemap", new SitemapBeanDefinitionParser());
        registerBeanDefinitionParser("pipeline", new PipelineBeanDefinitionParser());
        registerBeanDefinitionParser("match", new MatchBeanDefinitionParser());
        registerBeanDefinitionParser("select", new SelectBeanDefinitionParser());
        registerBeanDefinitionParser("generate", new GenerateBeanDefinitionParser());
        registerBeanDefinitionParser("transform", new TransformBeanDefinitionParser());
        registerBeanDefinitionParser("serialize", new SerializeBeanDefinitionParser());
        registerBeanDefinitionParser("aggregate", new AggregateBeanDefinitionParser());
    }
}
