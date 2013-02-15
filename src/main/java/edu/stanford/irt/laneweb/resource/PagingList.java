package edu.stanford.irt.laneweb.resource;

import java.util.Collection;
import java.util.LinkedList;


public class PagingList<E extends Object> extends LinkedList<E> {
    
    private static final long serialVersionUID = 1L;
    
    private PagingData pagingData;

    public PagingList(final Collection<E> collection, final PagingData pagingData) {
        super(collection);
        this.pagingData = pagingData;
    }
    
    public PagingData getPagingData() {
        return this.pagingData;
    }
}
