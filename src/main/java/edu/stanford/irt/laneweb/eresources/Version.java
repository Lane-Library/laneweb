/**
 * 
 */
package edu.stanford.irt.laneweb.eresources;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;


/**
 * @author ceyates
 */
public class Version {

    private String dates;

    private String description;

    private boolean isProxy = true;

    private Collection<Link> links;

    private String publisher;

    private Collection<String> subsets;

    private String summaryHoldings;

    private String firstLinkText;

    /*
     * (non-Javadoc)
     * @see
     * edu.stanford.irt.eresources.impl.Version#addLink(edu.stanford.irt.eresources
     * .impl.LinkImpl)
     */
    public void addLink(final Link link) {
        if (null == this.links) {
            this.links = new LinkedList<Link>();
        }
        this.links.add(link);
    }

    /*
     * (non-Javadoc)
     * @see edu.stanford.irt.eresources.impl.Version#addSubset(java.lang.String)
     */
    public void addSubset(final String subset) {
        if (null == this.subsets) {
            this.subsets = new HashSet<String>();
        }
        this.subsets.add(subset);
    }

    /*
     * (non-Javadoc)
     * @see edu.stanford.irt.eresources.impl.Version#getDates()
     */
    public String getDates() {
        return this.dates;
    }

    /*
     * (non-Javadoc)
     * @see edu.stanford.irt.eresources.impl.Version#getDescription()
     */
    public String getDescription() {
        return this.description;
    }

    /*
     * (non-Javadoc)
     * @see edu.stanford.irt.eresources.impl.Version#getLinks()
     */
    public Collection<Link> getLinks() {
        if (null == this.links) {
            return Collections.emptySet();
        }
        return Collections.unmodifiableCollection(this.links);
    }

    /*
     * (non-Javadoc)
     * @see edu.stanford.irt.eresources.impl.Version#getPublisher()
     */
    public String getPublisher() {
        return this.publisher;
    }

    /*
     * (non-Javadoc)
     * @see edu.stanford.irt.eresources.impl.Version#getSubsets()
     */
    public Collection<String> getSubsets() {
        if (null == this.subsets) {
            return Collections.emptySet();
        }
        return Collections.unmodifiableCollection(this.subsets);
    }

    /*
     * (non-Javadoc)
     * @see edu.stanford.irt.eresources.impl.Version#getSummaryHoldings()
     */
    public String getSummaryHoldings() {
        return this.summaryHoldings;
    }

    /*
     * (non-Javadoc)
     * @see edu.stanford.irt.eresources.impl.Version#isProxy()
     */
    public boolean isProxy() {
        return this.isProxy;
    }

    /*
     * (non-Javadoc)
     * @see edu.stanford.irt.eresources.impl.Version#setDates(java.lang.String)
     */
    public void setDates(final String dates) {
        this.dates = dates;
    }

    /*
     * (non-Javadoc)
     * @see
     * edu.stanford.irt.eresources.impl.Version#setDescription(java.lang.String)
     */
    public void setDescription(final String description) {
        this.description = description;
    }

    /*
     * (non-Javadoc)
     * @see edu.stanford.irt.eresources.impl.Version#setProxy(boolean)
     */
    public void setProxy(final boolean isProxy) {
        this.isProxy = isProxy;
    }

    /*
     * (non-Javadoc)
     * @see
     * edu.stanford.irt.eresources.impl.Version#setPublisher(java.lang.String)
     */
    public void setPublisher(final String publisher) {
        this.publisher = publisher;
    }

    /*
     * (non-Javadoc)
     * @see
     * edu.stanford.irt.eresources.impl.Version#setSummaryHoldings(java.lang
     * .String)
     */
    public void setSummaryHoldings(final String summaryHoldings) {
        this.summaryHoldings = summaryHoldings;
    }

    @Override
    public String toString() {
        return new StringBuilder("publisher:").append(this.publisher).append(" holdings:").append(this.summaryHoldings)
                .append(" links:").append(this.links).toString();
    }

    public String getFirstLinkText() {
        return this.firstLinkText;
    }

    public void setFirstLinkText(String firstLinkText) {
        this.firstLinkText = firstLinkText;
    }
}
