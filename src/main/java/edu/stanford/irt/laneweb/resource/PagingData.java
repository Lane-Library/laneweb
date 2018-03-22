package edu.stanford.irt.laneweb.resource;

import java.io.Serializable;
import java.util.List;

public class PagingData implements Serializable {

    private static final int DEFAULT_PAGE_SIZE = 100;

    private static final int MAX_PAGE_COUNT = 4;

    private static final long serialVersionUID = 1L;

    private String baseQuery = "";

    private int length;

    private int page;

    private int pages;

    private int pageSize;

    private int size;

    private int start;

    public PagingData(final List<? extends Object> resources, final int page, final String baseQuery) {
        this(resources, page, baseQuery, DEFAULT_PAGE_SIZE, MAX_PAGE_COUNT);
    }

    public PagingData(final List<? extends Object> resources, final int page, final String baseQuery, final int ps,
            final int pc) {
        if (page >= pc) {
            throw new IllegalArgumentException("not so many pages: " + page);
        }
        this.page = page;
        this.size = resources.size();
        this.baseQuery = baseQuery;
        this.pageSize = this.size / pc;
        if (this.size % pc != 0 ) {
            this.pageSize += 1;
        }
        if (this.pageSize < ps) {
            this.pageSize = ps;
        }
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
