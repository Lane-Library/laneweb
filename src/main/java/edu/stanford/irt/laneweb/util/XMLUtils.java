package edu.stanford.irt.laneweb.util;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import edu.stanford.irt.cocoon.xml.XMLConsumer;

/**
 * XML utility methods.
 */
public final class XMLUtils {

    /**
     * Empty attributes immutable object.
     */
    public static final Attributes EMPTY_ATTRIBUTES = new ImmutableEmptyAttributes();

    private XMLUtils() {
        // empty default constructor
    }

    public static void createElement(final XMLConsumer xmlConsumer, final String ns, final String name,
            final AttributesImpl atts, final String text) throws SAXException {
        startElement(xmlConsumer, ns, name, atts);
        data(xmlConsumer, text);
        endElement(xmlConsumer, ns, name);
    }

    /**
     * Create a start and endElement without Attributes The content of the Element is set to the stringValue parameter
     *
     * @param xmlConsumer
     *            The SAX content handler
     * @param namespaceURI
     *            The Namespace URI
     * @param localName
     *            The local name (without prefix)
     * @param stringValue
     *            The content of the Element
     * @exception org.xml.sax.SAXException
     *                Any SAX exception, possibly wrapping another exception.
     */
    public static void createElementNS(final XMLConsumer xmlConsumer, final String namespaceURI, final String localName,
            final String stringValue) throws SAXException {
        startElement(xmlConsumer, namespaceURI, localName);
        data(xmlConsumer, stringValue);
        endElement(xmlConsumer, namespaceURI, localName);
    }

    /**
     * Add string data
     *
     * @param xmlConsumer
     *            The SAX content handler
     * @param data
     *            The string data
     * @exception org.xml.sax.SAXException
     *                Any SAX exception, possibly wrapping another exception.
     */
    public static void data(final XMLConsumer xmlConsumer, final String data) throws SAXException {
        xmlConsumer.characters(data.toCharArray(), 0, data.length());
    }

    /**
     * Create endElement Prefix must be mapped to empty String
     * <p>
     * For information on the names, see startElement.
     * </p>
     *
     * @param xmlConsumer
     *            The SAX content handler
     * @param namespaceURI
     *            The Namespace URI
     * @param localName
     *            The local name (without prefix)
     * @exception org.xml.sax.SAXException
     *                Any SAX exception, possibly wrapping another exception.
     */
    public static void endElement(final XMLConsumer xmlConsumer, final String namespaceURI, final String localName)
            throws SAXException {
        xmlConsumer.endElement(namespaceURI, localName, localName);
    }

    public static void maybeCreateElement(final XMLConsumer xmlConsumer, final String namespaceURI, final String name,
            final Object value) throws SAXException {
        if (value != null && !"".equals(value)) {
            XMLUtils.createElementNS(xmlConsumer, namespaceURI, name, value.toString());
        }
    }

    /**
     * Create a startElement without Attributes Prefix must be mapped to empty String
     *
     * @param xmlConsumer
     *            The SAX content handler
     * @param namespaceURI
     *            The Namespace URI
     * @param localName
     *            The local name (without prefix)
     * @exception org.xml.sax.SAXException
     *                Any SAX exception, possibly wrapping another exception.
     */
    public static void startElement(final XMLConsumer xmlConsumer, final String namespaceURI, final String localName)
            throws SAXException {
        xmlConsumer.startElement(namespaceURI, localName, localName, EMPTY_ATTRIBUTES);
    }

    /**
     * Create a startElement with a empty Namespace Prefix must be mapped to empty String
     *
     * @param xmlConsumer
     *            The SAX content handler
     * @param namespaceURI
     *            The Namespace URI
     * @param localName
     *            The local name (without prefix)
     * @param atts
     *            The attributes attached to the element. If there are no attributes, it shall be an empty Attributes
     *            object.
     * @exception org.xml.sax.SAXException
     *                Any SAX exception, possibly wrapping another exception.
     */
    public static void startElement(final XMLConsumer xmlConsumer, final String namespaceURI, final String localName,
            final Attributes atts) throws SAXException {
        xmlConsumer.startElement(namespaceURI, localName, localName, atts);
    }
}
