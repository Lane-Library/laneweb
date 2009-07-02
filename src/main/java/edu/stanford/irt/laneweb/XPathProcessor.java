package edu.stanford.irt.laneweb;

import java.io.IOException;
import java.io.StringReader;
import java.util.Iterator;

import org.apache.excalibur.xml.xpath.PrefixResolver;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.namespace.NamespaceContext;
import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;


public class XPathProcessor implements org.apache.excalibur.xml.xpath.XPathProcessor {
    
    public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException {
        XPathProcessor processor = new XPathProcessor();
        DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        builder = factory.newDocumentBuilder();
        InputSource source = new InputSource();
        StringReader reader = new StringReader("<foo xmlns='what'><bar>This is the text</bar><bar id='_2'>more</bar></foo>");
        source.setCharacterStream(reader);
        Node node = builder.parse(source);
        PrefixResolver resolver = new PrefixResolver() {

            public String prefixToNamespace(String prefix) {
                if ("s".equals(prefix)) {
                    return "what";
                }
                throw new RuntimeException();
            }};
        System.out.println(processor.selectNodeList(node, "/s:foo/s:bar", resolver).getLength());
    }
    
    private XPathFactory factory = XPathFactory.newInstance();

    public boolean evaluateAsBoolean(Node contextNode, String str) {
        return (Boolean) doit(contextNode, str, XPathConstants.BOOLEAN, null);
    }

    public boolean evaluateAsBoolean(Node contextNode, String str, PrefixResolver resolver) {
        return (Boolean) doit(contextNode, str, XPathConstants.BOOLEAN, resolver);
    }

    public Number evaluateAsNumber(Node contextNode, String str) {
        return (Number) doit(contextNode, str, XPathConstants.NUMBER, null);
    }

    public Number evaluateAsNumber(Node contextNode, String str, PrefixResolver resolver) {
        return (Number) doit(contextNode, str, XPathConstants.NUMBER, resolver);
    }

    public String evaluateAsString(Node contextNode, String str) {
        return (String) doit(contextNode, str, XPathConstants.STRING, null);
    }

    public String evaluateAsString(Node contextNode, String str, PrefixResolver resolver) {
        return (String) doit(contextNode, str, XPathConstants.STRING, resolver);
    }

    public NodeList selectNodeList(Node contextNode, String str) {
        return (NodeList) doit(contextNode, str, XPathConstants.NODESET, null);
    }

    public NodeList selectNodeList(Node contextNode, String str, PrefixResolver resolver) {
        return (NodeList) doit(contextNode, str, XPathConstants.NODESET, resolver);
    }

    public Node selectSingleNode(Node contextNode, String str) {
        return (Node) doit(contextNode, str, XPathConstants.NODE, null);
    }

    public Node selectSingleNode(Node contextNode, String str, PrefixResolver resolver) {
        return (Node) doit(contextNode, str, XPathConstants.NODE, resolver);
    }
    
    private Object doit(Node node, String str, QName returnType, final PrefixResolver resolver) {
        XPath xpath = this.factory.newXPath();
        if (null != resolver) {
            xpath.setNamespaceContext(new NamespaceContext(){

                public String getNamespaceURI(String prefix) {
                    return resolver.prefixToNamespace(prefix);
                }

                public String getPrefix(String namespaceURI) {
                    throw new UnsupportedOperationException();
                }

                public Iterator getPrefixes(String namespaceURI) {
                    throw new UnsupportedOperationException();
                }});
        }
        try {
            return xpath.evaluate(str, node, returnType);
        } catch (XPathExpressionException e) {
           throw new RuntimeException(e);
        }
    }
}
