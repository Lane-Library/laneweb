package edu.stanford.irt.laneweb.eresources;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import edu.stanford.irt.laneweb.LanewebException;

public class PagingData implements Cloneable {

    private static final int DEFAULT_PAGE_SIZE = 100;

    private static final int MAX_PAGE_COUNT = 4;

    private String allLink;

    private String baseQuery = "";

    private String displayText;

    private int length;

    private int page;

    private int pages;

    private int pageSize;

    private List<String> pagingLinks;

    private boolean showingAll;

    private int size;

    private int start;

    public PagingData(final Collection<? extends Object> resources, final int page, final String baseQuery) {
        if (page >= MAX_PAGE_COUNT) {
            throw new IllegalArgumentException("not so many pages: " + page);
        }
        this.page = page;
        this.size = resources.size();
        this.baseQuery = baseQuery;
        this.pageSize = this.size / MAX_PAGE_COUNT;
        this.pageSize = this.size % MAX_PAGE_COUNT != 0 ? this.pageSize + 1 : this.pageSize;
        this.pageSize = this.pageSize < DEFAULT_PAGE_SIZE ? DEFAULT_PAGE_SIZE : this.pageSize;
        if (page < 0 || this.size <= this.pageSize) {
            this.start = 0;
            this.length = this.size;
        } else {
            this.start = page * this.pageSize;
            this.length = this.size - this.start < this.pageSize ? this.size - this.start : this.pageSize;
        }
        this.pages = this.size / this.pageSize;
        this.pages = this.size % this.pageSize != 0 ? this.pages + 1 : this.pages;
        this.showingAll = this.size <= this.length;
        StringBuilder sb = new StringBuilder("Displaying ");
        if (this.showingAll) {
            sb.append("all ").append(this.size).append(" matches.");
            this.pagingLinks = Collections.emptyList();
        } else {
            sb.append(this.start + 1).append(" to ").append(this.start + this.length).append(" of ");
            this.pagingLinks = new LinkedList<String>();
            for (int i = 1; i <= this.pages; i++) {
                if (i - 1 == this.page) {
                    this.pagingLinks.add("");
                } else {
                    this.pagingLinks.add(this.baseQuery + "page=" + i);
                }
            }
            this.allLink = this.baseQuery + "page=all";
        }
        this.displayText = sb.toString();
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        try {
            Object clone = super.clone();
            List<String> links = new LinkedList<String>();
            links.addAll(this.pagingLinks);
            ((PagingData) clone).pagingLinks = links;
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new LanewebException(e);
        }
    }

    public String getAllLink() {
        return this.allLink;
    }

    public String getDisplayText() {
        return this.displayText;
    }

    public int getLength() {
        return this.length;
    }

    public String getNoPageQuery() {
        return this.baseQuery;
    }

    public int getPage() {
        return this.page;
    }

    public int getPages() {
        return this.pages;
    }

    public int getPageSize() {
        return this.pageSize;
    }

    public int getSize() {
        return this.size;
    }

    public int getStart() {
        return this.start;
    }
}
