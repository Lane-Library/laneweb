package edu.stanford.irt.laneweb.eresources;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;

public class Eresource {

    private String description;

    private int id;

    private Collection<Link> links = new LinkedList<Link>();

    private int recordId;

    private String recordType;

    private int score;

    private String title;

    public Eresource(final String description, final int id, final int recordId, final String recordType,
            final int score, final String title) {
        this.description = description;
        this.id = id;
        this.recordId = recordId;
        this.recordType = recordType;
        this.score = score;
        this.title = title;
    }

    public String getDescription() {
        return this.description;
    }

    public int getId() {
        return this.id;
    }

    public Collection<Link> getLinks() {
        return Collections.unmodifiableCollection(this.links);
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

    @Override
    public String toString() {
        return new StringBuilder("title:").append(this.title).append(" score:").append(this.score).append(" updated:")
                .append(" versions:").append(this.links).toString();
    }

    void addLink(final Link link) {
        this.links.add(link);
    }
}
