package edu.stanford.irt.laneweb.config;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.cache.Cache;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.TransformerHandler;

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
import edu.stanford.irt.cocoon.pipeline.generate.DefaultAggregator;
import edu.stanford.irt.cocoon.pipeline.generate.URLGenerator;
import edu.stanford.irt.cocoon.pipeline.serialize.TransformerSerializer;
import edu.stanford.irt.cocoon.pipeline.transform.NamespaceFilter;
import edu.stanford.irt.cocoon.pipeline.transform.TraxTransformer;
import edu.stanford.irt.cocoon.pipeline.transform.XIncludeTransformer;
import edu.stanford.irt.cocoon.source.SourceResolver;
import edu.stanford.irt.cocoon.xml.SAXParser;
import edu.stanford.irt.cocoon.xml.TransformerHandlerFactory;
import edu.stanford.irt.cocoon.xml.XIncludePipe;
import edu.stanford.irt.cocoon.xml.XMLByteStreamInterpreter;
import edu.stanford.irt.laneweb.cocoon.DebugTransformer;
import edu.stanford.irt.laneweb.cocoon.HTML5Serializer;
import edu.stanford.irt.laneweb.cocoon.TextNodeParsingTransformer;
import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.laneweb.search.ParameterMapGenerator;

@Configuration
public class PipelineConfiguration {

    private static final long FIVE_MINUTES = 300;

    private static final String NO = "no";

    private static final String UTF_8 = "UTF-8";

    private static final String XHTML = "xhtml";

    private static final String YES = "yes";

    @Bean(name = "edu.stanford.irt.cocoon.pipeline.Aggregator/caching")
    @Scope("prototype")
    public Aggregator cachingAggregator() {
        return new AggregatorImpl("caching");
    }

    @Bean(name = "edu.stanford.irt.cocoon.pipeline.Aggregator/default")
    @Scope("prototype")
    public Aggregator defaultAggregator() {
        return new DefaultAggregator();
    }

    @Bean(name = "edu.stanford.irt.cocoon.pipeline.Pipeline/caching")
    @Scope("prototype")
    public Pipeline cachingPipeline(final Cache<Serializable, CachedResponse> cache,
            final XMLByteStreamInterpreter xmlByteStreamInterpreter) {
        return new CachingPipeline(cache, xmlByteStreamInterpreter);
    }

    @Bean(name = "edu.stanford.irt.cocoon.pipeline.Transformer/debug")
    @Scope("prototype")
    public Transformer debugTransformer() {
        List<String> disallowToDisplay = new ArrayList<>();
        disallowToDisplay.add(Model.FLICKR_TOKEN);
        disallowToDisplay.add(Model.BROWZINE_TOKEN);
        return new DebugTransformer(disallowToDisplay);
    }

    @Bean(name = "edu.stanford.irt.cocoon.pipeline.Pipeline/expires")
    @Scope("prototype")
    public Pipeline expiresCachingPipeline(final Cache<Serializable, CachedResponse> cache,
            final XMLByteStreamInterpreter xmlByteStreamInterpreter) {
        return new ExpiresCachingPipeline(cache, xmlByteStreamInterpreter, FIVE_MINUTES);
    }

    @Bean(name = "edu.stanford.irt.cocoon.pipeline.Generator/file")
    @Scope("prototype")
    public Generator fileGenerator(
            @Qualifier("edu.stanford.irt.cocoon.xml.SAXParser/xml") final SAXParser xmlSAXParser) {
        return new URLGenerator("file", xmlSAXParser);
    }

    @Bean(name = "edu.stanford.irt.cocoon.pipeline.Serializer/html5-indent")
    @Scope("prototype")
    public Serializer html5IndentSerializer(final TransformerHandler transformerHandler) {
        Map<String, String> outputKeys = new HashMap<>();
        outputKeys.put(OutputKeys.METHOD, XHTML);
        outputKeys.put(OutputKeys.MEDIA_TYPE, "text/html");
        outputKeys.put(OutputKeys.ENCODING, UTF_8);
        outputKeys.put(OutputKeys.INDENT, YES);
        outputKeys.put(OutputKeys.OMIT_XML_DECLARATION, YES);
        return new HTML5Serializer("html5-indent", transformerHandler, outputKeys);
    }

