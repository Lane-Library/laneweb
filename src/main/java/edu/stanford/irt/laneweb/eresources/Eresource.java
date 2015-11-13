package edu.stanford.irt.laneweb.eresources;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;

import org.apache.solr.client.solrj.beans.Field;
import org.springframework.data.annotation.Transient;

import com.fasterxml.jackson.databind.ObjectMapper;

import edu.stanford.irt.laneweb.LanewebException;

public class Eresource {

    public static class EresourceBuilder {

        private int available;

        private String description;

        private String id;

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

        public EresourceBuilder id(final String id) {
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

    @Field("availableItems")
    private int available;

    @Field
    private String description;

    @Field
    private String id;

    @Field
    private Boolean isCore;

    @Transient
    private Collection<Link> linksList = new ArrayList<Link>();

    @Field
    private String primaryType;

    @Field
    private String publicationAuthorsText;

    @Field
    private String publicationText;

    @Field
    private int recordId;

    @Field
    private String recordType;

    private float score;

    @Field
    private String title;

    @Field("totalItems")
    private int total;

    @Field
    private String versionsJson;

    @Field
    private int year;

    public Eresource(final EresourceBuilder builder) {
        this.description = builder.description;
        this.id = builder.id;
        this.linksList = builder.links;
        this.recordId = builder.recordId;
        this.recordType = builder.recordType;
        this.score = builder.score;
        this.title = builder.title;
        this.primaryType = builder.primaryType;
        this.total = builder.total;
        this.available = builder.available;
    }

    protected Eresource() {
        // spring-data-solr mapping needs this constructor
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

    public String getId() {
        return this.id;
    }

    public Collection<Link> getLinks() {
        if (this.linksList.isEmpty()) {
            setLinks();
        }
        return Collections.unmodifiableCollection(this.linksList);
    }

    public String getPrimaryType() {
        return this.primaryType;
    }

    public String getPublicationAuthorsText() {
        return this.publicationAuthorsText;
    }

    public String getPublicationText() {
        return this.publicationText;
    }

    public int getRecordId() {
        return this.recordId;
    }

    public String getRecordType() {
        return this.recordType;
    }

    public float getScore() {
        return this.score;
    }

    public String getTitle() {
        return this.title;
    }

    public int getTotal() {
        return this.total;
    }

    public String getVersionsJson() {
        return this.versionsJson;
    }

    public int getYear() {
        return this.year;
    }

    public boolean isValid() {
        boolean valid = true;
        // TODO: this is a temporary fix for fogbugz case 110705 Feedback-bug:  Impact factors links not working
        /*
        if (getLinks().size() == 1 && LinkType.IMPACTFACTOR.equals(getLinks().iterator().next().getType())) {
            valid = false;
        }
        */
        return valid;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public void setId(final String id) {
        this.id = id;
    }

    public void setPublicationAuthorsText(final String publicationAuthorsText) {
        this.publicationAuthorsText = publicationAuthorsText;
    }

    public void setPublicationText(final String publicationText) {
        this.publicationText = publicationText;
    }

    public void setRecordId(final int recordId) {
        this.recordId = recordId;
    }

    public void setRecordType(final String recordType) {
        this.recordType = recordType;
    }

    public void setScore(final float score) {
        this.score = score;
    }

    public void setTitle(final String title) {
        this.title = title;
    }

    public void setVersionsJson(final String versionsJson) {
        this.versionsJson = versionsJson;
    }

    public void setYear(final Integer year) {
        this.year = year;
    }

    @Override
    public String toString() {
        return new StringBuilder("title:").append(this.title).append(" score:").append(this.score).append(" updated:")
                .append(" versions:").append(this.linksList).toString();
    }

    private void parseLink(final Object linkObj, final LinkedHashMap<String, Object> versionMap,
            final boolean isFirstLink) {
        String linkLabel = null;
        String linkUrl = null;
        String linkText = null;
        String additionalText = null;
        String holdingsAndDates = null;
        LinkType linkType = null;
        LinkedHashMap<String, Object> jsonLink = (LinkedHashMap<String, Object>) linkObj;
        if (jsonLink.containsKey("label")) {
            linkLabel = (String) jsonLink.get("label");
        }
        if (jsonLink.containsKey("linkText")) {
            linkText = (String) jsonLink.get("linkText");
        }
        if (jsonLink.containsKey("additionalText")) {
            additionalText = (String) jsonLink.get("additionalText");
        }
        if (isFirstLink) {
            linkText = this.title;
        }
        if (jsonLink.containsKey("url")) {
            linkUrl = (String) jsonLink.get("url");
        }
        if (versionMap.containsKey("holdingsAndDates")) {
            holdingsAndDates = (String) versionMap.get("holdingsAndDates");
        }
        if (versionMap.get("hasGetPasswordLink") != null && ((Boolean) versionMap.get("hasGetPasswordLink"))) {
            linkType = LinkType.GETPASSWORD;
        } else if (linkLabel != null && "impact factor".equalsIgnoreCase(linkLabel)) {
            linkType = LinkType.IMPACTFACTOR;
        } else {
            linkType = LinkType.NORMAL;
        }
        this.linksList.add(new Link(linkLabel, linkType, linkUrl, linkText, additionalText, holdingsAndDates));
    }

    private void setLinks() {
        ObjectMapper mapper = new ObjectMapper();
        List<LinkedHashMap<String, Object>> versionData = null;
        try {
            versionData = mapper.readValue(this.versionsJson, List.class);
        } catch (IOException e) {
            throw new LanewebException(e);
        }
        int isFirstLink = 0;
        for (LinkedHashMap<String, Object> versionMap : versionData) {
            if (versionMap.containsKey("links")) {
                for (Object linkObj : (ArrayList<Object>) versionMap.get("links")) {
                    parseLink(linkObj, versionMap, isFirstLink++ == 0);
                }
            }
        }
    }
}
