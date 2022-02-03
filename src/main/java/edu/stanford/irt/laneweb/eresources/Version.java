package edu.stanford.irt.laneweb.eresources;

import java.util.List;

public class Version {

    private String additionalText;

    private String callnumber;

    private boolean getPasswordLink;

    private String holdingsAndDates;

    private int[] itemCount;

    private List<Link> links;

    private String locationName;

    private String locationUrl;

    private boolean proxy;

    private String publisher;

    private String summaryHoldings;

    public String getAdditionalText() {
        return this.additionalText;
    }

    public String getCallnumber() {
        return this.callnumber;
    }

    public String getHoldingsAndDates() {
        return this.holdingsAndDates;
    }

    public int[] getItemCount() {
        return this.itemCount;
    }

    public List<Link> getLinks() {
        return this.links;
    }

    public String getLocationName() {
        return this.locationName;
    }

    public String getLocationUrl() {
        return this.locationUrl;
    }

    public String getPublisher() {
        return this.publisher;
    }

    public String getSummaryHoldings() {
        return this.summaryHoldings;
    }

    public boolean hasGetPasswordLink() {
        return this.getPasswordLink;
    }

    public boolean isProxy() {
        return this.proxy;
    }

    public void setAdditionalText(final String additionalText) {
        this.additionalText = additionalText;
    }

    public void setCallnumber(final String callnumber) {
        this.callnumber = callnumber;
    }

    public void setHasGetPasswordLink(final boolean getPasswordLink) {
        this.getPasswordLink = getPasswordLink;
    }

    public void setHoldingsAndDates(final String holdingsAndDates) {
        this.holdingsAndDates = holdingsAndDates;
    }

    public void setItemCount(final int[] itemCount) {
        this.itemCount = itemCount;
    }

    public void setLinks(final List<Link> links) {
        this.links = links;
    }

    public void setLocationName(final String locationName) {
        this.locationName = locationName;
    }

    public void setLocationUrl(final String locationUrl) {
        this.locationUrl = locationUrl;
    }

    public void setProxy(final boolean proxy) {
        this.proxy = proxy;
    }

    public void setPublisher(final String publisher) {
        this.publisher = publisher;
    }

    public void setSummaryHoldings(final String summaryHoldings) {
        this.summaryHoldings = summaryHoldings;
    }
}
