package edu.stanford.irt.laneweb.config;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class SuggestConfigurationTest {

    private SuggestConfiguration configuration;

    @BeforeEach
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
