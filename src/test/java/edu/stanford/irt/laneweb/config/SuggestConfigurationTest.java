package edu.stanford.irt.laneweb.config;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

public class SuggestConfigurationTest {

    private SuggestConfiguration configuration;

    @Before
    public void setUp() {
        this.configuration = new SuggestConfiguration(null);
    }

    @Test
    public void testEresourceSuggestionManager() {
        assertNotNull(this.configuration.eresourceSuggestionManager());
    }

    @Test
    public void testExtensionsSuggestionManager() {
        assertNotNull(this.configuration.extensionsSuggestionManager());
    }

    @Test
    public void testMeshSuggestionManager() {
        assertNotNull(this.configuration.meshSuggestionManager());
    }
}
