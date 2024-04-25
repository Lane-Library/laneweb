package edu.stanford.irt.laneweb.eresources.model;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.data.annotation.Transient;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Eresource {

    private int available;

    private String description;

    private Collection<String> dois;

    private String id;

    private boolean isAnExactMatch = false;

    private Collection<String> isbns;

    private Collection<String> issns;

    @Transient
    private Collection<Link> linksList = new ArrayList<>();

    private String primaryType;

    private String publicationAuthorsText;

    private String publicationText;

    private String recordId;

    private String recordType;

    private String title;

    private int total;

    private String versionsJson;

    Gson gon = new GsonBuilder().create();

    public int getAvailable() {
        return this.available;
    }

    public String getDescription() {
        return this.description;
    }

    public Collection<String> getDois() {
        return this.dois;
    }

    public String getId() {
        return this.id;
    }

    public String getIsAnExactMatch() {
        return String.valueOf(this.isAnExactMatch);
    }

    public Collection<String> getIsbns() {
        return this.isbns;
    }

    public Collection<String> getIssns() {
        return this.issns;
    }

    public Collection<Link> getLinks() {
        if (this.linksList.isEmpty() && this.versionsJson != null) {
            setLinksAndVersion();
        }
        return this.linksList;
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

    public String getRecordId() {
        return this.recordId;
    }

    public String getRecordType() {
        return this.recordType;
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

    public boolean isAnExactMatch() {
        return this.isAnExactMatch;
    }

    public void setAnExactMatch(final boolean isAnExactMatch) {
        this.isAnExactMatch = isAnExactMatch;
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

    public void setRecordId(final String recordId) {
        this.recordId = recordId;
    }

    public void setRecordType(final String recordType) {
        this.recordType = recordType;
    }

    public void setTitle(final String title) {
        this.title = title;
    }

    public void setVersionsJson(final String versionsJson) {
        this.versionsJson = versionsJson;
    }

    @Override
    public String toString() {
        return new StringBuilder("title:").append(this.title).append(" versions:").append(this.linksList).toString();
    }

    private LinkType computeLinkType(final Link l, final Version v) {
        LinkType linkType = LinkType.NORMAL;
        String linkUrl = l.getUrl();
        if ("sul".equals(this.recordType) && linkUrl != null && linkUrl.contains("//searchworks.stanford.edu/view")) {
            linkType = LinkType.SUL_PRINT;
        } else if ("sul".equals(this.recordType) && this.primaryType.contains("Print")) {
            linkType = LinkType.SUL_PRINT;
        } else if ("bib".equals(this.recordType) && linkUrl != null
                && linkUrl.contains("//searchworks.stanford.edu/view")) {
            linkType = LinkType.LANE_PRINT;
        } else if (null != v.getLocationName() && v.getLocationName().toLowerCase().contains("digital")) {
            linkType = LinkType.LANE_DIGITAL;
        }
        return linkType;
    }

    private void setLinksAndVersion() {
        Version[] versions = this.gon.fromJson(this.versionsJson, Version[].class);
        for (Version v : versions) {
            v.getLinks().stream().forEach((final Link l) -> {
                l.setVersion(v);
                LinkType lt = computeLinkType(l, v);
                l.setType(lt);
                this.linksList.add(l);
            });
        }
    }
}
