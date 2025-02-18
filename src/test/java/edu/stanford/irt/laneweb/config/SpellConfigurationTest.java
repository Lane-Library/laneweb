package edu.stanford.irt.laneweb.config;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class SpellConfigurationTest {

    private SpellConfiguration configuration;

    @BeforeEach
    public void setUp() {
        this.configuration = new SpellConfiguration();
    }

    @Test
    public void testSpellChecker() {
        assertNotNull(this.configuration.spellChecker());
    }
}
