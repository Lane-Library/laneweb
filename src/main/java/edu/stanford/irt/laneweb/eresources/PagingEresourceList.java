package edu.stanford.irt.laneweb.eresources;

import java.util.Collection;
import java.util.LinkedList;

import edu.stanford.irt.eresources.Eresource;

public class PagingEresourceList extends LinkedList<Eresource> {

    private static final int DEFAULT_PAGE_SIZE = 100;

    private static final int MAX_PAGE_COUNT = 4;

    private static final long serialVersionUID = 1L;

    private int length;

    private int page;

    private int pages;

    private int start;

    public PagingEresourceList(final Collection<Eresource> eresources) {
        this(eresources, 0);
    }

    public PagingEresourceList(final Collection<Eresource> eresources, final int page) {
        super(eresources);
        if (page >= MAX_PAGE_COUNT) {
            throw new IllegalArgumentException("not so many pages: " + page);
        }
        this.page = page;
        int size = size();
        int pageSize = size / MAX_PAGE_COUNT;
        pageSize = size % MAX_PAGE_COUNT != 0 ? pageSize + 1 : pageSize;
        pageSize = pageSize < DEFAULT_PAGE_SIZE ? DEFAULT_PAGE_SIZE : pageSize;
        if (page < 0 || size <= pageSize) {
            this.start = 0;
            this.length = size;
        } else {
            this.start = page * pageSize;
            this.length = size - this.start < pageSize ? size - this.start : pageSize;
        }
        this.pages = size / pageSize;
        this.pages = size % pageSize != 0 ? this.pages + 1 : this.pages;
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

    public int getStart() {
        return this.start;
    }
}
