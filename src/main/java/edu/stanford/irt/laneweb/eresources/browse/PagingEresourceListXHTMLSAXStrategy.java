package edu.stanford.irt.laneweb.eresources.browse;

import java.util.ListIterator;

import org.xml.sax.SAXException;

import edu.stanford.irt.cocoon.xml.SAXStrategy;
import edu.stanford.irt.cocoon.xml.XMLConsumer;
import edu.stanford.irt.laneweb.LanewebException;
import edu.stanford.irt.laneweb.eresources.model.Eresource;
import edu.stanford.irt.laneweb.resource.AbstractXHTMLSAXStrategy;

public class PagingEresourceListXHTMLSAXStrategy extends AbstractXHTMLSAXStrategy<PagingEresourceList> {

    private static final int DEFAULT_PAGE_SIZE = 100;

    private SAXStrategy<EresourceListPagingData> pagingSaxStrategy;

    private SAXStrategy<Eresource> saxStrategy;

    public PagingEresourceListXHTMLSAXStrategy(final SAXStrategy<Eresource> saxStrategy,
            final SAXStrategy<EresourceListPagingData> pagingSaxStrategy) {
        this.saxStrategy = saxStrategy;
        this.pagingSaxStrategy = pagingSaxStrategy;
    }

    @Override
    public void toSAX(final PagingEresourceList list, final XMLConsumer xmlConsumer) {
        EresourceListPagingData pagingData = (EresourceListPagingData) list.getPagingData();
        int start = pagingData.getStart();
        int length = pagingData.getLength();
        int size = list.size();
        try {
            startHTMLDocument(xmlConsumer);
            startHead(xmlConsumer);
            createTitle(xmlConsumer, "biomed-resources");
            endHead(xmlConsumer);
            startBody(xmlConsumer);
            if (size > DEFAULT_PAGE_SIZE) {
                this.pagingSaxStrategy.toSAX(pagingData, xmlConsumer);
            }
            startUlWithClass(xmlConsumer, "lwSearchResults");
            if (length > 0) {
                int i = 0;
                for (ListIterator<Eresource> it = list.listIterator(start); it.hasNext() && i < length; i++) {
                    this.saxStrategy.toSAX(it.next(), xmlConsumer);
                }
            }
            endUl(xmlConsumer);
            if (size > DEFAULT_PAGE_SIZE && length > 0) {
                this.pagingSaxStrategy.toSAX(pagingData, xmlConsumer);
            }
            endBody(xmlConsumer);
            endHTMLDocument(xmlConsumer);
        } catch (SAXException e) {
            throw new LanewebException(e);
        }
    }
}
