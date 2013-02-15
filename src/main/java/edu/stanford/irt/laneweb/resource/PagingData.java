package edu.stanford.irt.laneweb.resource;

import java.util.Collection;

public class PagingData {

    private static final int DEFAULT_PAGE_SIZE = 100;

    private static final int MAX_PAGE_COUNT = 4;

    private String baseQuery = "";

    private int length;

    private int page;

    private int pages;

    private int pageSize;

    private int size;

    private int start;

    public PagingData(final Collection<? extends Object> resources, final int page) {
        this(resources, page, "");
    }

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
    }

    public String getBaseQuery() {
        return this.baseQuery;
    }

    public int getLength() {
        return this.length;
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
