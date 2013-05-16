package edu.stanford.irt.laneweb.eresources;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;

public class Eresource {

    private String description;

    private int id;

    private int recordId;

    private String recordType;

    private int score;

    private String title;

    private Collection<Version> versions;

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

    public Collection<Version> getVersions() {
        if (null == this.versions) {
            return Collections.emptySet();
        }
        return Collections.unmodifiableCollection(this.versions);
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public void setId(final int id) {
        this.id = id;
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

    @Override
    public String toString() {
        return new StringBuilder("title:").append(this.title).append(" score:").append(this.score).append(" updated:")
                .append(" versions:").append(this.versions).toString();
    }
}
