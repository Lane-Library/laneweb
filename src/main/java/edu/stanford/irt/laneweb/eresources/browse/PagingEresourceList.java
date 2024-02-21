package edu.stanford.irt.laneweb.eresources.browse;

import java.util.Collection;

import edu.stanford.irt.laneweb.eresources.model.Eresource;
import edu.stanford.irt.laneweb.resource.PagingData;
import edu.stanford.irt.laneweb.resource.PagingList;

public class PagingEresourceList extends PagingList<Eresource> {

    private static final long serialVersionUID = 1L;

    private String heading;

    public PagingEresourceList(final Collection<Eresource> eresources, final PagingData pagingData,
            final String heading) {
        super(eresources, pagingData);
        this.heading = heading;
    }

    public String getHeading() {
        return this.heading;
    }
}
