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
        assertEquals("Aortic Stenosis Subvalvular", this.voyagerMeshSuggest.filterQuery("\"Aortic-Stenosis, Subvalvular\""));
        assertEquals("Anti Inflammatory Agents Non Steroidal", this.voyagerMeshSuggest.filterQuery("Anti-Inflammatory Agents, Non-Steroidal"));
    }

}
