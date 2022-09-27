package edu.stanford.irt.laneweb.eresources.search;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import org.junit.Test;
import org.springframework.data.solr.core.query.SimpleField;
import org.springframework.data.solr.core.query.result.FacetFieldEntry;
import org.springframework.data.solr.core.query.result.SimpleFacetFieldEntry;

public class FacetComparatorTest {

    private static final Collection<String> PUB_TYPES = Arrays.asList("Req Pub 1", "Req Pub 2", "Req Pub 3");

    
    private static final String TOP_PRIORITY_FACET = "topPriority:Number One";
    
    
    @Test
    public final void testComparator() {
        FacetComparator comparator = new FacetComparator(PUB_TYPES);
        comparator.addTopPriority(TOP_PRIORITY_FACET);
        Set<FacetFieldEntry> set = new TreeSet<>(comparator);
        Iterator<FacetFieldEntry> i;
//        // basic order by count
        SimpleField fieldName = new SimpleField( "fieldName");
        set.add(new SimpleFacetFieldEntry( fieldName, "value", 1));
        set.add(new SimpleFacetFieldEntry(fieldName, "value", 0));
        i = set.iterator();
        assertEquals(1, i.next().getValueCount());
        assertEquals(0, i.next().getValueCount());
        set.clear();
        // order by index when counts are equal
        set.add(new SimpleFacetFieldEntry(fieldName, "value2", 1));
        set.add(new SimpleFacetFieldEntry(fieldName, "value1", 1));
        i = set.iterator();
        assertEquals("value1", i.next().getValue());
        assertEquals("value2", i.next().getValue());
        set.clear();
////        // case 110125: Have article type display 3 items at all times (even if results are 0)
        SimpleField publicationType = new SimpleField( "publicationType");
        set.add(new SimpleFacetFieldEntry(fieldName, "foo", 10));
        set.add(new SimpleFacetFieldEntry(publicationType, "Req Pub 2", 0));
        set.add(new SimpleFacetFieldEntry(publicationType, "Req Pub 3", 0));
        set.add(new SimpleFacetFieldEntry(publicationType, "Req Pub 1", 0));
        i = set.iterator();
        assertEquals("Req Pub 1", i.next().getValue());
        assertEquals("Req Pub 2", i.next().getValue());
        assertEquals("Req Pub 3", i.next().getValue());
        assertEquals("foo", i.next().getValue());
        set.clear();
        set.add(new SimpleFacetFieldEntry(fieldName, "foo", 100));
        set.add(new SimpleFacetFieldEntry(publicationType, "Req Pub 1", 5));
        set.add(new SimpleFacetFieldEntry(publicationType, "Req Pub 2", 0));
        set.add(new SimpleFacetFieldEntry(publicationType, "Req Pub 3", 10));
        i = set.iterator();
        assertEquals("Req Pub 3", i.next().getValue());
        assertEquals("Req Pub 1", i.next().getValue());
        assertEquals("Req Pub 2", i.next().getValue());
        assertEquals("foo", i.next().getValue());

        set.clear();
        SimpleField topPriority = new SimpleField( "topPriority");
        set.add(new SimpleFacetFieldEntry(fieldName, "foo", 100)); 
        set.add(new SimpleFacetFieldEntry(publicationType, "Req Pub 1", 10));
        set.add(new SimpleFacetFieldEntry(topPriority, "Number One", 1));
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
        SimpleField fieldName = new SimpleField( "fieldName");
        SimpleField fieldType = new SimpleField( "type");
        set.add(new SimpleFacetFieldEntry(fieldName, "value1", 1));
        set.add(new SimpleFacetFieldEntry(fieldName, "value2", 100));
        set.add(new SimpleFacetFieldEntry(fieldType, "Pictorial", 10));
        set.add(new SimpleFacetFieldEntry(fieldType, "Book", 50));
        i = set.iterator();
        assertEquals("Book", i.next().getValue());
        assertEquals("Pictorial", i.next().getValue());
        assertEquals("value2", i.next().getValue());
        assertEquals("value1", i.next().getValue());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public final void testException() {
        Set<SimpleFacetFieldEntry> set = new TreeSet<>(new FacetComparator(PUB_TYPES));
        SimpleField fieldName = new SimpleField( "fieldName");
        set.add(new SimpleFacetFieldEntry(fieldName, "value", 1));
        set.add(null);
    }
}
