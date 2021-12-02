package edu.stanford.irt.laneweb.eresources;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.solr.client.solrj.beans.Field;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.solr.core.mapping.SolrDocument;

import com.fasterxml.jackson.databind.ObjectMapper;

import edu.stanford.irt.laneweb.LanewebException;

@SolrDocument(collection = "laneSearch")
public class Eresource {

    private static final ObjectMapper mapper = new ObjectMapper();

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

    @Override
    public String toString() {
        return new StringBuilder("title:").append(this.title).append(" versions:").append(this.linksList).toString();
    }

    private void parseLink(final Map<String, Object> jsonLink, final Map<String, Object> versionMap) {
        String linkLabel = (String) jsonLink.get("label");
        String linkUrl = (String) jsonLink.get("url");
        String linkText = (String) jsonLink.get("linkText");
        String additionalText = (String) jsonLink.get("additionalText");
        String versionText = (String) versionMap.get("additionalText");
        String holdingsAndDates = (String) versionMap.get("holdingsAndDates");
        String publisher = (String) versionMap.get("publisher");
        String callnumber = (String) versionMap.get("callnumber");
        String locationName = (String) versionMap.get("locationName");
        String locationUrl = (String) versionMap.get("locationUrl");
        List<Integer> itemCountArray = (List<Integer>) versionMap.get("itemCount");
        int[] itemCount = null;
        if (null != itemCountArray) {
            itemCount = itemCountArray.stream().mapToInt(Integer::intValue).toArray();
        }
        LinkType linkType = LinkType.NORMAL;
        if (versionMap.get("hasGetPasswordLink") != null && ((Boolean) versionMap.get("hasGetPasswordLink"))) {
            linkType = LinkType.GETPASSWORD;
        } else if (linkLabel != null && "impact factor".equalsIgnoreCase(linkLabel)) {
            linkType = LinkType.IMPACTFACTOR;
        }
        Link link = new Link.Builder().setLabel(linkLabel).setType(linkType).setUrl(linkUrl).setLinkText(linkText)
                .setAdditionalText(additionalText).setHoldingsAndDates(holdingsAndDates).setPublisher(publisher)
                .setVersionText(versionText).setCallnumber(callnumber).setLocationName(locationName)
                .setLocationUrl(locationUrl).setItemCount(itemCount).build();
        this.linksList.add(link);
    }

    private void setLinks() {
        List<Map<String, Object>> versionData = null;
        try {
            versionData = mapper.readValue(this.versionsJson, List.class);
        } catch (IOException e) {
            throw new LanewebException(e);
        }
        for (Map<String, Object> versionMap : versionData) {
            if (versionMap.containsKey("links")) {
                for (Map<String, Object> linkObj : (List<Map<String, Object>>) versionMap.get("links")) {
                    parseLink(linkObj, versionMap);
                }
            }
        }
    }
}
