package edu.stanford.irt.laneweb.eresources;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.solr.client.solrj.beans.Field;
import org.springframework.data.annotation.Transient;

import com.fasterxml.jackson.databind.ObjectMapper;

import edu.stanford.irt.laneweb.LanewebException;

public class Eresource {

    private static final ObjectMapper mapper = new ObjectMapper();

    @Field("availableItems")
    private int available;

    @Field
    private String description;

    @Field
    private String id;

    @Field
    private Boolean isCore;

    @Transient
    private Collection<Link> linksList = new ArrayList<>();

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

    protected Eresource() {
        // spring-data-solr mapping needs this constructor
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

    private void parseLink(final Map<String, Object> jsonLink, final Map<String, Object> versionMap,
            final boolean isFirstLink) {
        String linkLabel = (String) jsonLink.get("label");
        String linkUrl = (String) jsonLink.get("url");
        String linkText = (String) jsonLink.get("linkText");
        String additionalText = (String) jsonLink.get("additionalText");
        String versionText = (String) versionMap.get("additionalText");
        String holdingsAndDates = (String) versionMap.get("holdingsAndDates");
        String publisher = (String) versionMap.get("publisher");
        LinkType linkType = LinkType.NORMAL;
        if (isFirstLink) {
            linkText = this.title;
        }
        if (versionMap.get("hasGetPasswordLink") != null && ((Boolean) versionMap.get("hasGetPasswordLink"))) {
            linkType = LinkType.GETPASSWORD;
        } else if (linkLabel != null && "impact factor".equalsIgnoreCase(linkLabel)) {
            linkType = LinkType.IMPACTFACTOR;
        }
        this.linksList.add(new Link(linkLabel, linkType, linkUrl, linkText, additionalText, holdingsAndDates, publisher,
                versionText));
    }

    @SuppressWarnings("unchecked")
    private void setLinks() {
        List<Map<String, Object>> versionData = null;
        try {
            versionData = mapper.readValue(this.versionsJson, List.class);
        } catch (IOException e) {
            throw new LanewebException(e);
        }
        int isFirstLink = 0;
        for (Map<String, Object> versionMap : versionData) {
            if (versionMap.containsKey("links")) {
                for (Map<String, Object> linkObj : (List<Map<String, Object>>) versionMap.get("links")) {
                    parseLink(linkObj, versionMap, isFirstLink++ == 0);
                }
            }
        }
    }
}
