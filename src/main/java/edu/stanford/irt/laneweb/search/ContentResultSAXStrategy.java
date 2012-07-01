package edu.stanford.irt.laneweb.search;

import org.apache.cocoon.xml.XMLConsumer;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import edu.stanford.irt.cocoon.xml.SAXStrategy;
import edu.stanford.irt.laneweb.LanewebException;
import edu.stanford.irt.laneweb.resource.Resource;
import edu.stanford.irt.laneweb.util.XMLUtils;
import edu.stanford.irt.search.ContentResult;


public class ContentResultSAXStrategy implements SAXStrategy<ContentResultSearchResult>, Resource {

    @Override
    public void toSAX(ContentResultSearchResult searchResult, XMLConsumer xmlConsumer) {
        ContentResult result = searchResult.getResult();
        AttributesImpl atts = new AttributesImpl();
        atts.addAttribute(EMPTY_NS, SCORE, SCORE, "CDATA", Integer.toString(searchResult.getScore()));
        atts.addAttribute(EMPTY_NS, TYPE, TYPE, "CDATA", "searchContent");
        try {
            XMLUtils.startElement(xmlConsumer, NAMESPACE, RESULT, atts);
            maybeCreateElement(xmlConsumer, RESOURCE_ID, searchResult.getResourceId());
            maybeCreateElement(xmlConsumer, RESOURCE_NAME, searchResult.getResourceName());
            maybeCreateElement(xmlConsumer, RESOURCE_URL, searchResult.getResourceUrl());
            maybeCreateElement(xmlConsumer, RESOURCE_HITS, searchResult.getResourceHits());
            maybeCreateElement(xmlConsumer, ID, result.getId());
            maybeCreateElement(xmlConsumer, CONTENT_ID, result.getContentId());
            maybeCreateElement(xmlConsumer, TITLE, result.getTitle());
            maybeCreateElement(xmlConsumer, DESCRIPTION, result.getDescription());
            maybeCreateElement(xmlConsumer, AUTHOR, result.getAuthor());
            maybeCreateElement(xmlConsumer, PUBLICATION_DATE, result.getPublicationDate());
            maybeCreateElement(xmlConsumer, PUBLICATION_TITLE, result.getPublicationTitle());
            maybeCreateElement(xmlConsumer, PUBLICATION_VOLUME, result.getPublicationVolume());
            maybeCreateElement(xmlConsumer, PUBLICATION_ISSUE, result.getPublicationIssue());
            maybeCreateElement(xmlConsumer, PAGES, result.getPages());
            maybeCreateElement(xmlConsumer, URL, result.getURL());
            XMLUtils.endElement(xmlConsumer, NAMESPACE, RESULT);
        } catch (SAXException e) {
            throw new LanewebException(e);
        }
    }

    private void maybeCreateElement(final XMLConsumer xmlConsumer, final String name, final String value) throws SAXException {
        if (value != null && !"".equals(value)) {
            XMLUtils.createElementNS(xmlConsumer, NAMESPACE, name, value);
        }
    }
}
