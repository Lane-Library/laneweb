package edu.stanford.irt.laneweb.config;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

public class SuggestConfigurationTest {

    private SuggestConfiguration configuration;

    @Before
    public void setUp() {
        this.configuration = new SuggestConfiguration();
    }

    @Test
    public void testEresourceSuggestionManager() {
        assertNotNull(this.configuration.eresourceSuggestionManager(null));
    }

    @Test
    public void testExtensionsSuggestionManager() {
        assertNotNull(this.configuration.extensionsSuggestionManager(null, null));
    }

    @Test
    public void testMeshSuggestionManager() {
        assertNotNull(this.configuration.meshSuggestionManager());
    }
}
