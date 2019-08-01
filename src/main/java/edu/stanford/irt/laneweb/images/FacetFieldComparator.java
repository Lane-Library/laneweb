package edu.stanford.irt.laneweb.images;

import java.util.Comparator;

import org.springframework.data.solr.core.query.result.FacetFieldEntry;

public class FacetFieldComparator implements Comparator<FacetFieldEntry> {

	@Override
	public int compare(FacetFieldEntry facet1, FacetFieldEntry facet2) {
		int copyrightValue1 = Integer.valueOf(facet1.getValue());
		int copyrightValue2 = Integer.valueOf(facet2.getValue());
		if (copyrightValue1 > copyrightValue2) {
			return 1;
		} else {
			return -1;
		}
	}

}
