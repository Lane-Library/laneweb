package edu.stanford.irt.laneweb.eresources.browse;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class TitleNormalizerTest {
    private void assertTitleCase(String input, String expected) {
        assertEquals(expected, TitleNormalizer.toTitleCase(input));
    }

    @Test
    public void testToTitleCase() {
        assertTitleCase(null, null);
        assertTitleCase("", "");
        assertTitleCase("e-Anatomy", "e-Anatomy");
        assertTitleCase("The Atlantic.", "The Atlantic");
        assertTitleCase("NEJM AI.", "NEJM AI");
        assertTitleCase("Red book (American Academy of Pediatrics)", "Red Book (American Academy of Pediatrics)");
        assertTitleCase("New York times (National edition)", "New York Times (National Edition)");
        assertTitleCase("The New York times", "The New York Times");
        assertTitleCase("New England journal of medicine", "New England Journal of Medicine");
    }

}
