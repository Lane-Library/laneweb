package edu.stanford.irt.laneweb.config;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.ErrorListener;
import javax.xml.transform.URIResolver;
import javax.xml.xpath.XPathFactoryConfigurationException;

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
import edu.stanford.irt.laneweb.cocoon.HTMLSAXParser;
import edu.stanford.irt.laneweb.cocoon.NekoHTMLConfiguration;
import edu.stanford.irt.laneweb.cocoon.TransformerErrorListener;
import edu.stanford.irt.laneweb.cocoon.XIncludeExceptionListenerImpl;

@Configuration
public class XMLConfiguration {

    @Bean
    public DocumentBuilderFactoryBean documentBuilderFactoryBean()
            throws SAXNotRecognizedException, SAXNotSupportedException, ParserConfigurationException {
        DocumentBuilderFactoryBean factoryBean = new DocumentBuilderFactoryBean(
                "org.apache.xerces.jaxp.DocumentBuilderFactoryImpl");
        factoryBean.setCoalescing(false);
        factoryBean.setExpandEntityReferences(true);
        factoryBean.setIgnoringComments(false);
        factoryBean.setIgnoringElementContentWhitespace(false);
        factoryBean.setNamespaceAware(true);
        factoryBean.setValidating(false);
        factoryBean.setXIncludeAware(false);
        factoryBean.setFeatures(Collections
                .singletonMap("http://apache.org/xml/features/nonvalidating/load-external-dtd", Boolean.FALSE));
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
    public TransformerHandlerFactory joostTransformerHandlerFactory(final URIResolver uriResolver) {
        return new TransformerHandlerFactory(joostSAXTransformerFactoryBean().getObject(), uriResolver,
                errorListener());
    }

    @Bean(name = "javax.xml.transform.sax.SAXTransformerFactory/saxon")
    public SAXTransformerFactoryBean saxonSAXTransformerFactoryBean() {
        return new SAXTransformerFactoryBean("net.sf.saxon.TransformerFactoryImpl");
    }

    @Bean(name = "edu.stanford.irt.cocoon.xml.TransformerHandlerFactory/saxon")
    public TransformerHandlerFactory saxonTransformerHandlerFactory(final URIResolver uriResolver) {
        return new TransformerHandlerFactory(saxonSAXTransformerFactoryBean().getObject(), uriResolver,
                errorListener());
    }

    @Bean
    public SAXParserFactoryBean saxParserFactoryBean()
            throws SAXNotRecognizedException, SAXNotSupportedException, ParserConfigurationException {
        SAXParserFactoryBean factoryBean = new SAXParserFactoryBean("org.apache.xerces.jaxp.SAXParserFactoryImpl");
        factoryBean.setNamespaceAware(true);
        factoryBean.setValidating(false);
        factoryBean.setXIncludeAware(false);
        factoryBean.setFeatures(Collections
                .singletonMap("http://apache.org/xml/features/nonvalidating/load-external-dtd", Boolean.FALSE));
        return factoryBean;
    }

    @Bean
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
    public XIncludePipe xIncludePipe(final SourceResolver sourceResolver) throws XPathFactoryConfigurationException,
            SAXNotRecognizedException, SAXNotSupportedException, ParserConfigurationException {
        return new XIncludePipe(sourceResolver, xmlSAXParser(), xIncludeExceptionListener(), xPointerProcessor());
    }

    @Bean(name = "edu.stanford.irt.cocoon.xml.SAXParser/xml")
    public SAXParser xmlSAXParser()
            throws SAXNotRecognizedException, SAXNotSupportedException, ParserConfigurationException {
        return new JaxpSAXParser(saxParserFactoryBean().getObject());
    }

    @Bean
    public XPathFactoryBean xPathFactoryBean() throws XPathFactoryConfigurationException {
        return new XPathFactoryBean("com.sun.org.apache.xpath.internal.jaxp.XPathFactoryImpl");
    }

    @Bean
    public XPointerProcessor xPointerProcessor() throws XPathFactoryConfigurationException, SAXNotRecognizedException,
            SAXNotSupportedException, ParserConfigurationException {
        return new XPointerProcessorImpl(xmlSAXParser(), xPathFactoryBean().getObject(),
                documentBuilderFactoryBean().getObject(), transformerFactoryBean().getObject());
    }
}
