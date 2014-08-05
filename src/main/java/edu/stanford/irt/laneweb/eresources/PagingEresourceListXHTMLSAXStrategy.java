package edu.stanford.irt.laneweb.eresources;

import java.util.ListIterator;

import org.xml.sax.SAXException;

import edu.stanford.irt.cocoon.xml.SAXStrategy;
import edu.stanford.irt.cocoon.xml.XMLConsumer;
import edu.stanford.irt.laneweb.LanewebException;
import edu.stanford.irt.laneweb.resource.AbstractXHTMLSAXStrategy;
import edu.stanford.irt.laneweb.resource.PagingData;
import edu.stanford.irt.laneweb.util.XMLUtils;

public class PagingEresourceListXHTMLSAXStrategy extends AbstractXHTMLSAXStrategy<PagingEresourceList> {

    private static final int DEFAULT_PAGE_SIZE = 100;

    private SAXStrategy<PagingData> pagingSaxStrategy;

    private SAXStrategy<Eresource> saxStrategy;

    public PagingEresourceListXHTMLSAXStrategy(final SAXStrategy<Eresource> saxStrategy,
            final SAXStrategy<PagingData> pagingSaxStrategy) {
        this.saxStrategy = saxStrategy;
        this.pagingSaxStrategy = pagingSaxStrategy;
    }

    public void toSAX(final PagingEresourceList list, final XMLConsumer xmlConsumer) {
        PagingData pagingData = list.getPagingData();
        int start = pagingData.getStart();
        int length = pagingData.getLength();
        int size = list.size();
        String heading = list.getHeading();
        try {
            startHTMLDocument(xmlConsumer);
            startHead(xmlConsumer);
            createTitle(xmlConsumer, "biomed-resources");
            endHead(xmlConsumer);
            startBody(xmlConsumer);
            if (size > DEFAULT_PAGE_SIZE) {
                this.pagingSaxStrategy.toSAX(pagingData, xmlConsumer);
            }
            createHeading(xmlConsumer, heading);
            startUlWithClass(xmlConsumer, "lwSearchResults");
            int i = 0;
            for (ListIterator<Eresource> it = list.listIterator(start); it.hasNext() && i < length; i++) {
                Eresource eresource = it.next();
                String description = eresource.getDescription();
                if (description != null && description.length() > 0) {
                    startLiWithClass(xmlConsumer, "hvrTrig");
                } else {
                    startLi(xmlConsumer);
                }
                this.saxStrategy.toSAX(eresource, xmlConsumer);
                endLi(xmlConsumer);
            }
            endUl(xmlConsumer);
            if (size > DEFAULT_PAGE_SIZE) {
                this.pagingSaxStrategy.toSAX(pagingData, xmlConsumer);
            }
            endBody(xmlConsumer);
            endHTMLDocument(xmlConsumer);
        } catch (SAXException e) {
            throw new LanewebException(e);
        }
    }

    private void createHeading(final XMLConsumer xmlConsumer, final String heading) throws SAXException {
        if (heading != null) {
            startElementWithClass(xmlConsumer, "h3", "eresources");
            XMLUtils.data(xmlConsumer, heading);
            createSpan(xmlConsumer, "Access restricted to Stanford Community unless noticed otherwise");
            XMLUtils.endElement(xmlConsumer, XHTML_NS, "h3");
        }
    }
}
