package edu.stanford.irt.laneweb.eresources.browse;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.mock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import edu.stanford.irt.laneweb.eresources.model.Eresource;

public class EresourceListPagingDataTest {

    private Eresource eresource;

    private EresourceListPagingData pagingData;

    @Before
    public void setUp() throws Exception {
        this.eresource = mock(Eresource.class);
        List<Eresource> list = Arrays.asList(new Eresource[256]);
        Collections.fill(list, this.eresource);
        this.pagingData = new EresourceListPagingData(list, 0, "", "a");
    }

    @Test
    public void testGetAlpha() {
        assertEquals("a", this.pagingData.getAlpha());
    }

    @Test
    public void testGetPagingLabels() {
        expect(this.eresource.getTitle()).andReturn("er title").times(6);
        replay(this.eresource);
        List<PagingLabel> labels = this.pagingData.getPagingLabels();
        assertEquals(3, labels.size());
        assertEquals(100, labels.get(0).getResults());
        assertEquals(100, labels.get(1).getResults());
        assertEquals(56, labels.get(2).getResults());
        for (PagingLabel pagingLabel : labels) {
            assertEquals("er title", pagingLabel.getStart());
            assertEquals("er title", pagingLabel.getEnd());
        }
        verify(this.eresource);
    }
}
