package edu.stanford.irt.laneweb.search.saxstrategy;

import edu.stanford.irt.cocoon.xml.SAXStrategy;
import edu.stanford.irt.cocoon.xml.XMLConsumer;
import edu.stanford.irt.laneweb.eresources.Eresource;
import edu.stanford.irt.laneweb.search.ContentResultSearchResult;
import edu.stanford.irt.laneweb.search.EresourceSearchResult;
import edu.stanford.irt.laneweb.search.SearchResult;

public class MergedSearchSAXStrategy implements SAXStrategy<SearchResult> {

    private SAXStrategy<ContentResultSearchResult> contentStrategy;

    private SAXStrategy<Eresource> eresourceStrategy;

    public MergedSearchSAXStrategy(final SAXStrategy<ContentResultSearchResult> contentStrategy,
            final SAXStrategy<Eresource> eresourceStrategy) {
        this.contentStrategy = contentStrategy;
        this.eresourceStrategy = eresourceStrategy;
    }

    public void toSAX(final SearchResult searchResult, final XMLConsumer xmlConsumer) {
        if (searchResult instanceof EresourceSearchResult) {
            this.eresourceStrategy.toSAX(((EresourceSearchResult) searchResult).getEresource(), xmlConsumer);
        } else if (searchResult instanceof ContentResultSearchResult) {
            this.contentStrategy.toSAX((ContentResultSearchResult) searchResult, xmlConsumer);
        }
    }
}
