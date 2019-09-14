package edu.stanford.irt.laneweb.images;

import java.io.Serializable;
import java.util.Comparator;

import org.springframework.data.solr.core.query.result.FacetFieldEntry;

public class FacetFieldComparator implements Comparator<FacetFieldEntry>, Serializable {

    /** for Serializable. */
    private static final long serialVersionUID = 1L;

    @Override
    public int compare(final FacetFieldEntry facet1, final FacetFieldEntry facet2) {
        int copyrightValue1 = Integer.parseInt(facet1.getValue());
        int copyrightValue2 = Integer.parseInt(facet2.getValue());
        if (copyrightValue1 > copyrightValue2) {
            return 1;
        }
        return -1;
    }
}
