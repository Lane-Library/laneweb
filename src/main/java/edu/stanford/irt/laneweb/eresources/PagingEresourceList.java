package edu.stanford.irt.laneweb.eresources;

import java.util.Collection;

import edu.stanford.irt.laneweb.resource.PagingData;
import edu.stanford.irt.laneweb.resource.PagingList;

public class PagingEresourceList extends PagingList<Eresource> {

    private static final long serialVersionUID = 1L;

    public PagingEresourceList(final Collection<Eresource> eresources, final PagingData pagingData) {
        super(eresources, pagingData);
    }
}
