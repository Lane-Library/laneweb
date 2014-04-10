package edu.stanford.irt.laneweb.search;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import edu.stanford.irt.laneweb.bassett.BassettCollectionManager;
import edu.stanford.irt.laneweb.bassett.BassettImage;
import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.search.Query;
import edu.stanford.irt.search.impl.MetaSearchManager;
import edu.stanford.irt.search.impl.Result;

public class ImageSearchGeneratorTest {

	private ImageSearchGenerator generator;
	
	private MetaSearchManager metasearchManager;
    
    private BassettCollectionManager bassettManager;

    private ImageSearchSAXStrategy saxStrategy;
    
    private Result result;

    private List<BassettImage> bassettResult =new ArrayList<BassettImage>();
    
	
	@Before
	public void setUp() throws Exception {
	   	this.metasearchManager = createMock(MetaSearchManager.class);
	   	this.bassettManager = createMock(BassettCollectionManager.class);
        this.saxStrategy = createMock(ImageSearchSAXStrategy.class);
        this.generator = new ImageSearchGenerator(this.metasearchManager, this.bassettManager, this.saxStrategy);
        this.bassettResult.add(new BassettImage("description", "title"));
        this.result = createMock(Result.class);
    }
	
	
	@Test
    public void testDoSearch() {
		expect(this.bassettManager.search("query")).andReturn((List<BassettImage>) this.bassettResult);
        expect(this.metasearchManager.search(isA(Query.class), eq(20000L), eq(true))).andReturn(this.result);
        replay(this.bassettManager, this.metasearchManager, this.saxStrategy, this.result);
        Map<String, Object> model = new HashMap<String, Object>();
        model.put(Model.QUERY, "query");
        model.put(Model.ENGINES, Collections.singleton("engine-2"));
        this.generator.setParameters(Collections.<String, String> emptyMap());
        this.generator.setModel(model);
        HashMap<String, Object> resultMap = this.generator.doSearch("query");
        assertNotNull(resultMap.get(ImageSearchGenerator.BASSETT_RESULT));
        assertNotNull(resultMap.get(ImageSearchGenerator.METASEARCH_RESULT));
        verify(this.bassettManager, this.metasearchManager, this.saxStrategy, this.result);
	}
	
	
}
