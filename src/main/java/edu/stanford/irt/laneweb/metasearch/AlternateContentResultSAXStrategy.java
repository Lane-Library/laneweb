package edu.stanford.irt.laneweb.metasearch;

import org.xml.sax.SAXException;

import edu.stanford.irt.cocoon.xml.XMLConsumer;
import edu.stanford.irt.laneweb.LanewebException;
import edu.stanford.irt.laneweb.util.XMLUtils;
import edu.stanford.irt.search.impl.ContentResult;

public class AlternateContentResultSAXStrategy extends AbstractResultSAXStrategy<ContentResult> {

    /** the String 'author'. */
    private static final String AUTHOR = "author";

    /** the String 'contents'. */
    private static final String CONTENT = "content";

    private static final String PUBLICATION_DATE = "pub-date";

    private static final String PUBLICATION_ISSUE = "pub-issue";

    private static final String PUBLICATION_TEXT = "pub-text";

    private static final String PUBLICATION_TITLE = "pub-title";

    private static final String PUBLICATION_VOLUME = "pub-volume";

    /** the String 'title'. */
    private static final String TITLE = "title";

    @Override
    public void toSAX(final ContentResult result, final XMLConsumer handler) {
        try {
            handler.startElement(NAMESPACE, CONTENT, CONTENT, XMLUtils.EMPTY_ATTRIBUTES);
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
        } catch (SAXException e) {
            throw new LanewebException(e);
        }
    }
}
