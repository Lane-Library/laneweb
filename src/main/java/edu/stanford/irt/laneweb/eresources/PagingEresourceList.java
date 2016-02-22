package edu.stanford.irt.laneweb.eresources;

import java.util.Collection;

import edu.stanford.irt.laneweb.resource.PagingData;
import edu.stanford.irt.laneweb.resource.PagingList;
import edu.stanford.irt.laneweb.solr.Eresource;

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
