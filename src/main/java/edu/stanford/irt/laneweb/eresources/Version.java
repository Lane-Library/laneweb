package edu.stanford.irt.laneweb.eresources;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;

public class Version {

    private String dates;

    private String description;

    private String firstLinkText;

    private Collection<Link> links;

    private String publisher;

    private String summaryHoldings;

    public void addLink(final Link link) {
        if (null == this.links) {
            this.links = new LinkedList<Link>();
        }
        this.links.add(link);
    }

    public String getDates() {
        return this.dates;
    }

    public String getDescription() {
        return this.description;
    }

    public String getFirstLinkText() {
        return this.firstLinkText;
    }

    public Collection<Link> getLinks() {
        if (null == this.links) {
            return Collections.emptySet();
        }
        return Collections.unmodifiableCollection(this.links);
    }

    public String getPublisher() {
        return this.publisher;
    }

    public String getSummaryHoldings() {
        return this.summaryHoldings;
    }

    public void setDates(final String dates) {
        this.dates = dates;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public void setFirstLinkText(final String firstLinkText) {
        this.firstLinkText = firstLinkText;
    }

    public void setPublisher(final String publisher) {
        this.publisher = publisher;
    }

    public void setSummaryHoldings(final String summaryHoldings) {
        this.summaryHoldings = summaryHoldings;
    }

    @Override
    public String toString() {
        return new StringBuilder("publisher:").append(this.publisher).append(" holdings:").append(this.summaryHoldings)
                .append(" links:").append(this.links).toString();
    }
}
