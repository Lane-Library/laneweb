package edu.stanford.irt.laneweb.eresources;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;

public class Version {

    private String dates;

    private String description;

    private Eresource eresource;

    private Collection<Link> links = new LinkedList<Link>();

    private String publisher;

    private String summaryHoldings;

    public Version(final String dates, final String description, final String publisher, final String summaryHoldings) {
        this.dates = dates;
        this.description = description;
        this.publisher = publisher;
        this.summaryHoldings = summaryHoldings;
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

    public Collection<Link> getLinks() {
        return Collections.unmodifiableCollection(this.links);
    }

    public String getPublisher() {
        return this.publisher;
    }

    public String getSummaryHoldings() {
        return this.summaryHoldings;
    }

    @Override
    public String toString() {
        return new StringBuilder("publisher:").append(this.publisher).append(" holdings:").append(this.summaryHoldings)
                .append(" links:").append(this.links).toString();
    }

    void addLink(final Link link) {
        link.setVersion(this);
        this.links.add(link);
    }

    void setEresource(final Eresource eresource) {
        this.eresource = eresource;
    }
}
