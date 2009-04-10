package edu.stanford.irt.laneweb.mesh;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class MeshSuggestComparatorTest {

    @Test
    public void testCompare() {
        MeshSuggestComparator comparator = new MeshSuggestComparator("TestString");
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
    }
}
