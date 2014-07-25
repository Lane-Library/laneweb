package edu.stanford.irt.laneweb.search;

import java.util.Collection;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import edu.stanford.irt.cocoon.xml.XMLConsumer;
import edu.stanford.irt.laneweb.LanewebException;
import edu.stanford.irt.laneweb.util.XMLUtils;
import edu.stanford.irt.search.impl.ContentResult;
import edu.stanford.irt.search.impl.Result;

public class ImageMetasearchSAXStrategy extends AbstractImageSearchSAXStrategy<Result> {

    private static final String CDATA = "CDATA";

    private static final String CLASS = "class";

    private static final String DIV = "div";

    private static final String XHTML_NS = "http://www.w3.org/1999/xhtml";

    @Override
    public void toSAX(final Result metaSearchResult, final XMLConsumer xmlConsumer) {
        try {
            Collection<Result> engines = metaSearchResult.getChildren();
            String hits = null;
            for (Result engineResult : engines) {
                synchronized (engineResult) {
                    startDiv(xmlConsumer);
                    Collection<Result> resources = engineResult.getChildren();
                    hits = engineResult.getHits();
                    String url = null;
                    for (Result resource : resources) {
                        if (resource.getId().indexOf("_content") == -1) {
                            url = resource.getURL();
                        }
                        if (resource.getHits() != null && !"0".equals(resource.getHits())
                                && resource.getId().endsWith("_content")) {
                            createTitle(xmlConsumer, resource.getId(), engineResult.getDescription(),
                                    resource.getHits(), hits, url);
                        }
                        startElementWithId(xmlConsumer, UL, "imageList");
                        Collection<Result> contents = resource.getChildren();
                        for (Result content : contents) {
                            if (content.getId().contains("_content_")) {
                                ContentResult contentResult = (ContentResult) content;
                                generateImages(xmlConsumer, contentResult.getContentId(), contentResult.getTitle(),
                                        contentResult.getURL(), contentResult.getDescription(),
                                        contentResult.getAuthor());
                            }
                        }
                        endUl(xmlConsumer);
                    }
                    endDiv(xmlConsumer);
                }
            }
            generateTooltips(xmlConsumer, engines);
        } catch (SAXException e) {
            throw new LanewebException(e);
        }
    }

    protected void generateTooltips(final XMLConsumer xmlConsumer, final Collection<Result> engines)
            throws SAXException {
        AttributesImpl atts = new AttributesImpl();
        atts.addAttribute(XHTML_NS, "style", "style", CDATA, "display:none");
        atts.addAttribute(XHTML_NS, CLASS, CLASS, CDATA, "tooltips");
        XMLUtils.startElement(xmlConsumer, XHTML_NS, DIV, atts);
        for (Result engineResult : engines) {
            Collection<Result> resources = engineResult.getChildren();
            for (Result resource : resources) {
                Collection<Result> contents = resource.getChildren();
                for (Result content : contents) {
                    if (content.getId().contains("_content_")) {
                        ContentResult contentResult = (ContentResult) content;
                        generateTooltipsImage(xmlConsumer, contentResult.getContentId(), contentResult.getAuthor());
                    }
                }
            }
        }
        endDiv(xmlConsumer);
    }
}