    @Bean(name = "edu.stanford.irt.cocoon.pipeline.Serializer/html5")
    @Scope("prototype")
    public Serializer html5Serializer(final TransformerHandler transformerHandler) {
        Map<String, String> outputKeys = new HashMap<>();
        outputKeys.put(OutputKeys.METHOD, XHTML);
        outputKeys.put(OutputKeys.MEDIA_TYPE, "text/html");
        outputKeys.put(OutputKeys.ENCODING, UTF_8);
        outputKeys.put(OutputKeys.INDENT, NO);
        outputKeys.put(OutputKeys.OMIT_XML_DECLARATION, YES);
        return new HTML5Serializer("html5", transformerHandler, outputKeys);
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
            @Qualifier("edu.stanford.irt.cocoon.xml.TransformerHandlerFactory/joost")
                final TransformerHandlerFactory joostTransformerHandlerFactory) {
        return new TraxTransformer("joost", joostTransformerHandlerFactory);
    }

    @Bean(name = "edu.stanford.irt.cocoon.pipeline.Transformer/namespace-filter")
    @Scope("prototype")
    public Transformer namespaceFilterTransformer() {
        return new NamespaceFilter();
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
            @Qualifier("edu.stanford.irt.cocoon.xml.TransformerHandlerFactory/saxon")
                final TransformerHandlerFactory saxonTransformerHandlerFactory) {
        return new TraxTransformer("saxon", saxonTransformerHandlerFactory);
    }

    @Bean(name = "edu.stanford.irt.cocoon.pipeline.Transformer/textNodeParser")
    @Scope("prototype")
    public Transformer textNodeParsingTransformer(final BeanFactory beanFactory) {
        return new TextNodeParsingTransformer("textNodeParser",
                beanFactory.getBean("edu.stanford.irt.cocoon.xml.SAXParser/html-fragment", SAXParser.class));
    }

    @Bean(name = "edu.stanford.irt.cocoon.pipeline.Serializer/text")
    @Scope("prototype")
    public Serializer textSerializer(final TransformerHandler transformerHandler) {
        Map<String, String> outputKeys = new HashMap<>();
        outputKeys.put(OutputKeys.METHOD, "text");
        return new TransformerSerializer("text", transformerHandler, outputKeys);
    }

    @Bean(name = "edu.stanford.irt.cocoon.pipeline.Pipeline/throttling")
    @Scope("prototype")
    public Pipeline throttlingPipeline() {
        return new ThrottlingPipeline();
    }

    @Bean
    @Scope("prototype")
    public TransformerHandler transformerHandler(
            @Qualifier("javax.xml.transform.sax.SAXTransformerFactory/saxon")
                final SAXTransformerFactory transformerFactory) throws TransformerConfigurationException {
        return transformerFactory.newTransformerHandler();
    }

    @Bean(name = "edu.stanford.irt.cocoon.pipeline.Serializer/xhtml")
    @Scope("prototype")
    public Serializer xhtmlSerializer(final TransformerHandler transformerHandler) {
        Map<String, String> outputKeys = new HashMap<>();
        outputKeys.put(OutputKeys.METHOD, XHTML);
        outputKeys.put(OutputKeys.ENCODING, UTF_8);
        outputKeys.put(OutputKeys.INDENT, NO);
        outputKeys.put(OutputKeys.DOCTYPE_PUBLIC, "-//W3C//DTD XHTML 1.0 Strict//EN");
        outputKeys.put(OutputKeys.DOCTYPE_SYSTEM, "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd");
        outputKeys.put(OutputKeys.OMIT_XML_DECLARATION, YES);
        return new HTML5Serializer(XHTML, transformerHandler, outputKeys);
    }

    @Bean(name = "edu.stanford.irt.cocoon.pipeline.Transformer/xinclude")
    @Scope("prototype")
    public Transformer xIncludeTransformer(final BeanFactory beanFactory) {
        return new XIncludeTransformer(
                beanFactory.getBean("edu.stanford.irt.cocoon.xml.XIncludePipe", XIncludePipe.class));
    }

    @Bean
    public XMLByteStreamInterpreter xmlByteStreamInterpreter() {
        return new XMLByteStreamInterpreter();
    }

    @Bean(name = "edu.stanford.irt.cocoon.pipeline.Serializer/xml")
    @Scope("prototype")
    public Serializer xmlSerializer(final TransformerHandler transformerHandler) {
        Map<String, String> outputKeys = new HashMap<>();
        outputKeys.put(OutputKeys.METHOD, "xml");
        outputKeys.put(OutputKeys.ENCODING, UTF_8);
        return new TransformerSerializer("xml", transformerHandler, outputKeys);
    }
}
