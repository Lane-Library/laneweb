package edu.stanford.irt.laneweb.search;

import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Map;

import org.apache.cocoon.caching.CacheableProcessingComponent;
import org.apache.excalibur.source.SourceValidity;
import org.apache.excalibur.source.impl.validity.NOPValidity;

import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.laneweb.model.ModelUtil;
import edu.stanford.irt.search.Result;
import edu.stanford.irt.search.impl.SimpleQuery;

public class DescribeGenerator extends AbstractMetasearchGenerator implements CacheableProcessingComponent {

    private static final String[] NO_ENGINES = new String[0];

    private String[] engines;

    private String key = null;

    private SourceValidity validity = null;

    public Serializable getKey() {
        return this.key;
    }

    public SourceValidity getValidity() {
        return this.validity;
    }

    @Override
    public void setModel(final Map<String, Object> model) {
        // don't call super.setModel(model) because null query throws exception,
        // instead this can be null:
        this.query = ModelUtil.getString(model, Model.QUERY);
        this.engines = ModelUtil.getObject(model, Model.ENGINES, String[].class, NO_ENGINES);
        //this is cacheable if there are no engines or query
        if (this.query == null && this.engines.length == 0) {
            this.key = "describe";
            this.validity = NOPValidity.SHARED_INSTANCE;
        }
    }

    @Override
    protected Result doSearch() {
        Collection<String> enginesList = null;
        if ((this.engines != null) && (this.engines.length > 0)) {
            enginesList = new LinkedList<String>();
            for (String element : this.engines) {
                enginesList.add(element);
            }
        }
        if (this.query != null) {
            return this.metaSearchManager.describe(new SimpleQuery(this.query), enginesList);
        } else {
            return this.metaSearchManager.describe(new SimpleQuery(""), enginesList);
        }
    }
}
