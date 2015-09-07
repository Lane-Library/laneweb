package edu.stanford.irt.laneweb.eresources;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Eresource {

    public static class EresourceBuilder {

        private int available;

        private String description;

        private int id;

        private List<Link> links = new ArrayList<Link>();

        private String primaryType;

        private int recordId;

        private String recordType;

        private int score;

        private String title;

        private int total;

        public EresourceBuilder addLink(final Link link) {
            this.links.add(link);
            return this;
        }

        public EresourceBuilder available(final int available) {
            this.available = available;
            return this;
        }

        public Eresource build() {
            return new Eresource(this);
        }

        public EresourceBuilder description(final String description) {
            this.description = description;
            return this;
        }

        public EresourceBuilder id(final int id) {
            this.id = id;
            return this;
        }

        public EresourceBuilder primaryType(final String primaryType) {
            this.primaryType = primaryType;
            return this;
        }

        public EresourceBuilder recordId(final int recordId) {
            this.recordId = recordId;
            return this;
        }

        public EresourceBuilder recordType(final String description) {
            this.recordType = description;
            return this;
        }

        public EresourceBuilder score(final int score) {
            this.score = score;
            return this;
        }

        public EresourceBuilder title(final String title) {
            this.title = title;
            return this;
        }

        public EresourceBuilder total(final int total) {
            this.total = total;
            return this;
        }
    }

    private int available;

    private String description;

    private int id;

    private List<Link> links;

    private String primaryType;

    private int recordId;

    private String recordType;

    private int score;

    private String title;

    private int total;

    public Eresource(final EresourceBuilder builder) {
        this.description = builder.description;
        this.id = builder.id;
        this.links = builder.links;
        this.recordId = builder.recordId;
        this.recordType = builder.recordType;
        this.score = builder.score;
        this.title = builder.title;
        this.primaryType = builder.primaryType;
        this.total = builder.total;
        this.available = builder.available;
    }

    public static EresourceBuilder builder() {
        return new EresourceBuilder();
    }

    public int getAvailable() {
        return this.available;
    }

    public String getDescription() {
        return this.description;
    }

    public int getId() {
        return this.id;
    }

    public List<Link> getLinks() {
        return Collections.unmodifiableList(this.links);
    }

    public String getPrimaryType() {
        return this.primaryType;
    }

    public int getRecordId() {
        return this.recordId;
    }

    public String getRecordType() {
        return this.recordType;
    }

    public int getScore() {
        return this.score;
    }

    public String getTitle() {
        return this.title;
    }

    public int getTotal() {
        return this.total;
    }

    public boolean isValid() {
        boolean valid = true;
        // TODO: this is a temporary fix for fogbugz case 110705 Feedback-bug:  Impact factors links not working
        /*
        if (this.links.size() == 1 && LinkType.IMPACTFACTOR.equals(this.links.get(0).getType())) {
            valid = false;
        }
        */
        return valid;
    }

    @Override
    public String toString() {
        return new StringBuilder("title:").append(this.title).append(" score:").append(this.score).append(" updated:")
                .append(" versions:").append(this.links).toString();
    }
}
