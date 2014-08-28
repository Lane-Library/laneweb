package edu.stanford.irt.laneweb.search;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Collection;

import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import edu.stanford.irt.search.Query;
import edu.stanford.irt.search.SearchException;
import edu.stanford.irt.search.SearchStatus;
import edu.stanford.irt.search.impl.ContentResult;
import edu.stanford.irt.search.impl.Result;

/**
 * I turn a result Object into SAX events. The root element is 'search', top level result children are 'engine'
 * elements, while 'engine' children are 'resource' elements.  Copied from lane-search project
 */
public class SAXResult {

    /** the String 'author'. */
    public static final String AUTHOR = "author";

    /** the String 'CDATA'. */
    public static final String CDATA = "CDATA";

    /** the String 'contents'. */
    public static final String CONTENT = "content";

    /** the String 'description'. */
    public static final String DESCRIPTION = "description";

    /** the String 'engine'. */
    public static final String ENGINE = "engine";

    /** the String 'exception'. */
    public static final String EXCEPTION = "exception";

    /** the String 'hits'. */
    public static final String HITS = "hits";

    /** the String 'id'. */
    public static final String ID = "id";

    /** the String 'message'. */
    public static final String MESSAGE = "message";

    /** the String 'name'. */
    public static final String NAME = "name";

    /** the namespace, currently http://irt.stanford.edu/search/2.0 */
    public static final String NAMESPACE = "http://irt.stanford.edu/search/2.0";

    /** the String 'page'. */
    public static final String PAGES = "page";

    public static final String PUBLICATION_DATE = "pub-date";

    public static final String PUBLICATION_ISSUE = "pub-issue";

    public static final String PUBLICATION_TEXT = "pub-text";

    public static final String PUBLICATION_TITLE = "pub-title";

    public static final String PUBLICATION_VOLUME = "pub-volume";

    /** the String 'query'. */
    public static final String QUERY = "query";

    /** the String 'resource'. */
    public static final String RESOURCE = "resource";

    /** the root element, the String 'search'. */
    public static final String SEARCH = "search";

    /** the String 'stacktrace'. */
    public static final String STACKTRACE = "stacktrace";

    /** the String 'status'. */
    public static final String STATUS = "status";

    /** the String 'time'. */
    public static final String TIME = "time";

    /** the String 'title'. */
    public static final String TITLE = "title";

    /** the String 'url'. */
    public static final String URL = "url";

    /** convenience static constant for elements with no attributes. */
    protected static final Attributes EMPTY_ATTS = new AttributesImpl();

    private static final int ENGINE_LEVEL = 1;

    private static final int RESOURCE_LEVEL = 2;

    private static final int RESULT_LEVEL = 0;

    private static final int UNINITIALIZED = -1;

    /** a counter to keep track of what level of the composite tree I am at. */
    private int level = UNINITIALIZED;

    /** the Result object. */
    private Result result;

    /**
     * @param result
     *            a Result object
     */
    public SAXResult(final Result result) {
        if (result == null) {
            throw new SearchException("null result");
        }
        this.result = result;
    }

    /**
     * @param handler
     *            the ContentHandler
     * @throws SAXException
     *             if broken
     */
    public void toSAX(final ContentHandler handler) throws SAXException {
        doToSAX(handler, this.result);
    }

    /**
     * I handle 'leaf' elements, that is elements with only text node children.
     *
     * @param handler
     *            the ContentHandler
     * @param name
     *            the element name
     * @param content
     *            the String content
     * @throws SAXException
     *             if broken
     */
    protected void handleElement(final ContentHandler handler, final String name, final String content)
            throws SAXException {
        if (content == null || content.length() == 0) {
            return;
        }
        char[] characters = content.toCharArray();
        handler.startElement(NAMESPACE, name, name, EMPTY_ATTS);
        handler.characters(characters, 0, characters.length);
        handler.endElement(NAMESPACE, name, name);
    }

