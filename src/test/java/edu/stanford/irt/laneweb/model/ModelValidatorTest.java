package edu.stanford.irt.laneweb.model;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class ModelValidatorTest {

    private ModelValidator validator;

    @Before
    public void setUp() {
        this.validator = new ModelValidator();
    }

    @Test
    public void testIsValid() {
        assertTrue(this.validator.isValid(null, null));
    }
}
