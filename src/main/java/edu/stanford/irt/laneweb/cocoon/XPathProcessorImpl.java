package edu.stanford.irt.laneweb.cocoon;

import java.util.Iterator;

import javax.xml.namespace.NamespaceContext;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import javax.xml.xpath.XPathFactoryConfigurationException;

import org.apache.excalibur.xml.xpath.PrefixResolver;
import org.apache.excalibur.xml.xpath.XPathProcessor;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import edu.stanford.irt.laneweb.LanewebException;

public class XPathProcessorImpl implements XPathProcessor {

    private XPathFactory factory;
    
    public XPathProcessorImpl() {
        try {
            this.factory = XPathFactory.newInstance(XPathFactory.DEFAULT_OBJECT_MODEL_URI,"com.sun.org.apache.xpath.internal.jaxp.XPathFactoryImpl", null);
        } catch (XPathFactoryConfigurationException e) {
            throw new LanewebException(e);
        }
    }

    public boolean evaluateAsBoolean(final Node contextNode, final String str) {
    	throw new UnsupportedOperationException();
    }

    public boolean evaluateAsBoolean(final Node contextNode, final String str, final PrefixResolver resolver) {
    	throw new UnsupportedOperationException();
    }

    public Number evaluateAsNumber(final Node contextNode, final String str) {
    	throw new UnsupportedOperationException();
    }

    public Number evaluateAsNumber(final Node contextNode, final String str, final PrefixResolver resolver) {
    	throw new UnsupportedOperationException();
    }

    public String evaluateAsString(final Node contextNode, final String str) {
    	throw new UnsupportedOperationException();
    }

    public String evaluateAsString(final Node contextNode, final String str, final PrefixResolver resolver) {
    	throw new UnsupportedOperationException();
    }

    public NodeList selectNodeList(final Node contextNode, final String str) {
    	throw new UnsupportedOperationException();
    }

    public NodeList selectNodeList(final Node node, final String str, final PrefixResolver resolver) {
        XPath xpath = null;
        synchronized(this.factory) {
            xpath = this.factory.newXPath();
        }
        if (null != resolver) {
            xpath.setNamespaceContext(new NamespaceContext() {

                public String getNamespaceURI(final String prefix) {
                    return resolver.prefixToNamespace(prefix);
                }

                public String getPrefix(final String namespaceURI) {
                    throw new UnsupportedOperationException();
                }

                @SuppressWarnings("rawtypes")
                public Iterator getPrefixes(final String namespaceURI) {
                    throw new UnsupportedOperationException();
                }
            });
        }
        try {
            return (NodeList) xpath.evaluate(str, node, XPathConstants.NODESET);
        } catch (XPathExpressionException e) {
            throw new IllegalStateException(e);
        }
    }

    public Node selectSingleNode(final Node contextNode, final String str) {
    	throw new UnsupportedOperationException();
    }

    public Node selectSingleNode(final Node contextNode, final String str, final PrefixResolver resolver) {
    	throw new UnsupportedOperationException();
    }
}