    /**
     * I use recursion to step through the composite Result object, keeping track of what level I am using 'level'.
     *
     * @param handler
     *            the ContentHandler
     * @param result
     *            the Result to convert
     * @throws SAXException
     *             probably due to programming error.
     */
    private void doToSAX(final ContentHandler handler, final Result result) throws SAXException {
        this.level++;
        AttributesImpl atts = new AttributesImpl();
        Collection<Result> children;
        String description;
        Exception exception;
        String hits;
        String id;
        Query query;
        SearchStatus status;
        String time;
        String url;
        synchronized (result) {
            children = result.getChildren();
            description = result.getDescription();
            exception = result.getException();
            hits = result.getHits();
            id = result.getId();
            query = result.getQuery();
            status = result.getStatus();
            time = result.getTime();
            url = result.getURL();
        }
        if (id.endsWith("_content")) {
            --this.level;
            return;
        }
        atts.addAttribute(NAMESPACE, ID, ID, CDATA, id);
        if (status != null) {
            atts.addAttribute(NAMESPACE, STATUS, STATUS, CDATA, status.toString().toLowerCase());
        }
        if (this.level == RESULT_LEVEL) {
            handler.startDocument();
            handler.startElement(NAMESPACE, SEARCH, SEARCH, atts);
            if (query != null) {
                handleElement(handler, QUERY, query.getSearchText());
            }
        } else if (this.level == ENGINE_LEVEL) {
            handler.startElement(NAMESPACE, ENGINE, ENGINE, atts);
        } else if (this.level == RESOURCE_LEVEL) {
            handler.startElement(NAMESPACE, RESOURCE, RESOURCE, atts);
        }
        handleElement(handler, URL, url);
        handleElement(handler, HITS, hits);
        handleElement(handler, TIME, time);
        handleElement(handler, DESCRIPTION, description);
        doToSAXException(handler, exception);
        for (Result child : children) {
            if (child instanceof ContentResult) {
                doToSAXContent(handler, (ContentResult) child);
            } else {
                doToSAX(handler, child);
            }
        }
        if (this.level == RESULT_LEVEL) {
            handler.endElement(NAMESPACE, SEARCH, SEARCH);
            handler.endDocument();
        } else if (this.level == ENGINE_LEVEL) {
            handler.endElement(NAMESPACE, ENGINE, ENGINE);
        } else if (this.level == RESOURCE_LEVEL) {
            handler.endElement(NAMESPACE, RESOURCE, RESOURCE);
        }
        this.level--;
    }

    private void doToSAXContent(final ContentHandler handler, final ContentResult result) throws SAXException {
        handler.startElement(NAMESPACE, CONTENT, CONTENT, EMPTY_ATTS);
        handleElement(handler, ID, result.getContentId());
        handleElement(handler, TITLE, result.getTitle());
        handleElement(handler, DESCRIPTION, result.getDescription());
        handleElement(handler, AUTHOR, result.getAuthor());
        handleElement(handler, PUBLICATION_DATE, result.getPublicationDate());
        handleElement(handler, PUBLICATION_TITLE, result.getPublicationTitle());
        handleElement(handler, PUBLICATION_VOLUME, result.getPublicationVolume());
        handleElement(handler, PUBLICATION_ISSUE, result.getPublicationIssue());
        handleElement(handler, PUBLICATION_TEXT, result.getPublicationText());
        handleElement(handler, URL, result.getURL());
        handler.endElement(NAMESPACE, CONTENT, CONTENT);
    }

    private void doToSAXException(final ContentHandler handler, final Exception exception) throws SAXException {
        if (exception != null) {
            handler.startElement(NAMESPACE, EXCEPTION, EXCEPTION, EMPTY_ATTS);
            handleElement(handler, MESSAGE, exception.getMessage());
            StringWriter sw = new StringWriter();
            PrintWriter writer = new PrintWriter(sw);
            exception.printStackTrace(writer);
            handleElement(handler, STACKTRACE, sw.toString());
            handler.endElement(NAMESPACE, EXCEPTION, EXCEPTION);
        }
    }
}
