package edu.stanford.irt.laneweb.search;

import org.apache.cocoon.xml.XMLConsumer;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import edu.stanford.irt.cocoon.xml.SAXStrategy;
import edu.stanford.irt.laneweb.LanewebException;
import edu.stanford.irt.laneweb.resource.Resource;
import edu.stanford.irt.laneweb.util.XMLUtils;
import edu.stanford.irt.search.ContentResult;
import edu.stanford.irt.search.Result;

public class ContentResultSAXStrategy implements SAXStrategy<ContentResultSearchResult>, Resource {

    @Override
    public void toSAX(final ContentResultSearchResult result, final XMLConsumer xmlConsumer) {
        ContentResult contentResult = result.getContentResult();
        Result resourceResult = result.getResourceResult();
        AttributesImpl atts = new AttributesImpl();
        atts.addAttribute(EMPTY_NS, SCORE, SCORE, "CDATA", Integer.toString(result.getScore()));
        atts.addAttribute(EMPTY_NS, TYPE, TYPE, "CDATA", "searchContent");
        try {
            XMLUtils.startElement(xmlConsumer, NAMESPACE, RESULT, atts);
            maybeCreateElement(xmlConsumer, RESOURCE_ID, resourceResult.getId());
            maybeCreateElement(xmlConsumer, RESOURCE_NAME, resourceResult.getDescription());
            maybeCreateElement(xmlConsumer, RESOURCE_URL, resourceResult.getURL());
            maybeCreateElement(xmlConsumer, RESOURCE_HITS, resourceResult.getHits());
            maybeCreateElement(xmlConsumer, ID, contentResult.getId());
            maybeCreateElement(xmlConsumer, CONTENT_ID, contentResult.getContentId());
            maybeCreateElement(xmlConsumer, TITLE, contentResult.getTitle());
            maybeCreateElement(xmlConsumer, DESCRIPTION, contentResult.getDescription());
            maybeCreateElement(xmlConsumer, AUTHOR, contentResult.getAuthor());
            maybeCreateElement(xmlConsumer, PUBLICATION_DATE, contentResult.getPublicationDate());
            maybeCreateElement(xmlConsumer, PUBLICATION_TITLE, contentResult.getPublicationTitle());
            maybeCreateElement(xmlConsumer, PUBLICATION_VOLUME, contentResult.getPublicationVolume());
            maybeCreateElement(xmlConsumer, PUBLICATION_ISSUE, contentResult.getPublicationIssue());
            maybeCreateElement(xmlConsumer, PAGES, contentResult.getPages());
            maybeCreateElement(xmlConsumer, URL, contentResult.getURL());
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
