package edu.stanford.irt.laneweb.eresources;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;

public class Version {

    private String dates;

    private String description;

    private Eresource eresource;

    private Collection<HistoryLink> links = new LinkedList<HistoryLink>();

    private String publisher;

    private String summaryHoldings;

    public Version(final String dates, final String description, final String publisher, final String summaryHoldings,
            final Eresource eresource) {
        this.dates = dates;
        this.description = description;
        this.publisher = publisher;
        this.summaryHoldings = summaryHoldings;
        this.eresource = eresource;
    }

    public String getDates() {
        return this.dates;
    }

    public String getDescription() {
        return this.description;
    }

    public Eresource getEresource() {
        return this.eresource;
    }

    public Collection<HistoryLink> getLinks() {
        return Collections.unmodifiableCollection(this.links);
    }

    public String getPublisher() {
        return this.publisher;
    }

    public String getSummaryHoldings() {
        return this.summaryHoldings;
    }

    void addLink(final HistoryLink link) {
        this.links.add(link);
    }
}
