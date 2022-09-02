package edu.stanford.irt.laneweb.eresources;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import org.apache.solr.client.solrj.beans.Field;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.solr.core.mapping.SolrDocument;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;

import edu.stanford.irt.laneweb.LanewebException;

@SolrDocument(collection = "laneSearch")
public class Eresource {

    private static final ObjectMapper mapper = JsonMapper.builder()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false).build();

    @Field("availableItems")
    private int available;

    @Field
    private String description;

    @Field
    private Collection<String> dois;

    @Id
    @Field
    private String id;

    @Transient
    private Collection<Link> linksList = new ArrayList<>();

    @Field
    private String primaryType;

    @Field
    private String publicationAuthorsText;

    @Field
    private String publicationText;

    @Field
    private String recordId;

    @Field
    private String recordType;

    @Field
    private String title;

    @Field("totalItems")
    private int total;

    @Field
    private String versionsJson;

    private boolean isAnExactMatch = false;

    protected Eresource() {
        // spring-data-solr mapping needs this constructor
    }

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

    public String getIsAnExactMatch() {
        return String.valueOf(this.isAnExactMatch);
    }

    public boolean isAnExactMatch() {
        return isAnExactMatch;
    }

    public void setAnExactMatch(boolean isAnExactMatch) {
        this.isAnExactMatch = isAnExactMatch;
    }

    @Override
    public String toString() {
        return new StringBuilder("title:").append(this.title).append(" versions:").append(this.linksList).toString();
    }

    private LinkType computeLinkType(final Link l) {
        Version v = l.getVersion();
        LinkType linkType = LinkType.NORMAL;
        String linkUrl = l.getUrl();
        if ("impact factor".equalsIgnoreCase(l.getLabel())) {
            linkType = LinkType.LANE_IMPACTFACTOR;
        } else if ("sul".equals(this.recordType) && linkUrl != null
                && linkUrl.contains("//searchworks.stanford.edu/view")) {
            linkType = LinkType.SUL_PRINT;
        } else if ("sul".equals(this.recordType) && this.primaryType.contains("Print")) {
            linkType = LinkType.SUL_PRINT;
        } else if (null != linkUrl && linkUrl.contains("//lmldb.stanford.edu/cgi-bin/Pwebrecon.cgi?BBID")) {
            linkType = LinkType.LANE_PRINT;
        } else if (null != v.getLocationName() && v.getLocationName().toLowerCase().contains("digital")) {
            linkType = LinkType.LANE_DIGITAL;
        }
        return linkType;
    }

    private void setLinks() {
        Version[] versions;
        try {
            // versionJson can sometimes contain the string "null" as a value
            // string replace is simpler than using a jackson custom filter
            versions = mapper.readValue(this.versionsJson.replace("\"null\"", "\"\""), Version[].class);
        } catch (IOException e) {
            throw new LanewebException(e);
        }
        for (Version v : versions) {
            v.getLinks().stream().forEach((final Link l) -> {
                l.setVersion(v);
                LinkType lt = computeLinkType(l);
                l.setType(lt);
                if (LinkType.LANE_IMPACTFACTOR.equals(lt)) {
                    v.setPublisher("Journal Citation Reports");
                }
                this.linksList.add(l);
            });
        }
    }
}
