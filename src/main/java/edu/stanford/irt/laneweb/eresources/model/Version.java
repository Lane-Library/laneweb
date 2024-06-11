package edu.stanford.irt.laneweb.eresources.model;

import java.util.List;

public class Version {

    private String additionalText;

    private String callnumber;

    private String holdingsAndDates;

    private int[] itemCount;

    private List<Link> links;

    private String locationCode;

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

    public String getLocationCode() {
        return this.locationCode;
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

    public boolean isProxy() {
        return this.proxy;
    }

    public void setAdditionalText(final String additionalText) {
        this.additionalText = additionalText;
    }

    public void setCallnumber(final String callnumber) {
        this.callnumber = callnumber;
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

    public void setLocationCode(final String locationCode) {
        this.locationCode = locationCode;
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
