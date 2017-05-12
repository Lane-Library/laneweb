package edu.stanford.irt.laneweb.config;

import java.io.Serializable;
import java.util.Properties;

import javax.cache.Cache;
import javax.xml.transform.sax.SAXTransformerFactory;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.oxm.Marshaller;

import edu.stanford.irt.cocoon.cache.CachedResponse;
import edu.stanford.irt.cocoon.pipeline.Aggregator;
import edu.stanford.irt.cocoon.pipeline.CachingPipeline;
import edu.stanford.irt.cocoon.pipeline.ExpiresCachingPipeline;
import edu.stanford.irt.cocoon.pipeline.Generator;
import edu.stanford.irt.cocoon.pipeline.NonCachingPipeline;
import edu.stanford.irt.cocoon.pipeline.Pipeline;
import edu.stanford.irt.cocoon.pipeline.Serializer;
import edu.stanford.irt.cocoon.pipeline.ThrottlingPipeline;
import edu.stanford.irt.cocoon.pipeline.Transformer;
import edu.stanford.irt.cocoon.pipeline.generate.AggregatorImpl;
import edu.stanford.irt.cocoon.pipeline.generate.URLGenerator;
import edu.stanford.irt.cocoon.pipeline.serialize.TransformerSerializer;
import edu.stanford.irt.cocoon.pipeline.transform.NamespaceFilter;
import edu.stanford.irt.cocoon.pipeline.transform.TraxTransformer;
import edu.stanford.irt.cocoon.pipeline.transform.XIncludeTransformer;
import edu.stanford.irt.cocoon.source.SourceResolver;
import edu.stanford.irt.cocoon.xml.SAXParser;
import edu.stanford.irt.cocoon.xml.TransformerHandlerFactory;
import edu.stanford.irt.cocoon.xml.XIncludePipe;
import edu.stanford.irt.laneweb.classes.EventListTransformer;
import edu.stanford.irt.laneweb.classes.RootElementProvidingGenerator;
import edu.stanford.irt.laneweb.cocoon.DebugTransformer;
import edu.stanford.irt.laneweb.cocoon.HTML5Serializer;
import edu.stanford.irt.laneweb.cocoon.TextNodeParsingTransformer;
import edu.stanford.irt.laneweb.search.ParameterMapGenerator;
import edu.stanford.irt.laneweb.seminars.SeminarsGenerator;

@Configuration
public class PipelineConfiguration {

    private static final String ENCODING = "encoding";

    private static final String INDENT = "indent";

    private static final String METHOD = "method";

    private static final String NO = "no";

    private static final String OMIT_XML_DECLARATION = "omit-xml-declaration";

    private static final String UTF_8 = "UTF-8";

    private static final String XHTML = "xhtml";

    private static final String YES = "yes";

    @Bean(name = "edu.stanford.irt.cocoon.pipeline.Aggregator/<aggregator>")
    @Scope("prototype")
    public Aggregator aggregator() {
        return new AggregatorImpl("<aggregator>");
    }

    @Bean(name = "edu.stanford.irt.cocoon.pipeline.Pipeline/caching")
    @Scope("prototype")
    public Pipeline cachingPipeline(final Cache<Serializable, CachedResponse> cache) {
        return new CachingPipeline(cache);
    }

    @Bean(name = "edu.stanford.irt.cocoon.pipeline.Transformer/debug")
    @Scope("prototype")
    public Transformer debugTransformer() {
        return new DebugTransformer();
    }

    @Bean(name = "edu.stanford.irt.cocoon.pipeline.Transformer/eventlist")
    @Scope("prototype")
    public Transformer eventListTransformer(final SourceResolver sourceResolver,
            @Qualifier("edu.stanford.irt.cocoon.xml.SAXParser/xml") final SAXParser xmlSAXParser) {
        return new EventListTransformer(sourceResolver, xmlSAXParser);
    }

    @Bean(name = "edu.stanford.irt.cocoon.pipeline.Pipeline/expires")
    @Scope("prototype")
    public Pipeline expiresCachingPipeline(final Cache<Serializable, CachedResponse> cache) {
        return new ExpiresCachingPipeline(cache, 300);
    }

    @Bean(name = "edu.stanford.irt.cocoon.pipeline.Generator/file")
    @Scope("prototype")
    public Generator fileGenerator(
            @Qualifier("edu.stanford.irt.cocoon.xml.SAXParser/xml") final SAXParser xmlSAXParser) {
        return new URLGenerator("file", xmlSAXParser);
    }

    @Bean(name = "edu.stanford.irt.cocoon.pipeline.Serializer/html5-indent")
    @Scope("prototype")
    public Serializer html5IndentSerializer(
            @Qualifier("javax.xml.transform.sax.SAXTransformerFactory/saxon") final SAXTransformerFactory saxonSAXTranformerFactory) {
        Properties props = new Properties();
        props.setProperty(METHOD, XHTML);
        props.setProperty(ENCODING, UTF_8);
        props.setProperty(INDENT, YES);
        props.setProperty(OMIT_XML_DECLARATION, YES);
        return new HTML5Serializer("html5-indent", saxonSAXTranformerFactory, props);
    }

    @Bean(name = "edu.stanford.irt.cocoon.pipeline.Serializer/html5")
    @Scope("prototype")
    public Serializer html5Serializer(
            @Qualifier("javax.xml.transform.sax.SAXTransformerFactory/saxon") final SAXTransformerFactory saxonSAXTranformerFactory) {
        Properties props = new Properties();
        props.setProperty(METHOD, XHTML);
        props.setProperty(ENCODING, UTF_8);
        props.setProperty(INDENT, NO);
        props.setProperty(OMIT_XML_DECLARATION, YES);
        return new HTML5Serializer("html5", saxonSAXTranformerFactory, props);
    }

