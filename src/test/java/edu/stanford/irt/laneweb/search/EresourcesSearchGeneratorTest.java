package edu.stanford.irt.laneweb.search;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import java.util.Collections;

import org.junit.Before;
import org.junit.Test;

import edu.stanford.irt.cocoon.xml.SAXStrategy;
import edu.stanford.irt.eresources.CollectionManager;
import edu.stanford.irt.eresources.Eresource;
import edu.stanford.irt.laneweb.model.Model;

public class EresourcesSearchGeneratorTest {

    private CollectionManager collectionManager;

    private Eresource eresource;

    private EresourcesSearchGenerator generator;

    private SAXStrategy<PagingSearchResultList> saxStrategy;

    @SuppressWarnings("unchecked")
    @Before
    public void setUp() throws Exception {
        this.collectionManager = createMock(CollectionManager.class);
        this.saxStrategy = createMock(SAXStrategy.class);
        this.generator = new EresourcesSearchGenerator(this.collectionManager, this.saxStrategy);
        this.eresource = createMock(Eresource.class);
    }

    @Test
    public void testGetSearchResults() {
        expect(this.collectionManager.search("query")).andReturn(Collections.<Eresource> singletonList(this.eresource));
        expect(this.eresource.getTitle()).andReturn("title");
//        expect(this.eresource.getScore()).andReturn(0).times(2);
        replay(this.collectionManager, this.saxStrategy, this.eresource);
        this.generator.doSearch("query");
        verify(this.collectionManager, this.saxStrategy, this.eresource);
    }

    @Test
    public void testGetSearchResultsModelParametersType() {
        expect(this.collectionManager.searchType("parameterType", "query")).andReturn(
                Collections.<Eresource> singletonList(this.eresource));
        expect(this.eresource.getTitle()).andReturn("title");
//        expect(this.eresource.getScore()).andReturn(0).times(2);
        replay(this.collectionManager, this.saxStrategy, this.eresource);
        this.generator.setModel(Collections.<String, Object> singletonMap(Model.TYPE, "modelType"));
        this.generator.setParameters(Collections.<String, String> singletonMap(Model.TYPE, "parameterType"));
        this.generator.doSearch("query");
        verify(this.collectionManager, this.saxStrategy, this.eresource);
    }

    @Test
    public void testGetSearchResultsModelType() {
        expect(this.collectionManager.searchType("type", "query")).andReturn(Collections.<Eresource> singletonList(this.eresource));
        expect(this.eresource.getTitle()).andReturn("title");
//        expect(this.eresource.getScore()).andReturn(0).times(2);
        replay(this.collectionManager, this.saxStrategy, this.eresource);
        this.generator.setModel(Collections.<String, Object> singletonMap(Model.TYPE, "type"));
        this.generator.setParameters(Collections.<String, String> emptyMap());
        this.generator.doSearch("query");
        verify(this.collectionManager, this.saxStrategy, this.eresource);
    }

    @Test
    public void testGetSearchResultsParametersType() {
        expect(this.collectionManager.searchType("type", "query")).andReturn(Collections.<Eresource> singletonList(this.eresource));
        expect(this.eresource.getTitle()).andReturn("title");
//        expect(this.eresource.getScore()).andReturn(0).times(2);
        replay(this.collectionManager, this.saxStrategy, this.eresource);
        this.generator.setParameters(Collections.<String, String> singletonMap(Model.TYPE, "type"));
        this.generator.doSearch("query");
        verify(this.collectionManager, this.saxStrategy, this.eresource);
    }
}
