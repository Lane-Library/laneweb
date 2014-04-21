package edu.stanford.irt.laneweb.search;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
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

	private List<BassettImage> bassettResult = new ArrayList<BassettImage>();

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
		String queryTerm = "query 3";
		expect(this.bassettManager.search(queryTerm)).andReturn((List<BassettImage>) this.bassettResult);
		expect(this.metasearchManager.search(isA(Query.class), eq(20000L),  eq(true))).andReturn(
				this.result);
		replay(this.bassettManager, this.metasearchManager, this.saxStrategy, this.result);
		Map<String, Object> model = new HashMap<String, Object>();
		model.put(Model.QUERY, queryTerm);
		model.put(Model.URL_ENCODED_QUERY, queryTerm.replaceAll(" " , "%20"));
		this.generator.setModel(model);
		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put(Model.ENGINES, "engine-parameter");
		this.generator.setParameters(parameters);
		HashMap<String, Object> resultMap = this.generator.doSearch("query 3");
		assertNotNull(resultMap.get(ImageSearchGenerator.BASSETT_RESULT));
		assertNotNull(resultMap.get(ImageSearchGenerator.METASEARCH_RESULT));
		assertEquals("query%203", resultMap.get(ImageSearchGenerator.SEARCH_TERM));
		verify(this.bassettManager, this.metasearchManager, this.saxStrategy, this.result);
	}

}
