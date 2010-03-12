package edu.stanford.irt.laneweb.cocoon;

import java.util.Iterator;

import javax.xml.namespace.NamespaceContext;
import javax.xml.namespace.QName;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.excalibur.xml.xpath.PrefixResolver;
import org.apache.excalibur.xml.xpath.XPathProcessor;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XPathProcessorImpl implements XPathProcessor {

    private XPathFactory factory = XPathFactory.newInstance();

    public boolean evaluateAsBoolean(final Node contextNode, final String str) {
        return (Boolean) evaluate(contextNode, str, XPathConstants.BOOLEAN, null);
    }

    public boolean evaluateAsBoolean(final Node contextNode, final String str, final PrefixResolver resolver) {
        return (Boolean) evaluate(contextNode, str, XPathConstants.BOOLEAN, resolver);
    }

    public Number evaluateAsNumber(final Node contextNode, final String str) {
        return (Number) evaluate(contextNode, str, XPathConstants.NUMBER, null);
    }

    public Number evaluateAsNumber(final Node contextNode, final String str, final PrefixResolver resolver) {
        return (Number) evaluate(contextNode, str, XPathConstants.NUMBER, resolver);
    }

    public String evaluateAsString(final Node contextNode, final String str) {
        return (String) evaluate(contextNode, str, XPathConstants.STRING, null);
    }

    public String evaluateAsString(final Node contextNode, final String str, final PrefixResolver resolver) {
        return (String) evaluate(contextNode, str, XPathConstants.STRING, resolver);
    }

    public NodeList selectNodeList(final Node contextNode, final String str) {
        return (NodeList) evaluate(contextNode, str, XPathConstants.NODESET, null);
    }

    public NodeList selectNodeList(final Node contextNode, final String str, final PrefixResolver resolver) {
        return (NodeList) evaluate(contextNode, str, XPathConstants.NODESET, resolver);
    }

    public Node selectSingleNode(final Node contextNode, final String str) {
        return (Node) evaluate(contextNode, str, XPathConstants.NODE, null);
    }

    public Node selectSingleNode(final Node contextNode, final String str, final PrefixResolver resolver) {
        return (Node) evaluate(contextNode, str, XPathConstants.NODE, resolver);
    }

    private Object evaluate(final Node node, final String str, final QName returnType, final PrefixResolver resolver) {
        XPath xpath = this.factory.newXPath();
        if (null != resolver) {
            xpath.setNamespaceContext(new NamespaceContext() {

                public String getNamespaceURI(final String prefix) {
                    return resolver.prefixToNamespace(prefix);
                }

                public String getPrefix(final String namespaceURI) {
                    throw new UnsupportedOperationException();
                }

                @SuppressWarnings("unchecked")
                public Iterator getPrefixes(final String namespaceURI) {
                    throw new UnsupportedOperationException();
                }
            });
        }
        try {
            return xpath.evaluate(str, node, returnType);
        } catch (XPathExpressionException e) {
            throw new IllegalStateException(e);
        }
    }
}
