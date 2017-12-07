package edu.stanford.irt.laneweb.config;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.ErrorListener;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.URIResolver;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.xpath.XPathFactory;
import javax.xml.xpath.XPathFactoryConfigurationException;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;

import edu.stanford.irt.cocoon.source.SourceResolver;
import edu.stanford.irt.cocoon.spring.DocumentBuilderFactoryBean;
import edu.stanford.irt.cocoon.spring.SAXParserFactoryBean;
import edu.stanford.irt.cocoon.spring.SAXTransformerFactoryBean;
import edu.stanford.irt.cocoon.spring.TransformerFactoryBean;
import edu.stanford.irt.cocoon.spring.XPathFactoryBean;
import edu.stanford.irt.cocoon.xml.JaxpSAXParser;
import edu.stanford.irt.cocoon.xml.SAXParser;
import edu.stanford.irt.cocoon.xml.TransformerHandlerFactory;
import edu.stanford.irt.cocoon.xml.URIResolverImpl;
import edu.stanford.irt.cocoon.xml.XIncludeExceptionListener;
import edu.stanford.irt.cocoon.xml.XIncludePipe;
import edu.stanford.irt.cocoon.xml.XPointerProcessor;
import edu.stanford.irt.cocoon.xml.xpointer.XPointerProcessorImpl;
import edu.stanford.irt.laneweb.LanewebException;
import edu.stanford.irt.laneweb.cocoon.HTMLSAXParser;
import edu.stanford.irt.laneweb.cocoon.NekoHTMLConfiguration;
import edu.stanford.irt.laneweb.cocoon.TransformerErrorListener;
import edu.stanford.irt.laneweb.cocoon.XIncludeExceptionListenerImpl;

@Configuration
public class XMLConfiguration {

    @Bean
    public DocumentBuilderFactoryBean documentBuilderFactoryBean() {
        DocumentBuilderFactoryBean factoryBean = new DocumentBuilderFactoryBean(
                "org.apache.xerces.jaxp.DocumentBuilderFactoryImpl");
        factoryBean.setCoalescing(false);
        factoryBean.setExpandEntityReferences(true);
        factoryBean.setIgnoringComments(false);
        factoryBean.setIgnoringElementContentWhitespace(false);
        factoryBean.setNamespaceAware(true);
        factoryBean.setValidating(false);
        factoryBean.setXIncludeAware(false);
        try {
            factoryBean.setFeatures(Collections
                    .singletonMap("http://apache.org/xml/features/nonvalidating/load-external-dtd", Boolean.FALSE));
        } catch (SAXNotRecognizedException | SAXNotSupportedException | ParserConfigurationException e) {
            throw new LanewebException(e);
        }
        return factoryBean;
    }

    @Bean
    public ErrorListener errorListener() {
        return new TransformerErrorListener();
    }

    @Bean(name = "edu.stanford.irt.cocoon.xml.SAXParser/html-fragment")
    @Scope("prototype")
    public SAXParser htmlFragmentSAXParser() {
        Map<String, String> properties = new HashMap<>();
        properties.put("http://cyberneko.org/html/properties/default-encoding", "UTF-8");
        properties.put("http://cyberneko.org/html/properties/names/elems", "lower");
        properties.put("http://cyberneko.org/html/properties/namespaces-uri", "http://www.w3.org/1999/xhtml");
        Map<String, Boolean> features = new HashMap<>();
        features.put("http://cyberneko.org/html/features/insert-namespaces", Boolean.TRUE);
        features.put("http://cyberneko.org/html/features/balance-tags/document-fragment", Boolean.TRUE);
        NekoHTMLConfiguration configuration = new NekoHTMLConfiguration(properties, features);
        return new HTMLSAXParser(configuration);
    }

    @Bean(name = "edu.stanford.irt.cocoon.xml.SAXParser/html")
    @Scope("prototype")
    public SAXParser htmlSAXParser() {
        Map<String, String> properties = new HashMap<>();
        properties.put("http://cyberneko.org/html/properties/default-encoding", "UTF-8");
        properties.put("http://cyberneko.org/html/properties/names/elems", "lower");
        properties.put("http://cyberneko.org/html/properties/namespaces-uri", "http://www.w3.org/1999/xhtml");
        Map<String, Boolean> features = Collections.singletonMap("http://cyberneko.org/html/features/insert-namespaces",
                Boolean.TRUE);
        NekoHTMLConfiguration configuration = new NekoHTMLConfiguration(properties, features);
        return new HTMLSAXParser(configuration);
    }

