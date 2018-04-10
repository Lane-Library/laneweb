package edu.stanford.irt.laneweb.util;

import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

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

    public static void createElement(final ContentHandler contentHandler, final String ns, final String name,
            final AttributesImpl atts, final String text) throws SAXException {
        startElement(contentHandler, ns, name, atts);
        data(contentHandler, text);
        endElement(contentHandler, ns, name);
    }

    /**
     * Create a start and endElement without Attributes The content of the Element is set to the stringValue parameter
     *
     * @param contentHandler
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
    public static void createElementNS(final ContentHandler contentHandler, final String namespaceURI, final String localName,
            final String stringValue) throws SAXException {
        startElement(contentHandler, namespaceURI, localName);
        data(contentHandler, stringValue);
        endElement(contentHandler, namespaceURI, localName);
    }

    /**
     * Add string data
     *
     * @param contentHandler
     *            The SAX content handler
     * @param data
     *            The string data
     * @exception org.xml.sax.SAXException
     *                Any SAX exception, possibly wrapping another exception.
     */
    public static void data(final ContentHandler contentHandler, final String data) throws SAXException {
        contentHandler.characters(data.toCharArray(), 0, data.length());
    }

    /**
     * Create endElement Prefix must be mapped to empty String
     * <p>
     * For information on the names, see startElement.
     * </p>
     *
     * @param contentHandler
     *            The SAX content handler
     * @param namespaceURI
     *            The Namespace URI
     * @param localName
     *            The local name (without prefix)
     * @exception org.xml.sax.SAXException
     *                Any SAX exception, possibly wrapping another exception.
     */
    public static void endElement(final ContentHandler contentHandler, final String namespaceURI, final String localName)
            throws SAXException {
        contentHandler.endElement(namespaceURI, localName, localName);
    }

    public static void maybeCreateElement(final ContentHandler contentHandler, final String namespaceURI, final String name,
            final Object value) throws SAXException {
        if (value != null && !"".equals(value)) {
            XMLUtils.createElementNS(contentHandler, namespaceURI, name, value.toString());
        }
    }

    /**
     * Create a startElement without Attributes Prefix must be mapped to empty String
     *
     * @param contentHandler
     *            The SAX content handler
     * @param namespaceURI
     *            The Namespace URI
     * @param localName
     *            The local name (without prefix)
     * @exception org.xml.sax.SAXException
     *                Any SAX exception, possibly wrapping another exception.
     */
    public static void startElement(final ContentHandler contentHandler, final String namespaceURI, final String localName)
            throws SAXException {
        contentHandler.startElement(namespaceURI, localName, localName, EMPTY_ATTRIBUTES);
    }

    /**
     * Create a startElement with a empty Namespace Prefix must be mapped to empty String
     *
     * @param contentHandler
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
    public static void startElement(final ContentHandler contentHandler, final String namespaceURI, final String localName,
            final Attributes atts) throws SAXException {
        contentHandler.startElement(namespaceURI, localName, localName, atts);
    }
}
