package edu.stanford.irt.laneweb.solr;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.apache.solr.client.solrj.beans.Field;
import org.springframework.data.annotation.Transient;

import com.fasterxml.jackson.databind.ObjectMapper;

public class SolrEresource {

    @Field
    private String description;

    @Field
    private String id;

    @Field
    private Boolean isCore;

    @Transient
    private List<Link> linksList;

    @Field
    private String publicationAuthorsText;

    @Field
    private String publicationText;

    @Field
    private Integer recordId;

    @Field
    private String recordType;

    private String score;

    @Field("title_sort")
    String sortedTitle;

    @Field
    private String[] text;

    @Field
    private String title;

    @Field
    String[] type;

    @Field
    private String versionsJson;

    @Field
    private Integer year;

    public String getDescription() {
        return this.description;
    }

    public String getId() {
        return this.id;
    }

    public List<Link> getLinks() {
        if (this.linksList == null) {
            setLinkList();
        }
        return this.linksList;
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

    public Enum<RecordType> getRecordType() {
        if (this.recordType.equals(RecordType.PUBMED.name().toLowerCase())) {
            return RecordType.PUBMED;
        } else if (this.recordType.equals(RecordType.BIB.name().toLowerCase())) {
            return RecordType.BIB;
        } else if (this.recordType.equals(RecordType.WEB.name().toLowerCase())) {
            return RecordType.WEB;
        } else if (this.recordType.equals(RecordType.CLASS.name().toLowerCase())) {
            return RecordType.CLASS;
        } else if (this.recordType.equals(RecordType.PRINT.name().toLowerCase())) {
            return RecordType.PRINT;
        } else {
            return RecordType.OTHER;
        }
    }

    public String getScore() {
        return this.score;
    }

    public String[] getText() {
        return this.text;
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

    private void setLinkList() {
        this.linksList = new ArrayList<Link>();
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
                    Link link = new Link();
                    LinkedHashMap<String, Object> jsonLink = (LinkedHashMap<String, Object>) linkObj;
                    if (jsonLink.containsKey("label")) {
                        link.setLabel((String) jsonLink.get("label"));
                    }
                    if (jsonLink.containsKey("linkText")) {
                        if (isFirstLink++ == 0) {
                            link.setText(this.title);
                        } else {
                            link.setText((String) jsonLink.get("linkText"));
                        }
                    }
                    if (jsonLink.containsKey("additionalText")) {
                        link.setAdditionalText((String) jsonLink.get("additionalText"));
                    }
                    if (jsonLink.containsKey("url")) {
                        link.setUrl((String) jsonLink.get("url"));
                    }
                    if (versionMap.get("hasGetPasswordLink") != null
                            && ((Boolean) versionMap.get("hasGetPasswordLink"))) {
                        link.setType("getPassword");
                    } else if (link.getLabel() != null && "impact factor".equalsIgnoreCase(link.getLabel())) {
                        link.setType("impactFactor");
                    } else {
                        link.setType("normal");
                    }
                    this.linksList.add(link);
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

    public void setScore(final String score) {
        this.score = score;
    }

    public void setText(final String[] text) {
        this.text = text;
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
}
