package edu.stanford.irt.laneweb.images;

import static org.junit.Assert.assertEquals;

import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import org.junit.Test;
import org.springframework.data.solr.core.query.SimpleField;
import org.springframework.data.solr.core.query.result.FacetFieldEntry;
import org.springframework.data.solr.core.query.result.SimpleFacetFieldEntry;

public class FacetFieldComparatorTest {

    @Test
    public final void testCompare() {
        FacetFieldEntry ffe1 = new SimpleFacetFieldEntry(new SimpleField("foo"), "10", 1);
        FacetFieldEntry ffe2 = new SimpleFacetFieldEntry(new SimpleField("foo"), "5", 1);
        Set<FacetFieldEntry> set = new TreeSet<>(new FacetFieldComparator());
        set.add(ffe2);
        set.add(ffe1);
        Iterator<FacetFieldEntry> i = set.iterator();
        assertEquals(ffe2, i.next());
        assertEquals(ffe1, i.next());
    }
}
