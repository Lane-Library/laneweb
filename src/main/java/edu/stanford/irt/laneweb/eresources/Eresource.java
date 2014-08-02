package edu.stanford.irt.laneweb.eresources;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;

import org.apache.solr.client.solrj.beans.Field;
import org.springframework.data.annotation.Transient;

import com.fasterxml.jackson.databind.ObjectMapper;

public class Eresource {

    @Field
    private String description;

    @Field
    private String id;

    @Field
    private Boolean isCore;

    @Transient
    private Collection<Link> linksList = new ArrayList<Link>();

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

    @Field
    String[] type;

    @Field
    private String versionsJson;

    @Field
    private Integer year;

    public void addLink(final Link link) {
        this.linksList.add(link);
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

    public String[] getType() {
        return this.type;
    }

    public String getVersionsJson() {
        return this.versionsJson;
    }

    public Integer getYear() {
        return this.year;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public void setId(final String id) {
        this.id = id;
    }

    private void setLinks() {
        ObjectMapper mapper = new ObjectMapper();
        List<LinkedHashMap<String, Object>> versionData = null;
        try {
            versionData = mapper.readValue(this.versionsJson, List.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        int isFirstLink = 0;
        for (LinkedHashMap<String, Object> versionMap : versionData) {
            if (versionMap.containsKey("links")) {
                for (Object linkObj : (ArrayList<Object>) versionMap.get("links")) {
                    String linkLabel = null;
                    String linkUrl = null;
                    String linkText = null;
                    String additionalText = null;
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
                    if (isFirstLink++ == 0) {
                        linkText = this.title;
                        if (versionMap.containsKey("additionalText")) {
                            additionalText = (String) versionMap.get("additionalText");
                        }
                    }
                    if (jsonLink.containsKey("url")) {
                        linkUrl = (String) jsonLink.get("url");
                    }
                    if (versionMap.get("hasGetPasswordLink") != null
                            && ((Boolean) versionMap.get("hasGetPasswordLink"))) {
                        linkType = LinkType.GETPASSWORD;
                    } else if (linkLabel != null && "impact factor".equalsIgnoreCase(linkLabel)) {
                        linkType = LinkType.IMPACTFACTOR;
                    } else {
                        linkType = LinkType.NORMAL;
                    }
                    this.linksList.add(new Link(linkLabel, linkType, linkUrl, linkText, additionalText));
                }
            }
        }
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

    public void setType(final String[] type) {
        this.type = type;
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
}
