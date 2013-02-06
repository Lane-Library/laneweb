package edu.stanford.irt.laneweb.eresources;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import edu.stanford.irt.eresources.Eresource;

public class PagingEresourceList extends LinkedList<Eresource> {

    private static final int DEFAULT_PAGE_SIZE = 100;

    private static final int MAX_PAGE_COUNT = 4;

    private static final long serialVersionUID = 1L;

    private int length;

    private int page;

    private int pages;

    private int pageSize;

    private List<PagingLabel> pagingLabels;

    private int size;

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
        this.size = size();
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

    public int getLength() {
        return this.length;
    }

    public int getPage() {
        return this.page;
    }

    public int getPages() {
        return this.pages;
    }

    public List<PagingLabel> getPagingLabels() {
        if (null != this.pagingLabels) {
            return this.pagingLabels;
        }
        this.pagingLabels = new LinkedList<PagingLabel>();
        for (int i = 0; i < this.pages && this.page >= 0; i++) {
            int pageLabelStart;
            int pageLabelEnd;
            int numResults;
            if (i == 0) {
                pageLabelStart = 0;
                pageLabelEnd = this.pageSize - 1;
            } else {
                pageLabelStart = i * this.pageSize;
                pageLabelEnd = ((i + 1) * this.pageSize) - 1;
            }
            pageLabelEnd = pageLabelEnd >= this.size ? this.size - 1 : pageLabelEnd;
            numResults = (pageLabelEnd - pageLabelStart) + 1;
            this.pagingLabels.add(new PagingLabel(this.get(pageLabelStart).getTitle(), this.get(pageLabelEnd)
                    .getTitle(), numResults));
        }
        return this.pagingLabels;
    }

    public int getStart() {
        return this.start;
    }
}
