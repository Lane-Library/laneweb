package edu.stanford.irt.laneweb.servlet.mvc;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import edu.stanford.irt.laneweb.search.MetaSearchManagerSource;

public class SearchContextReloaderControllerTest {

    private SearchContextReloaderController controller;

    private MetaSearchManagerSource msms;

    @Before
    public void setUp() throws Exception {
        this.msms = createMock(MetaSearchManagerSource.class);
        this.controller = new SearchContextReloaderController(this.msms);
    }

    @Test
    public void testReloadContext() throws IOException {
        this.msms
                .reload("https://irt-svn.stanford.edu/repos/irt/lane/search/tags/search-lane-release/src/main/resources/search-lane.xml",
                        "sunetid", "password");
        replay(this.msms);
        assertEquals("redirect:/reloadresources.html", this.controller.reloadContext("release", "sunetid", "password"));
        verify(this.msms);
    }
}
