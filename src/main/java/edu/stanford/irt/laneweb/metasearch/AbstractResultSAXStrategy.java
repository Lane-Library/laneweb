package edu.stanford.irt.laneweb.metasearch;

import org.xml.sax.SAXException;

import edu.stanford.irt.cocoon.xml.SAXStrategy;
import edu.stanford.irt.cocoon.xml.XMLConsumer;
import edu.stanford.irt.laneweb.util.XMLUtils;
import edu.stanford.irt.search.impl.Result;

public abstract class AbstractResultSAXStrategy<R extends Result> implements SAXStrategy<R> {

    /** the String 'CDATA'. */
    protected static final String CDATA = "CDATA";

    /** the String 'description'. */
    protected static final String DESCRIPTION = "description";

    /** the String 'hits'. */
    protected static final String HITS = "hits";

    /** the String 'id'. */
    protected static final String ID = "id";

    /** the namespace, currently http://irt.stanford.edu/search/2.0 */
    protected static final String NAMESPACE = "http://irt.stanford.edu/search/2.0";

    /** the String 'status'. */
    protected static final String STATUS = "status";

    /** the String 'time'. */
    protected static final String TIME = "time";

    /** the String 'url'. */
    protected static final String URL = "url";

    /** the String 'exception'. */
    private static final String EXCEPTION = "exception";

    /** the String 'message'. */
    private static final String MESSAGE = "message";

    protected void doToSAXException(final XMLConsumer xmlConsumer, final Exception exception) throws SAXException {
        if (exception != null) {
            xmlConsumer.startElement(NAMESPACE, EXCEPTION, EXCEPTION, XMLUtils.EMPTY_ATTRIBUTES);
            handleElement(xmlConsumer, MESSAGE, exception.getMessage());
            xmlConsumer.endElement(NAMESPACE, EXCEPTION, EXCEPTION);
        }
    }

    /**
     * I handle 'leaf' elements, that is elements with only text node children.
     *
     * @param xmlConsumer
     *            the XMLConsumer
     * @param name
     *            the element name
     * @param content
     *            the String content
     * @throws SAXException
     *             if broken
     */
    protected void handleElement(final XMLConsumer xmlConsumer, final String name, final String content)
            throws SAXException {
        if (content == null || content.length() == 0) {
            return;
        }
        char[] characters = content.toCharArray();
        xmlConsumer.startElement(NAMESPACE, name, name, XMLUtils.EMPTY_ATTRIBUTES);
        xmlConsumer.characters(characters, 0, characters.length);
        xmlConsumer.endElement(NAMESPACE, name, name);
    }
}
