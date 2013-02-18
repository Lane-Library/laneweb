package edu.stanford.irt.laneweb.search;

import java.util.HashSet;
import java.util.ListIterator;
import java.util.Set;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import edu.stanford.irt.cocoon.xml.SAXStrategy;
import edu.stanford.irt.cocoon.xml.XMLConsumer;
import edu.stanford.irt.eresources.Eresource;
import edu.stanford.irt.laneweb.LanewebException;
import edu.stanford.irt.laneweb.resource.PagingData;
import edu.stanford.irt.laneweb.util.XMLUtils;
import edu.stanford.irt.search.ContentResult;
import edu.stanford.irt.search.Result;

public class PagingSearchResultListXHTMLSAXStrategy implements SAXStrategy<PagingSearchResultList> {

    private static final String CDATA = "CDATA";

    private static final String CLASS = "class";

    private static final String DD = "dd";

    private static final String EMPTY_NS = "";

    private static final String NO_PREFIX = "";

    private static final String UL = "ul";

    private static final String XHTML_NS = "http://www.w3.org/1999/xhtml";

    private static final String ID = "id";

    private static final String DIV = "div";

    private static final String SPAN = "span";

    private static final String HREF = "href";

    private static final String A = "a";

    private static final String STYLE = "style";

    private SAXStrategy<SearchResult> saxStrategy;
    
    private SAXStrategy<PagingData> pagingDataStrategy = new SearchListPagingDataSAXStrategy();

    public PagingSearchResultListXHTMLSAXStrategy(final SAXStrategy<SearchResult> saxStrategy) {
        this.saxStrategy = saxStrategy;
    }

    public void toSAX(final PagingSearchResultList list, final XMLConsumer xmlConsumer) {
        PagingData pagingData = list.getPagingData();
        int start = pagingData.getStart();
        int length = pagingData.getLength();
        try {
            xmlConsumer.startDocument();
            xmlConsumer.startPrefixMapping(NO_PREFIX, XHTML_NS);
            XMLUtils.startElement(xmlConsumer, XHTML_NS, "html");
            XMLUtils.startElement(xmlConsumer, XHTML_NS, "head");
            XMLUtils.startElement(xmlConsumer, XHTML_NS, "title");
            XMLUtils.data(xmlConsumer, "search results");
            XMLUtils.endElement(xmlConsumer, XHTML_NS, "title");
            XMLUtils.endElement(xmlConsumer, XHTML_NS, "head");
            XMLUtils.startElement(xmlConsumer, XHTML_NS, "body");
            AttributesImpl atts = new AttributesImpl();
            atts.addAttribute(EMPTY_NS, CLASS, CLASS, CDATA, "lwSearchResults");
            XMLUtils.startElement(xmlConsumer, XHTML_NS, "dl", atts);
            int i = 0;
            for (ListIterator<SearchResult> it = list.listIterator(start); it.hasNext() && i < length; i++) {
                SearchResult result = it.next();
                    XMLUtils.startElement(xmlConsumer, XHTML_NS, DD);
                    StringBuilder sb = new StringBuilder("r-");
                    sb.append(start + 1 + i);
                    if (isHvrTrig(result)) {
                        sb.append(" hvrTrig");
                    }
                    atts = new AttributesImpl();
                    atts.addAttribute(EMPTY_NS, CLASS, CLASS, CDATA, sb.toString());
                    XMLUtils.startElement(xmlConsumer, XHTML_NS, UL, atts);
                    this.saxStrategy.toSAX(result, xmlConsumer);
                    XMLUtils.endElement(xmlConsumer, XHTML_NS, UL);
                    XMLUtils.endElement(xmlConsumer, XHTML_NS, DD);
            }
            XMLUtils.endElement(xmlConsumer, XHTML_NS, "dl");
            
            int size = list.size();
            if (size > 100) {
            	this.pagingDataStrategy.toSAX(pagingData, xmlConsumer);
            }
            atts = new AttributesImpl();
            atts.addAttribute(EMPTY_NS, ID, ID, CDATA, "search-content-counts");
            XMLUtils.startElement(xmlConsumer, XHTML_NS, DIV, atts);
            XMLUtils.data(xmlConsumer, "\u00A0");
            Set<Result> countedResources = new HashSet<Result>();
            boolean showPubMedStrategies = false;
            for (SearchResult resource : list) {
                if (resource instanceof ContentResultSearchResult) {
                    Result resourceResult = ((ContentResultSearchResult) resource).getResourceResult();
                    if (!countedResources.contains(resourceResult)) {
                        countedResources.add(resourceResult);
                        String id = resourceResult.getId();
                        if ("pubmed".equals(id)) {
                            showPubMedStrategies = true;
                        }
                        atts = new AttributesImpl();
                        atts.addAttribute(EMPTY_NS, ID, ID, CDATA, id);
                        XMLUtils.startElement(xmlConsumer, XHTML_NS, SPAN, atts);
                        atts = new AttributesImpl();
                        atts.addAttribute(EMPTY_NS, HREF, HREF, CDATA, resourceResult.getURL());
                        XMLUtils.startElement(xmlConsumer, XHTML_NS, A, atts);
                        XMLUtils.data(xmlConsumer, resourceResult.getHits());
                        XMLUtils.endElement(xmlConsumer, XHTML_NS, A);
                        XMLUtils.endElement(xmlConsumer, XHTML_NS, SPAN);
                    }
                }
            }
            XMLUtils.endElement(xmlConsumer, XHTML_NS, DIV);
            if (showPubMedStrategies) {
                atts = new AttributesImpl();
                atts.addAttribute(EMPTY_NS, ID, ID, CDATA, "showPubMedStrategies");
                atts.addAttribute(EMPTY_NS, STYLE, STYLE, CDATA, "display:none;");
                XMLUtils.startElement(xmlConsumer, XHTML_NS, SPAN, atts);
                XMLUtils.data(xmlConsumer, "true");
                XMLUtils.endElement(xmlConsumer, XHTML_NS, SPAN);
            }
                        
            XMLUtils.endElement(xmlConsumer, XHTML_NS, "body");
            XMLUtils.endElement(xmlConsumer, XHTML_NS, "html");
            xmlConsumer.endPrefixMapping(NO_PREFIX);
            xmlConsumer.endDocument();
        } catch (SAXException e) {
            throw new LanewebException(e);
        }
    }

    private boolean isHvrTrig(SearchResult result) {
        String description = null;
        if (result instanceof EresourceSearchResult) {
            Eresource eresource = ((EresourceSearchResult) result).getEresource();
            description = eresource.getDescription();
        } else if (result instanceof ContentResultSearchResult) {
            ContentResult contentResult = ((ContentResultSearchResult) result).getContentResult();
            description = contentResult.getDescription();
        }
        return description != null && description.length() > 0;
    }
}
