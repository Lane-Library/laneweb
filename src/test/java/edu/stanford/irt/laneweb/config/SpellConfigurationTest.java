package edu.stanford.irt.laneweb.config;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

public class SpellConfigurationTest {

    private SpellConfiguration configuration;

    @Before
    public void setUp() {
        this.configuration = new SpellConfiguration();
    }

    @Test
    public void testSpellChecker() {
        assertNotNull(this.configuration.spellChecker());
    }
}
