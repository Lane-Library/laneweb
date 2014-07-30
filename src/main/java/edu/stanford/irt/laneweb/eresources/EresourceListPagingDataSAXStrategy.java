package edu.stanford.irt.laneweb.eresources;

import org.xml.sax.SAXException;

import edu.stanford.irt.cocoon.xml.XMLConsumer;
import edu.stanford.irt.laneweb.LanewebException;
import edu.stanford.irt.laneweb.resource.AbstractXHTMLSAXStrategy;
import edu.stanford.irt.laneweb.util.XMLUtils;

public class EresourceListPagingDataSAXStrategy extends AbstractXHTMLSAXStrategy<EresourceListPagingData> {

    @Override
    public void toSAX(final EresourceListPagingData pagingData, final XMLConsumer xmlConsumer) {
        try {
            startDivWithClass(xmlConsumer, "resourceListPagination");
            String alpha = pagingData.getAlpha();
            if (alpha == null || "all".equals(alpha)) {
                alpha = "";
            } else {
                alpha = alpha.toUpperCase();
            }
            if (pagingData.getSize() > pagingData.getLength()) {
                startDivWithClass(xmlConsumer, "yui3-g");
                startDivWithClass(xmlConsumer, "yui3-u-5-12");
                createDisplayingMarkup(xmlConsumer, pagingData, alpha);
                endDiv(xmlConsumer);
                String hrefBase = pagingData.getBaseQuery();
                startDivWithClass(xmlConsumer, "yui3-u-1-3");
                createSeeAllMarkup(xmlConsumer, hrefBase, alpha);
                endDiv(xmlConsumer);
                startDivWithClass(xmlConsumer, "yui3-u-1-4");
                startDivWithClass(xmlConsumer, "view-by");
                createPagingButton(xmlConsumer, hrefBase, pagingData, alpha);
                endDiv(xmlConsumer);
                endDiv(xmlConsumer);
                endDiv(xmlConsumer);
            } else {
                createDisplayingMarkup(xmlConsumer, pagingData, alpha);
            }
            endDiv(xmlConsumer);
        } catch (SAXException e) {
            throw new LanewebException(e);
        }
    }

    private void createDisplayingMarkup(final XMLConsumer xmlConsumer, final EresourceListPagingData pagingData,
            final String alpha) throws SAXException {
        String hrefBase = pagingData.getBaseQuery();
        int size = pagingData.getSize();
        int length = pagingData.getLength();
        int start = pagingData.getStart();
        String quotedAlpha = maybeGetQuotedAlpha(alpha);
        if (size > length) {
            createSpanWithId(xmlConsumer, "pageStart", Integer.toString(start + 1));
            StringBuilder sb = new StringBuilder(" to ");
            sb.append(start + length).append(" of ");
            XMLUtils.data(xmlConsumer, sb.toString());
            sb.setLength(0);
            sb.append("?");
            if (hrefBase.length() > 0) {
                sb.append(hrefBase).append("&");
            }
            sb.append("page=all");
            createAnchor(xmlConsumer, sb.toString(), size + quotedAlpha + " titles");
        } else {
            StringBuilder sb = new StringBuilder("all ").append(size).append(quotedAlpha).append(" titles");
            XMLUtils.data(xmlConsumer, sb.toString());
        }
    }

    private void createPagingButton(final XMLConsumer xmlConsumer, final String hrefBase, EresourceListPagingData pagingData, final String alpha) throws SAXException {

        XMLUtils.data(xmlConsumer, "View by page ");
        startDivWithClass(xmlConsumer, "pagingButton");
        StringBuilder sb = new StringBuilder();
        if (alpha.isEmpty()) {
            sb.append("A-Z");
        } else {
            String upperAlpha = alpha.toUpperCase();
            sb.append(upperAlpha).append("a-").append(upperAlpha).append('z');
        }
        XMLUtils.data(xmlConsumer, sb.toString());
        createElementWithClass(xmlConsumer, "i", "fa fa-angle-double-down", "");
        startUlWithClass(xmlConsumer, "pagingLabels");
        int i = 1;
        for (PagingLabel label : pagingData.getPagingLabels()) {
            createPagingLabelMarkup(xmlConsumer, label, hrefBase, i);
            i++;
        }
        endUl(xmlConsumer);
        endDiv(xmlConsumer);
    }

    private void createPagingLabelMarkup(final XMLConsumer xmlConsumer, final PagingLabel label, final String hrefBase,
            final int i) throws SAXException {
        startLi(xmlConsumer);
        StringBuilder sb = new StringBuilder("?");
        if (hrefBase.length() > 0) {
            sb.append(hrefBase).append("&");
        }
        sb.append("page=").append(i);
        startAnchor(xmlConsumer, sb.toString());
        createSpan(xmlConsumer, label.getStart());
        XMLUtils.data(xmlConsumer, " — ");
        createSpan(xmlConsumer, label.getEnd());
        sb.setLength(0);
        sb.append(" (").append(label.getResults()).append(')');
        XMLUtils.data(xmlConsumer, sb.toString());
        endAnchor(xmlConsumer);
        endLi(xmlConsumer);
    }

    private void createSeeAllMarkup(final XMLConsumer xmlConsumer, final String hrefBase, final String alpha)
            throws SAXException {
        StringBuilder sb = new StringBuilder("?");
        if (hrefBase.length() > 0) {
            sb.append(hrefBase).append("&");
        }
        sb.append("page=all");
        startAnchor(xmlConsumer, sb.toString());
        XMLUtils.data(xmlConsumer, new StringBuilder("All").append(maybeGetQuotedAlpha(alpha)).append(" titles ").toString());
        createElementWithClass(xmlConsumer, "i", "fa fa-arrow-right", "");
        endAnchor(xmlConsumer);
    }

    private String maybeGetQuotedAlpha(final String alpha) {
        if (alpha.isEmpty()) {
            return alpha;
        } else {
            return new StringBuilder(" \"").append(alpha).append('"').toString();
        }
    }
}
