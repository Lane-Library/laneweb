package edu.stanford.irt.laneweb.eresources.search;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import org.junit.Test;

public class FacetComparatorTest {

    private static final Collection<String> PUB_TYPES = Arrays.asList("Req Pub 1", "Req Pub 2", "Req Pub 3");

    @Test
    public final void test() {
        Set<Facet> set = new TreeSet<Facet>(new FacetComparator(PUB_TYPES));
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
        set.add(new Facet("date", "[20100101 TO *]", 1, null));
        set.add(new Facet("date", "[20050101 TO *]", 10, null));
        i = set.iterator();
        assertEquals("[20100101 TO *]", i.next().getValue());
        assertEquals("[20050101 TO *]", i.next().getValue());
        set.clear();
        // case 110125: Have article type display 3 items at all times (even if results are 0)
        set.add(new Facet("publicationType", "foo", 10, null));
        set.add(new Facet("publicationType", "Req Pub 2", 0, null));
        set.add(new Facet("publicationType", "Req Pub 3", 0, null));
        set.add(new Facet("publicationType", "Req Pub 1", 0, null));
        i = set.iterator();
        assertEquals("Req Pub 1", i.next().getValue());
        assertEquals("Req Pub 2", i.next().getValue());
        assertEquals("Req Pub 3", i.next().getValue());
        assertEquals("foo", i.next().getValue());
        set.clear();
        set.add(new Facet("publicationType", "foo", 10, null));
        set.add(new Facet("publicationType", "Req Pub 1", 5, null));
        set.add(new Facet("publicationType", "Req Pub 2", 0, null));
        set.add(new Facet("publicationType", "Req Pub 3", 10, null));
        i = set.iterator();
        assertEquals("Req Pub 3", i.next().getValue());
        assertEquals("Req Pub 1", i.next().getValue());
        assertEquals("Req Pub 2", i.next().getValue());
        assertEquals("foo", i.next().getValue());
        set.clear();
        // case 131450: indent Digital and Print for Book and Journal types
        set.add(new Facet("type", "Book Print", 100, null));
        set.add(new Facet("type", "Book", 10, null));
        set.add(new Facet("type", "Book Digital", 1000, null));
        i = set.iterator();
        assertEquals("Book", i.next().getValue());
        assertEquals("Book Digital", i.next().getValue());
        assertEquals("Book Print", i.next().getValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public final void testException() {
        Set<Facet> set = new TreeSet<Facet>(new FacetComparator(PUB_TYPES));
        set.add(new Facet("fieldName", "value", 1, null));
        set.add(null);
    }
}
