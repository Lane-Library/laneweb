package edu.stanford.irt.laneweb.solr;

import static org.junit.Assert.assertEquals;

import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import org.junit.Test;

public class FacetComparatorTest {

    @Test
    public final void test() {
        Set<Facet> set = new TreeSet<Facet>(new FacetComparator());
        Iterator<Facet> i;
        // basic order by count
        set.add(new Facet("fieldName", "value", 1, null));
        set.add(new Facet("fieldName", "value", 0, null));
        i = set.iterator();
        assertEquals(1, i.next().getCount());
        assertEquals(0, i.next().getCount());
        set.clear();
        // order by value when counts are equal
        set.add(new Facet("fieldName", "value2", 1, null));
        set.add(new Facet("fieldName", "value1", 1, null));
        i = set.iterator();
        assertEquals("value1", i.next().getValue());
        assertEquals("value2", i.next().getValue());
        set.clear();
        // case 110630: Move 5 years to be higher then last 10 years within Year filter
        set.add(new Facet("fieldName", "year:[xx TO yy]", 1, null));
        set.add(new Facet("fieldName", "year:[yy TO zz]", 1, null));
        i = set.iterator();
        assertEquals("year:[yy TO zz]", i.next().getValue());
        assertEquals("year:[xx TO yy]", i.next().getValue());
        set.clear();
        // case 110125: Have article type display 3 items at all times (even if results are 0)
        set.add(new Facet("fieldName", "foo", 10, null));
        set.add(new Facet("fieldName", "Clinical Trial", 0, null));
        set.add(new Facet("fieldName", "Systematic Review", 0, null));
        set.add(new Facet("fieldName", "Randomized Controlled Trial", 0, null));
        i = set.iterator();
        assertEquals("Clinical Trial", i.next().getValue());
        assertEquals("Randomized Controlled Trial", i.next().getValue());
        assertEquals("Systematic Review", i.next().getValue());
        assertEquals("foo", i.next().getValue());
        set.clear();
        set.add(new Facet("fieldName", "foo", 10, null));
        set.add(new Facet("fieldName", "Clinical Trial", 5, null));
        set.add(new Facet("fieldName", "Systematic Review", 0, null));
        set.add(new Facet("fieldName", "Randomized Controlled Trial", 10, null));
        i = set.iterator();
        assertEquals("Randomized Controlled Trial", i.next().getValue());
        assertEquals("Clinical Trial", i.next().getValue());
        assertEquals("Systematic Review", i.next().getValue());
        assertEquals("foo", i.next().getValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public final void testException() {
        Set<Facet> set = new TreeSet<Facet>(new FacetComparator());
        set.add(new Facet("fieldName", "value", 1, null));
        set.add(null);
    }
}
