package edu.stanford.irt.laneweb;

import static org.junit.Assert.assertFalse;

import java.io.IOException;

import org.junit.Test;

import jdepend.framework.JDepend;

public class PackageCycleTest {

    @Test
    public void testCycles() throws IOException {
        JDepend jdepend = new JDepend();
        jdepend.addDirectory("target/classes");
        jdepend.analyze();
        assertFalse(jdepend.containsCycles());
    }
}
