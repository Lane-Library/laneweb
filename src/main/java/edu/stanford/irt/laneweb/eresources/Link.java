package edu.stanford.irt.laneweb.eresources;

public class Link {

    static final class Builder {

        private String additionalText;

        private String callNumber;

        private String holdingsAndDates;

        private String label;

        private String linkText;

        private String locationName;

        private String locationUrl;

        private String publisher;

        private LinkType type;

        private String url;

        private String versionText;

        public Builder setAdditionalText(final String additionalText) {
            this.additionalText = additionalText;
            return this;
        }

        public Builder setCallNumber(final String callNumber) {
            this.callNumber = callNumber;
            return this;
        }

        public Builder setHoldingsAndDates(final String holdingsAndDates) {
            this.holdingsAndDates = holdingsAndDates;
            return this;
        }

        public Builder setLabel(final String label) {
            this.label = label;
            return this;
        }

        public Builder setLinkText(final String linkText) {
            this.linkText = linkText;
            return this;
        }

        public Builder setLocationName(final String locationName) {
            this.locationName = locationName;
            return this;
        }

        public Builder setLocationUrl(final String locationUrl) {
            this.locationUrl = locationUrl;
            return this;
        }

        public Builder setPublisher(final String publisher) {
            this.publisher = publisher;
            return this;
        }

        public Builder setType(final LinkType type) {
            this.type = type;
            return this;
        }

        public Builder setUrl(final String url) {
            this.url = url;
            return this;
        }

        public Builder setVersionText(final String versionText) {
            this.versionText = versionText;
            return this;
        }

        Link build() {
            return new Link(this);
        }
    }

    private String additionalText;

    private String callNumber;

    private String holdingsAndDates;

    private String label;

    private String linkText;

    private String locationName;

    private String locationUrl;

    private String publisher;

    private LinkType type;

    private String url;

    private String versionText;

    private Link(final Builder builder) {
        this.additionalText = builder.additionalText;
        this.callNumber = builder.callNumber;
        this.holdingsAndDates = builder.holdingsAndDates;
        this.label = builder.label;
        this.linkText = builder.linkText;
        this.locationName = builder.locationName;
        this.locationUrl = builder.locationUrl;
        this.publisher = builder.publisher;
        this.type = builder.type;
        this.url = builder.url;
        this.versionText = builder.versionText;
    }

    public String getAdditionalText() {
        return this.additionalText;
    }

    public String getCallNumber() {
        return this.callNumber;
    }

    public String getHoldingsAndDates() {
        return this.holdingsAndDates;
    }

    public String getLabel() {
        return this.label;
    }

    public String getLinkText() {
        return this.linkText;
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

    public LinkType getType() {
        return this.type;
    }

    public String getUrl() {
        return this.url;
    }

    public String getVersionText() {
        return this.versionText;
    }

    @Override
    public String toString() {
        return new StringBuilder("url:").append(this.url).toString();
    }
}
