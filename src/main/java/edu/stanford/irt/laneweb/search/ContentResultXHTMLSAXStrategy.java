package edu.stanford.irt.laneweb.search;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import edu.stanford.irt.cocoon.xml.SAXStrategy;
import edu.stanford.irt.cocoon.xml.XMLConsumer;
import edu.stanford.irt.laneweb.LanewebException;
import edu.stanford.irt.laneweb.util.XMLUtils;
import edu.stanford.irt.search.ContentResult;
import edu.stanford.irt.search.Result;

public class ContentResultXHTMLSAXStrategy implements SAXStrategy<ContentResultSearchResult> {

    private static final String A = "a";

    private static final String BR = "br";

    private static final String CDATA = "CDATA";

    private static final String CLASS = "class";

    private static final String DIV = "div";

    private static final String EMPTY_NS = "";

    private static final String HREF = "href";

    private static final String LI = "li";

    private static final String SPAN = "span";

    private static final String XHTML_NS = "http://www.w3.org/1999/xhtml";

    @Override
    public void toSAX(final ContentResultSearchResult result, final XMLConsumer xmlConsumer) {
        ContentResult contentResult = result.getContentResult();
        Result resourceResult = result.getResourceResult();
        try {
            XMLUtils.startElement(xmlConsumer, XHTML_NS, LI);
            String resourceName = resourceResult.getDescription();
            int resourceHits = Integer.parseInt(resourceResult.getHits());
            if (resourceName.indexOf("PubMed") == 0) {
                resourceName = "PubMed";
            }
            AttributesImpl atts = new AttributesImpl();
            atts.addAttribute(EMPTY_NS, CLASS, CLASS, CDATA, "primaryLink");
            atts.addAttribute(EMPTY_NS, HREF, HREF, CDATA, contentResult.getURL());
            XMLUtils.startElement(xmlConsumer, XHTML_NS, A, atts);
            XMLUtils.data(xmlConsumer, contentResult.getTitle());
            XMLUtils.endElement(xmlConsumer, XHTML_NS, A);
            // TODO: pubAuthor conditional on search source
            atts = new AttributesImpl();
            atts.addAttribute(EMPTY_NS, CLASS, CLASS, CDATA, "pubAuthor");
            XMLUtils.startElement(xmlConsumer, XHTML_NS, DIV, atts);
            XMLUtils.data(xmlConsumer, contentResult.getAuthor());
            XMLUtils.endElement(xmlConsumer, XHTML_NS, DIV);
            atts = new AttributesImpl();
            atts.addAttribute(EMPTY_NS, CLASS, CLASS, CDATA, "pubTitle");
            XMLUtils.startElement(xmlConsumer, XHTML_NS, DIV, atts);
            String pubTitle = contentResult.getPublicationTitle();
            StringBuilder sb = new StringBuilder();
            if (pubTitle != null && pubTitle.length() > 0) {
                sb.append(pubTitle).append(". ");
                String pubDate = contentResult.getPublicationDate();
                if (pubDate != null) {
                    sb.append(pubDate);
                }
                String pubVolume = contentResult.getPublicationVolume();
                if (pubVolume != null) {
                    sb.append(';').append(pubVolume);
                }
                String pubIssue = contentResult.getPublicationIssue();
                if (pubIssue != null) {
                    sb.append('(').append(pubIssue).append(')');
                }
                String pages = contentResult.getPages();
                if (pages != null) {
                    sb.append(':').append(pages).append('.');
                }
                XMLUtils.data(xmlConsumer, sb.toString());
                if (resourceName.equals("PubMed") && resourceHits <= 10) {
                    atts = new AttributesImpl();
                    atts.addAttribute(EMPTY_NS, CLASS, CLASS, CDATA, "sourceLink");
                    XMLUtils.startElement(xmlConsumer, XHTML_NS, SPAN, atts);
                    sb.setLength(0);
                    sb.append(" - ").append(resourceName);
                    XMLUtils.data(xmlConsumer, sb.toString());
                    XMLUtils.endElement(xmlConsumer, XHTML_NS, SPAN);
                }
                String contentId = contentResult.getContentId();
                if (contentId.indexOf("PMID:") == 0) {
                    String pmid = contentId.substring(5);
                    atts = new AttributesImpl();
                    atts.addAttribute(EMPTY_NS, CLASS, CLASS, CDATA, "pmid");
                    XMLUtils.startElement(xmlConsumer, XHTML_NS, SPAN, atts);
                    XMLUtils.data(xmlConsumer, " PMID: ");
                    sb.setLength(0);
                    sb.append("http://www.ncbi.nlm.nih.gov/pubmed/").append(pmid).append("?otool=stanford");
                    atts = new AttributesImpl();
                    atts.addAttribute(EMPTY_NS, HREF, HREF, CDATA, sb.toString());
                    XMLUtils.startElement(xmlConsumer, XHTML_NS, A, atts);
                    XMLUtils.data(xmlConsumer, pmid);
                    XMLUtils.endElement(xmlConsumer, XHTML_NS, A);
                    XMLUtils.endElement(xmlConsumer, XHTML_NS, SPAN);
                } else {
                    XMLUtils.data(xmlConsumer, contentId);
                }
                XMLUtils.startElement(xmlConsumer, XHTML_NS, BR);
                XMLUtils.endElement(xmlConsumer, XHTML_NS, BR);
                if (!"PubMed".equals(resourceName) && resourceHits > 10) {
                    atts = new AttributesImpl();
                    atts.addAttribute(EMPTY_NS, HREF, HREF, CDATA, resourceResult.getURL());
                    XMLUtils.startElement(xmlConsumer, XHTML_NS, A, atts);
                    sb.setLength(0);
                    sb.append("All results from").append(resourceName).append("  ");
                    XMLUtils.data(xmlConsumer, sb.toString());
                    XMLUtils.endElement(xmlConsumer, XHTML_NS, A);
                    XMLUtils.data(xmlConsumer, " \u00BB");
                }
            } else if (!"PubMed".equals(resourceName) && resourceHits > 10) {
                atts = new AttributesImpl();
                atts.addAttribute(EMPTY_NS, HREF, HREF, CDATA, resourceResult.getURL());
                XMLUtils.startElement(xmlConsumer, XHTML_NS, A, atts);
                sb.setLength(0);
                sb.append("All results from").append(resourceName).append("  ");
                XMLUtils.data(xmlConsumer, sb.toString());
                XMLUtils.endElement(xmlConsumer, XHTML_NS, A);
                XMLUtils.data(xmlConsumer, " \u00BB");
                // TODO: emrid link for uptodate here . . . .
            } else if (resourceHits <= 10) {
                atts = new AttributesImpl();
                atts.addAttribute(EMPTY_NS, CLASS, CLASS, CDATA, "sourceLink");
                XMLUtils.startElement(xmlConsumer, XHTML_NS, SPAN, atts);
                XMLUtils.data(xmlConsumer, resourceName);
            }
            XMLUtils.endElement(xmlConsumer, XHTML_NS, DIV);
            String description = contentResult.getDescription();
            if (description != null && description.length() > 0) {
                atts = new AttributesImpl();
                atts.addAttribute(EMPTY_NS, CLASS, CLASS, CDATA, "hvrTarg");
                XMLUtils.startElement(xmlConsumer, XHTML_NS, LI, atts);
                XMLUtils.data(xmlConsumer, description);
                XMLUtils.endElement(xmlConsumer, XHTML_NS, LI);
            }
            XMLUtils.endElement(xmlConsumer, XHTML_NS, LI);
        } catch (SAXException e) {
            throw new LanewebException(e);
        }
    }
}