    @Bean(name = "javax.xml.transform.sax.SAXTransformerFactory/joost")
    public SAXTransformerFactoryBean joostSAXTransformerFactoryBean() {
        return new SAXTransformerFactoryBean("net.sf.joost.trax.TransformerFactoryImpl");
    }

    @Bean(name = "edu.stanford.irt.cocoon.xml.TransformerHandlerFactory/joost")
    public TransformerHandlerFactory joostTransformerHandlerFactory(
            @Qualifier("javax.xml.transform.sax.SAXTransformerFactory/joost") final SAXTransformerFactory joostSAXTransformerFactory,
            final URIResolver uriResolver, final ErrorListener errorListener) {
        return new TransformerHandlerFactory(joostSAXTransformerFactory, uriResolver, errorListener);
    }

    @Bean(name = "javax.xml.transform.sax.SAXTransformerFactory/saxon")
    public SAXTransformerFactoryBean saxonSAXTransformerFactoryBean() {
        return new SAXTransformerFactoryBean("net.sf.saxon.TransformerFactoryImpl");
    }

    @Bean(name = "edu.stanford.irt.cocoon.xml.TransformerHandlerFactory/saxon")
    public TransformerHandlerFactory saxonTransformerHandlerFactory(
            @Qualifier("javax.xml.transform.sax.SAXTransformerFactory/saxon") final SAXTransformerFactory saxonSAXTransformerFactory,
            final URIResolver uriResolver, final ErrorListener errorListener) {
        return new TransformerHandlerFactory(saxonSAXTransformerFactory, uriResolver, errorListener);
    }

    @Bean
    public SAXParserFactoryBean saxParserFactoryBean() {
        SAXParserFactoryBean factoryBean = new SAXParserFactoryBean("org.apache.xerces.jaxp.SAXParserFactoryImpl");
        factoryBean.setNamespaceAware(true);
        factoryBean.setValidating(false);
        factoryBean.setXIncludeAware(false);
        try {
            factoryBean.setFeatures(Collections
                    .singletonMap("http://apache.org/xml/features/nonvalidating/load-external-dtd", Boolean.FALSE));
        } catch (SAXNotRecognizedException | SAXNotSupportedException | ParserConfigurationException e) {
            throw new LanewebException(e);
        }
        return factoryBean;
    }

    @Bean(name = "javax.xml.transform.TransformerFactory")
    public TransformerFactoryBean transformerFactoryBean() {
        return new TransformerFactoryBean("net.sf.saxon.TransformerFactoryImpl");
    }

    @Bean
    public URIResolver uriResolver(final SourceResolver sourceResolver) {
        return new URIResolverImpl(sourceResolver);
    }

    @Bean
    public XIncludeExceptionListener xIncludeExceptionListener() {
        return new XIncludeExceptionListenerImpl();
    }

    @Bean(name = "edu.stanford.irt.cocoon.xml.XIncludePipe")
    @Scope("prototype")
    public XIncludePipe xIncludePipe(final SourceResolver sourceResolver,
            @Qualifier("edu.stanford.irt.cocoon.xml.SAXParser/xml") final SAXParser xmlSAXParser,
            final XIncludeExceptionListener xIncludeExceptionListener, final XPointerProcessor xPointerProcessor) {
        return new XIncludePipe(sourceResolver, xmlSAXParser, xIncludeExceptionListener, xPointerProcessor);
    }

    @Bean(name = "edu.stanford.irt.cocoon.xml.SAXParser/xml")
    public SAXParser xmlSAXParser(final SAXParserFactory saxParserFactory) {
        return new JaxpSAXParser(saxParserFactory);
    }

    @Bean
    public XPathFactoryBean xPathFactoryBean() {
        try {
            return new XPathFactoryBean("com.sun.org.apache.xpath.internal.jaxp.XPathFactoryImpl");
        } catch (XPathFactoryConfigurationException e) {
            throw new LanewebException(e);
        }
    }

    @Bean
    public XPointerProcessor xPointerProcessor(
            @Qualifier("edu.stanford.irt.cocoon.xml.SAXParser/xml") final SAXParser xmlSAXParser,
            final XPathFactory xPathFactory, final DocumentBuilderFactory documentBuilderFactory,
            @Qualifier("javax.xml.transform.TransformerFactory") final TransformerFactory transformerFactory) {
        return new XPointerProcessorImpl(xmlSAXParser, xPathFactory, documentBuilderFactory, transformerFactory);
    }
}
