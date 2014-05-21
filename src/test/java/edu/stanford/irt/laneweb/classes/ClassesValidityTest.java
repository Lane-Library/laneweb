package edu.stanford.irt.laneweb.classes;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Calendar;

import org.junit.Test;

public class ClassesValidityTest {

    @Test
    public void testIsValid() {
        assertTrue(new ClassesValidity().isValid());
    }

    @Test
    public void testIsValidExpiredCurrentHour() throws InterruptedException {
        ClassesValidity v = new ClassesValidity(Calendar.getInstance().get(Calendar.HOUR_OF_DAY), 1);
        Thread.sleep(10);
        assertFalse(v.isValid());
    }

    @Test
    public void testIsValidExpiredNotHour() throws InterruptedException {
        ClassesValidity v = new ClassesValidity(Calendar.getInstance().get(Calendar.HOUR_OF_DAY) - 1, 1);
        Thread.sleep(10);
        assertTrue(v.isValid());
    }
}
