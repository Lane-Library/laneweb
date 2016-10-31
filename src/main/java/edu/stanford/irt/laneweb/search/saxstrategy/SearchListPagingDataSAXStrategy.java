package edu.stanford.irt.laneweb.search.saxstrategy;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import edu.stanford.irt.cocoon.xml.SAXStrategy;
import edu.stanford.irt.cocoon.xml.XMLConsumer;
import edu.stanford.irt.laneweb.LanewebException;
import edu.stanford.irt.laneweb.resource.PagingData;
import edu.stanford.irt.laneweb.util.XMLUtils;

public class SearchListPagingDataSAXStrategy implements SAXStrategy<PagingData> {

    private static final String A = "a";

    private static final String CDATA = "CDATA";

    private static final String CLASS = "class";

    private static final String DIV = "div";

    private static final String EMPTY_NS = "";

    private static final String HREF = "href";

    private static final String ID = "id";

    private static final String SPAN = "span";

    private static final String XHTML_NS = "http://www.w3.org/1999/xhtml";

    @Override
    public void toSAX(final PagingData pagingData, final XMLConsumer xmlConsumer) {
        try {
            int size = pagingData.getSize();
            int length = pagingData.getLength();
            int start = pagingData.getStart();
            AttributesImpl atts = new AttributesImpl();
            atts.addAttribute(EMPTY_NS, CLASS, CLASS, CDATA, "resourceListPagination");
            XMLUtils.startElement(xmlConsumer, XHTML_NS, DIV, atts);
            String hrefBase = pagingData.getBaseQuery();
            if (size > length) {
                pageCountToSAX(xmlConsumer, size, length, start, hrefBase);
                seeAllToSAX(xmlConsumer, hrefBase);
                paginationToSAX(xmlConsumer, pagingData, hrefBase);
            } else {
                StringBuilder sb = new StringBuilder("all ").append(size).append(" matches");
                XMLUtils.data(xmlConsumer, sb.toString());
            }
            XMLUtils.endElement(xmlConsumer, XHTML_NS, DIV);
        } catch (SAXException e) {
            throw new LanewebException(e);
        }
    }

    private void pageCountToSAX(final XMLConsumer xmlConsumer, final int size, final int length, final int start,
            final String hrefBase) throws SAXException {
        AttributesImpl atts = new AttributesImpl();
        atts.addAttribute(EMPTY_NS, ID, ID, CDATA, "pageStart");
        XMLUtils.startElement(xmlConsumer, XHTML_NS, SPAN, atts);
        XMLUtils.data(xmlConsumer, Integer.toString(start + 1));
        XMLUtils.endElement(xmlConsumer, XHTML_NS, SPAN);
        StringBuilder sb = new StringBuilder(" to ");
        sb.append(start + length).append(" of ");
        XMLUtils.data(xmlConsumer, sb.toString());
        atts = new AttributesImpl();
        sb.setLength(0);
        sb.append('?');
        if (hrefBase.length() > 0) {
            sb.append(hrefBase).append('&');
        }
        sb.append("page=all");
        atts.addAttribute(EMPTY_NS, HREF, HREF, CDATA, sb.toString());
        XMLUtils.startElement(xmlConsumer, XHTML_NS, A, atts);
        sb.setLength(0);
        sb.append(Integer.toString(size)).append(" matches");
        XMLUtils.data(xmlConsumer, sb.toString());
        XMLUtils.endElement(xmlConsumer, XHTML_NS, A);
    }

    private void paginationToSAX(final XMLConsumer xmlConsumer, final PagingData pagingData, final String hrefBase)
            throws SAXException {
        AttributesImpl atts = new AttributesImpl();
        atts.addAttribute(EMPTY_NS, CLASS, CLASS, CDATA, "paginationNumbers");
        XMLUtils.startElement(xmlConsumer, XHTML_NS, DIV, atts);
        int pages = pagingData.getPages();
        int page = pagingData.getPage();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < pages; i++) {
            if (i == page) {
                XMLUtils.data(xmlConsumer, Integer.toString(i + 1));
            } else {
                // <a href="?{$query-string}page={number($page) + 1}"><xsl:value-of
                // select="number($page) + 1"/></a>
                atts = new AttributesImpl();
                sb.setLength(0);
                sb.append('?');
                if (hrefBase.length() > 0) {
                    sb.append(hrefBase).append('&');
                }
                sb.append("page=").append(i + 1);
                atts.addAttribute(EMPTY_NS, HREF, HREF, CDATA, sb.toString());
                XMLUtils.startElement(xmlConsumer, XHTML_NS, A, atts);
                XMLUtils.data(xmlConsumer, Integer.toString(i + 1));
                XMLUtils.endElement(xmlConsumer, XHTML_NS, A);
            }
            if (i + 1 < pages) {
                XMLUtils.data(xmlConsumer, " | ");
            }
        }
        XMLUtils.endElement(xmlConsumer, XHTML_NS, DIV);
    }

    private void seeAllToSAX(final XMLConsumer xmlConsumer, final String hrefBase) throws SAXException {
        // <a id="seeAll" href="?{$no-page-query-string}page=all">See All</a>
        AttributesImpl atts = new AttributesImpl();
        atts.addAttribute(EMPTY_NS, ID, ID, CDATA, "seeAll");
        StringBuilder sb = new StringBuilder("?");
        if (hrefBase.length() > 0) {
            sb.append(hrefBase).append('&');
        }
        sb.append("page=all");
        atts.addAttribute(EMPTY_NS, HREF, HREF, CDATA, sb.toString());
        XMLUtils.startElement(xmlConsumer, XHTML_NS, A, atts);
        XMLUtils.data(xmlConsumer, "See All");
        XMLUtils.endElement(xmlConsumer, XHTML_NS, A);
    }
}
