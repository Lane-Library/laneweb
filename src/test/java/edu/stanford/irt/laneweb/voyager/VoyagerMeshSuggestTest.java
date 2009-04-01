package edu.stanford.irt.laneweb.voyager;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class VoyagerMeshSuggestTest {

    private VoyagerMeshSuggest voyagerMeshSuggest;

    @Before
    public void setUp() throws Exception {
        this.voyagerMeshSuggest = new VoyagerMeshSuggest();
    }

    @Test
    public void testFilterQuery() {
        assertEquals("AORTIC STENOSIS SUBVALVULAR", this.voyagerMeshSuggest.filterQuery("\"Aortic-Stenosis, Subvalvular\""));
        assertEquals("ANTI INFLAMMATORY AGENTS NON STEROIDAL", this.voyagerMeshSuggest.filterQuery("Anti-Inflammatory Agents, Non-Steroidal"));
    }

}
