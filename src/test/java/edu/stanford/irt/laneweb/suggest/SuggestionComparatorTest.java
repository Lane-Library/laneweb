package edu.stanford.irt.laneweb.suggest;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

public class SuggestionComparatorTest {

    @Test
    public void testCompare() {
        SuggestionComparator comparator = new SuggestionComparator("TestString");
        assertTrue(comparator.compare("TestString foo", "bar") < 0);
        assertTrue(comparator.compare("TestString foo", "foo TestString") < 0);
        assertTrue(comparator.compare("foo TestString", "bar") < 0);
        assertTrue(comparator.compare("bar", "TestString foo") > 0);
        assertTrue(comparator.compare("foo TestString", "TestString foo") > 0);
        assertTrue(comparator.compare("bar", "foo TestString") > 0);
        assertTrue(comparator.compare("aaa", "bbb") < 0);
        assertTrue(comparator.compare("bbb", "aaa") > 0);
        assertTrue(comparator.compare("aaa", "aaa") == 0);
        assertTrue(comparator.compare("foo TestString", "foo TestString") == 0);
        assertTrue(comparator.compare("Zeitschrift für allgemeine Mikrobiologie", "zeitschrift fur allgemeine Mikrobiologie") == 0);
    }

    @Test
    public void testNullParameter() {
        SuggestionComparator comparator = new SuggestionComparator("");
        try {
            comparator.compare("", null);
            fail();
        } catch (IllegalArgumentException e) {
        }
    }

    @Test
    public void testNullQuery() {
        try {
            new SuggestionComparator(null);
            fail();
        } catch (IllegalArgumentException e) {
        }
    }
}
