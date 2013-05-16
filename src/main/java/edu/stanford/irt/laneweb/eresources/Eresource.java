/**
 * 
 */
package edu.stanford.irt.laneweb.eresources;

import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;


/**
 * @author ceyates
 */
public class Eresource {

    private String description;

    private int id;

    private String keywords;

    private Collection<String> meshTerms;

    private String preferredTitle;

    private int recordId;

    private String recordType;

    private int score;

    private String title;

    private Collection<String> types;

    private Date updated;

    private Collection<Version> versions;

    public void addMeshTerm(final String meshTerm) {
        if (null == this.meshTerms) {
            this.meshTerms = new HashSet<String>();
        }
        this.meshTerms.add(meshTerm);
    }

    public void addType(final String type) {
        if (null == this.types) {
            this.types = new HashSet<String>();
        }
        this.types.add(type);
    }

    public void addVersion(final Version version) {
        if (null == this.versions) {
            this.versions = new LinkedList<Version>();
        }
        this.versions.add(version);
    }

    public String getDescription() {
        return this.description;
    }

    public int getId() {
        return this.id;
    }

    public String getKeywords() {
        return this.keywords;
    }

    public Collection<String> getMeshTerms() {
        if (null == this.meshTerms) {
            return Collections.emptySet();
        }
        return this.meshTerms;
    }

    public String getPreferredTitle() {
        return this.preferredTitle;
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

    public Collection<String> getTypes() {
        if (null == this.types) {
            return Collections.emptySet();
        }
        return Collections.unmodifiableCollection(this.types);
    }

    public Date getUpdated() {
        return this.updated;
    }

    public Collection<Version> getVersions() {
        if (null == this.versions) {
            return Collections.emptySet();
        }
        return Collections.unmodifiableCollection(this.versions);
    }

    public boolean isCore() {
        return (null != this.types) && this.types.contains("core material");
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public void setId(final int id) {
        this.id = id;
    }

    public void setKeywords(final String keywords) {
        this.keywords = keywords;
    }

    public void setPreferredTitle(final String preferredTitle) {
        this.preferredTitle = preferredTitle;
    }

    public void setRecordId(final int recordId) {
        this.recordId = recordId;
    }

    public void setRecordType(final String recordType) {
        this.recordType = recordType;
    }

    public void setScore(final int score) {
        this.score = score;
    }

    public void setTitle(final String title) {
        this.title = title;
    }

    public void setUpdated(final Date updated) {
        this.updated = updated;
    }

    @Override
    public String toString() {
        return new StringBuilder("title:").append(this.title).append(" score:").append(this.score).append(" updated:")
                .append(this.updated).append(" versions:").append(this.versions).toString();
    }
}
