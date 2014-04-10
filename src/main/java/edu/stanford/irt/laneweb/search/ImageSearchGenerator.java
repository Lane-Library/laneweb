package edu.stanford.irt.laneweb.search;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import edu.stanford.irt.cocoon.pipeline.ParametersAware;
import edu.stanford.irt.cocoon.xml.SAXStrategy;
import edu.stanford.irt.laneweb.bassett.BassettCollectionManager;
import edu.stanford.irt.laneweb.bassett.BassettImage;
import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.laneweb.model.ModelUtil;
import edu.stanford.irt.search.impl.MetaSearchManager;
import edu.stanford.irt.search.impl.Result;
import edu.stanford.irt.search.impl.SimpleQuery;

public class ImageSearchGenerator  extends AbstractMetasearchGenerator<HashMap<String, Object>> implements ParametersAware{

	public static  final String BASSETT_RESULT = "bassett";
	public static  final String METASEARCH_RESULT = "metasearch";
	
	private static final long DEFAULT_TIMEOUT = 20000;

    private Collection<String> engines;

    private MetaSearchManager metasearchManager;

    private BassettCollectionManager bassettCollection;
    
    private String timeout;
	
	public ImageSearchGenerator(final MetaSearchManager metaSearchManager, final BassettCollectionManager collectionManager, final SAXStrategy<HashMap<String, Object>> saxStrategy) {
		super(metaSearchManager, saxStrategy);
		this.bassettCollection = collectionManager;
		this.metasearchManager = metaSearchManager;
	}

	@Override
	protected HashMap<String, Object> doSearch(String query) {
		HashMap<String,Object> result = new HashMap<>();
		long time = DEFAULT_TIMEOUT;
        if (null != this.timeout) {
            try {
                time = Long.parseLong(this.timeout);
            } catch (NumberFormatException nfe) {
                time = DEFAULT_TIMEOUT;
            }
        }
        List<BassettImage> bassettResult = bassettCollection.search(query);
        result.put(BASSETT_RESULT, bassettResult);
        Result metaSearchResult = null;
        if (query == null || query.isEmpty()) {
        	metaSearchResult = new Result("");
        } else {
        	metaSearchResult = this.metasearchManager.search(new SimpleQuery(query, this.engines), time, true);
        }
        result.put(METASEARCH_RESULT, metaSearchResult);
		return result;
	}
	
	
	 @SuppressWarnings("unchecked")
	    @Override
	    public void setModel(final Map<String, Object> model) {
	        super.setModel(model);
	        this.timeout = ModelUtil.getString(model, Model.TIMEOUT);
	        this.engines = ModelUtil.getObject(model, Model.ENGINES, Collection.class, Collections.<String> emptyList());
	    }

	    public void setParameters(final Map<String, String> parameters) {
	        if (this.timeout == null) {
	            this.timeout = parameters.get(Model.TIMEOUT);
	        }
	        if (this.engines == null || this.engines.size() == 0) {
	            String engineList = parameters.get(Model.ENGINES);
	            if (engineList != null) {
	                this.engines = new LinkedList<String>();
	                for (StringTokenizer st = new StringTokenizer(engineList, ","); st.hasMoreTokens();) {
	                    this.engines.add(st.nextToken());
	                }
	            }
	        }
	    }

	

}