    @Bean(name = "edu.stanford.irt.cocoon.pipeline.Generator/html")
    @Scope("prototype")
    public Generator htmlGenerator(final BeanFactory beanFactory) {
        return new URLGenerator("html",
                beanFactory.getBean("edu.stanford.irt.cocoon.xml.SAXParser/html", SAXParser.class));
    }

    @Bean(name = "edu.stanford.irt.cocoon.pipeline.Transformer/joost")
    @Scope("prototype")
    public Transformer joostTransformer(
            @Qualifier("edu.stanford.irt.cocoon.xml.TransformerHandlerFactory/joost") final TransformerHandlerFactory joostTransformerHandlerFactory) {
        return new TraxTransformer("joost", joostTransformerHandlerFactory);
    }

    @Bean(name = "edu.stanford.irt.cocoon.pipeline.Transformer/namespace-filter")
    @Scope("prototype")
    public Transformer namespaceFilterTransformer() {
        return new NamespaceFilter();
    }

    @Bean(name = "edu.stanford.irt.cocoon.pipeline.Generator/noncached-classes")
    @Scope("prototype")
    public Generator nonCachedClassesGenerator(
            @Qualifier("edu.stanford.irt.cocoon.xml.SAXParser/xml") final SAXParser xmlSAXParser) {
        return new RootElementProvidingGenerator("noncached-classes", xmlSAXParser);
    }

    @Bean(name = "edu.stanford.irt.cocoon.pipeline.Pipeline/noncaching")
    @Scope("prototype")
    public Pipeline nonCachingPipeline() {
        return new NonCachingPipeline();
    }

    @Bean(name = "edu.stanford.irt.cocoon.pipeline.Generator/parameter-map")
    @Scope("prototype")
    public Generator parameterMapGenerator(final Marshaller marshaller) {
        return new ParameterMapGenerator(marshaller);
    }

    @Bean(name = "edu.stanford.irt.cocoon.pipeline.Transformer/saxon")
    @Scope("prototype")
    public Transformer saxonTransformer(
            @Qualifier("edu.stanford.irt.cocoon.xml.TransformerHandlerFactory/saxon") final TransformerHandlerFactory saxonTransformerHandlerFactory) {
        return new TraxTransformer("saxon", saxonTransformerHandlerFactory);
    }

    @Bean(name = "edu.stanford.irt.cocoon.pipeline.Generator/seminars")
    @Scope("prototype")
    public Generator seminarsGenerator(final BeanFactory beanFactory, final SourceResolver sourceResolver) {
        return new SeminarsGenerator(beanFactory.getBean("edu.stanford.irt.cocoon.xml.SAXParser/html", SAXParser.class),
                sourceResolver);
    }

    @Bean(name = "edu.stanford.irt.cocoon.pipeline.Transformer/textNodeParser")
    @Scope("prototype")
    public Transformer textNodeParsingTransformer(final BeanFactory beanFactory) {
        return new TextNodeParsingTransformer("textNodeParser",
                beanFactory.getBean("edu.stanford.irt.cocoon.xml.SAXParser/html-fragment", SAXParser.class));
    }

    @Bean(name = "edu.stanford.irt.cocoon.pipeline.Serializer/text")
    @Scope("prototype")
    public Serializer textSerializer(
            @Qualifier("javax.xml.transform.sax.SAXTransformerFactory/saxon") final SAXTransformerFactory saxonSAXTranformerFactory) {
        Properties props = new Properties();
        props.setProperty(METHOD, "text");
        return new TransformerSerializer("text", saxonSAXTranformerFactory, props);
    }

    @Bean(name = "edu.stanford.irt.cocoon.pipeline.Pipeline/throttling")
    @Scope("prototype")
    public Pipeline throttlingPipeline() {
        return new ThrottlingPipeline();
    }

    @Bean(name = "edu.stanford.irt.cocoon.pipeline.Serializer/xhtml")
    @Scope("prototype")
    public Serializer xhtmlSerializer(
            @Qualifier("javax.xml.transform.sax.SAXTransformerFactory/saxon") final SAXTransformerFactory saxonSAXTranformerFactory) {
        Properties props = new Properties();
        props.setProperty(METHOD, XHTML);
        props.setProperty(ENCODING, UTF_8);
        props.setProperty(INDENT, NO);
        props.setProperty("doctype-public", "-//W3C//DTD XHTML 1.0 Strict//EN");
        props.setProperty("doctype-system", "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd");
        props.setProperty(OMIT_XML_DECLARATION, YES);
        return new HTML5Serializer(XHTML, saxonSAXTranformerFactory, props);
    }

    @Bean(name = "edu.stanford.irt.cocoon.pipeline.Transformer/xinclude")
    @Scope("prototype")
    public Transformer xIncludeTransformer(final BeanFactory beanFactory) {
        return new XIncludeTransformer(
                beanFactory.getBean("edu.stanford.irt.cocoon.xml.XIncludePipe", XIncludePipe.class));
    }

    @Bean(name = "edu.stanford.irt.cocoon.pipeline.Serializer/xml")
    @Scope("prototype")
    public Serializer xmlSerializer(
            @Qualifier("javax.xml.transform.sax.SAXTransformerFactory/saxon") final SAXTransformerFactory saxonSAXTranformerFactory) {
        Properties props = new Properties();
        props.setProperty(METHOD, "xml");
        return new TransformerSerializer("xml", saxonSAXTranformerFactory, props);
    }
}
