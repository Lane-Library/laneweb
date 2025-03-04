package edu.stanford.irt.laneweb.eresources.search;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import org.junit.jupiter.api.Test;
import edu.stanford.irt.laneweb.eresources.model.solr.Field;
import edu.stanford.irt.laneweb.eresources.model.solr.FacetFieldEntry;

public class FacetComparatorTest {

    private static final Collection<String> PUB_TYPES = Arrays.asList("Req Pub 1", "Req Pub 2", "Req Pub 3");

    private static final String TOP_PRIORITY_FACET = "topPriority:Number One";

    @Test
    public final void testComparator() {
        FacetComparator comparator = new FacetComparator(PUB_TYPES);
        comparator.addTopPriority(TOP_PRIORITY_FACET);
        Set<FacetFieldEntry> set = new TreeSet<>(comparator);
        Iterator<FacetFieldEntry> i;
        // // basic order by count
        Field fieldName = new Field("fieldName");
        set.add(new FacetFieldEntry(fieldName, "value", 1));
        set.add(new FacetFieldEntry(fieldName, "value", 0));
        i = set.iterator();
        assertEquals(1, i.next().getValueCount());
        assertEquals(0, i.next().getValueCount());
        set.clear();
        // order by index when counts are equal
        set.add(new FacetFieldEntry(fieldName, "value2", 1));
        set.add(new FacetFieldEntry(fieldName, "value1", 1));
        i = set.iterator();
        assertEquals("value1", i.next().getValue());
        assertEquals("value2", i.next().getValue());
        set.clear();
        //// // case 110125: Have article type display 3 items at all times (even if
        //// results are 0)
        Field publicationType = new Field("publicationType");
        set.add(new FacetFieldEntry(fieldName, "foo", 10));
        set.add(new FacetFieldEntry(publicationType, "Req Pub 2", 0));
        set.add(new FacetFieldEntry(publicationType, "Req Pub 3", 0));
        set.add(new FacetFieldEntry(publicationType, "Req Pub 1", 0));
        i = set.iterator();
        assertEquals("Req Pub 1", i.next().getValue());
        assertEquals("Req Pub 2", i.next().getValue());
        assertEquals("Req Pub 3", i.next().getValue());
        assertEquals("foo", i.next().getValue());
        set.clear();
        set.add(new FacetFieldEntry(fieldName, "foo", 100));
        set.add(new FacetFieldEntry(publicationType, "Req Pub 1", 5));
        set.add(new FacetFieldEntry(publicationType, "Req Pub 2", 0));
        set.add(new FacetFieldEntry(publicationType, "Req Pub 3", 10));
        i = set.iterator();
        assertEquals("Req Pub 3", i.next().getValue());
        assertEquals("Req Pub 1", i.next().getValue());
        assertEquals("Req Pub 2", i.next().getValue());
        assertEquals("foo", i.next().getValue());

        set.clear();
        Field topPriority = new Field("topPriority");
        set.add(new FacetFieldEntry(fieldName, "foo", 100));
        set.add(new FacetFieldEntry(publicationType, "Req Pub 1", 10));
        set.add(new FacetFieldEntry(topPriority, "Number One", 1));
        i = set.iterator();
        assertEquals("Number One", i.next().getValue());
        assertEquals("Req Pub 1", i.next().getValue());
        assertEquals("foo", i.next().getValue());
    }

    @Test
    public final void testComparatorWithAddTopPrioritiesFromFacets() {
        FacetComparator comparator = new FacetComparator(PUB_TYPES);
        comparator.addTopPrioritiesFromFacets("type:\"Book\"::type:\"Pictorial\"");
        Set<FacetFieldEntry> set = new TreeSet<>(comparator);
        Iterator<FacetFieldEntry> i;
        Field fieldName = new Field("fieldName");
        Field fieldType = new Field("type");
        set.add(new FacetFieldEntry(fieldName, "value1", 1));
        set.add(new FacetFieldEntry(fieldName, "value2", 100));
        set.add(new FacetFieldEntry(fieldType, "Pictorial", 10));
        set.add(new FacetFieldEntry(fieldType, "Book", 50));
        i = set.iterator();
        assertEquals("Book", i.next().getValue());
        assertEquals("Pictorial", i.next().getValue());
        assertEquals("value2", i.next().getValue());
        assertEquals("value1", i.next().getValue());
    }

    @Test
    public final void testException() {
        Set<FacetFieldEntry> set = new TreeSet<>(new FacetComparator(PUB_TYPES));
        Field fieldName = new Field("fieldName");
        set.add(new FacetFieldEntry(fieldName, "value", 1));
        assertThrows(IllegalArgumentException.class, () -> {
            set.add(null);
        });
    }
}
