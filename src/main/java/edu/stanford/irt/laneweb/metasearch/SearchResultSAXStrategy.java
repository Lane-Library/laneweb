package edu.stanford.irt.laneweb.metasearch;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import edu.stanford.irt.cocoon.xml.SAXStrategy;
import edu.stanford.irt.cocoon.xml.XMLConsumer;
import edu.stanford.irt.laneweb.LanewebException;
import edu.stanford.irt.laneweb.resource.Resource;
import edu.stanford.irt.laneweb.util.XMLUtils;
import edu.stanford.irt.search.impl.ContentResult;
import edu.stanford.irt.search.impl.Result;

public class SearchResultSAXStrategy implements SAXStrategy<SearchResult> {

    @Override
    public void toSAX(final SearchResult result, final XMLConsumer xmlConsumer) {
        ContentResult contentResult = result.getContentResult();
        Result resourceResult = result.getResourceResult();
        AttributesImpl atts = new AttributesImpl();
        atts.addAttribute(Resource.EMPTY_NS, Resource.SCORE, Resource.SCORE, Resource.CDATA,
                Integer.toString(result.getScore()));
        atts.addAttribute(Resource.EMPTY_NS, Resource.TYPE, Resource.TYPE, Resource.CDATA, "searchContent");
        try {
            XMLUtils.startElement(xmlConsumer, Resource.NAMESPACE, Resource.RESULT, atts);
            maybeCreateElement(xmlConsumer, Resource.RESOURCE_ID, resourceResult.getId());
            maybeCreateElement(xmlConsumer, Resource.RESOURCE_NAME, resourceResult.getDescription());
            maybeCreateElement(xmlConsumer, Resource.RESOURCE_URL, resourceResult.getURL());
            maybeCreateElement(xmlConsumer, Resource.RESOURCE_HITS, resourceResult.getHits());
            maybeCreateElement(xmlConsumer, Resource.ID, contentResult.getId());
            maybeCreateElement(xmlConsumer, Resource.CONTENT_ID, contentResult.getContentId());
            String title = contentResult.getTitle();
            title = title == null ? "no title" : title;
            maybeCreateElement(xmlConsumer, Resource.TITLE, title);
            maybeCreateElement(xmlConsumer, Resource.DESCRIPTION, contentResult.getDescription());
            maybeCreateElement(xmlConsumer, Resource.AUTHOR, contentResult.getAuthor());
            maybeCreateElement(xmlConsumer, Resource.URL, contentResult.getURL());
            maybeCreateElement(xmlConsumer, Resource.PUBLICATION_TEXT, contentResult.getPublicationText());
            XMLUtils.endElement(xmlConsumer, Resource.NAMESPACE, Resource.RESULT);
        } catch (SAXException e) {
            throw new LanewebException(e);
        }
    }

    private void maybeCreateElement(final XMLConsumer xmlConsumer, final String name, final String value)
            throws SAXException {
        if (value != null && !"".equals(value)) {
            XMLUtils.createElementNS(xmlConsumer, Resource.NAMESPACE, name, value);
        }
    }
}
