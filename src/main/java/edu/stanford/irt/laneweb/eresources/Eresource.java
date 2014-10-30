package edu.stanford.irt.laneweb.eresources;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;

public class Eresource {

    private String description;

    private int id;

    private Collection<Link> links = new LinkedList<Link>();

    private String primaryType;

    private int recordId;

    private String recordType;

    private int score;

    private String title;
    
    private int total;
    
    private int available;

    public Eresource(final String description, final int id, final int recordId, final String recordType,
            final int score, final String title, final String primaryType, final int total, int available) {
        this.description = description;
        this.id = id;
        this.recordId = recordId;
        this.recordType = recordType;
        this.score = score;
        this.title = title;
        this.primaryType = primaryType;
        this.total = total;
        this.available = available;
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

    public String getPrimaryType() {
        return this.primaryType;
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
    
    public int getTotal() {
        return this.total;
    }
    
    public int getAvailable() {
        return this.available;
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
