package edu.stanford.irt.laneweb.suggest;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

import edu.stanford.irt.suggest.Suggestion;

public class SuggestionComparatorTest {

    @Test
    public void testCompare() {
        SuggestionComparator comparator = new SuggestionComparator("TestString");
        assertTrue(comparator.compare(new Suggestion("", "TestString foo"), new Suggestion("", "bar")) < 0);
        assertTrue(comparator.compare(new Suggestion("", "TestString foo"), new Suggestion("", "foo TestString")) < 0);
        assertTrue(comparator.compare(new Suggestion("", "foo TestString"), new Suggestion("", "bar")) < 0);
        assertTrue(comparator.compare(new Suggestion("", "bar"), new Suggestion("", "TestString foo")) > 0);
        assertTrue(comparator.compare(new Suggestion("", "foo TestString"), new Suggestion("", "TestString foo")) > 0);
        assertTrue(comparator.compare(new Suggestion("", "bar"), new Suggestion("", "foo TestString")) > 0);
        assertTrue(comparator.compare(new Suggestion("", "aaa"), new Suggestion("", "bbb")) < 0);
        assertTrue(comparator.compare(new Suggestion("", "bbb"), new Suggestion("", "aaa")) > 0);
        assertTrue(comparator.compare(new Suggestion("", "aaa"), new Suggestion("", "aaa")) == 0);
        assertTrue(comparator.compare(new Suggestion("", "foo TestString"), new Suggestion("", "foo TestString")) == 0);
        assertTrue(comparator.compare(new Suggestion("", "Zeitschrift für allgemeine Mikrobiologie"), new Suggestion("",
                "zeitschrift fur allgemeine Mikrobiologie")) == 0);
    }

    @Test
    public void testNullParameter() {
        SuggestionComparator comparator = new SuggestionComparator("");
        try {
            comparator.compare(new Suggestion("", ""), null);
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
