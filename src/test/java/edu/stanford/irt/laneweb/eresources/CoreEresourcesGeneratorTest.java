package edu.stanford.irt.laneweb.eresources;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;

import edu.stanford.irt.cocoon.xml.SAXStrategy;
import edu.stanford.irt.eresources.CollectionManager;
import edu.stanford.irt.laneweb.LanewebException;

public class CoreEresourcesGeneratorTest {

    private CollectionManager collectionManager;

    private CoreEresourcesGenerator generator;

    private SAXStrategy<PagingEresourceList> saxStrategy;

    @SuppressWarnings("unchecked")
    @Before
    public void setUp() throws Exception {
        this.collectionManager = createMock(CollectionManager.class);
        this.saxStrategy = createMock(SAXStrategy.class);
        this.generator = new CoreEresourcesGenerator("type", this.collectionManager, this.saxStrategy);
    }

    @Test
    public void testGetEresourceList() {
        this.generator.type = "type";
        expect(this.collectionManager.getCore("type")).andReturn(null);
        replay(this.collectionManager);
        this.generator.getEresourceList();
        verify(this.collectionManager);
    }

    @Test
    // TODO: return empty collection
    public void testGetEresourceListNullType() {
        try {
            this.generator.getEresourceList();
            fail();
        } catch (LanewebException e) {
        }
    }
}
