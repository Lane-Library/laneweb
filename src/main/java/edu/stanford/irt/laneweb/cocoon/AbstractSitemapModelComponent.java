package edu.stanford.irt.laneweb.cocoon;

import java.io.IOException;
import java.util.Map;

import org.apache.avalon.framework.parameters.Parameters;
import org.apache.cocoon.environment.SourceResolver;
import org.apache.cocoon.objectmodel.helper.ParametersMap;
import org.apache.cocoon.sitemap.SitemapModelComponent;
import org.apache.excalibur.source.Source;

public abstract class AbstractSitemapModelComponent implements SitemapModelComponent {

    protected Map<String, Object> getModel() {
		return model;
	}

	protected Map<String, String> getParameterMap() {
		return parameterMap;
	}

	protected Source getSource() {
		return source;
	}

	private Map<String, Object> model;

    private Map<String, String> parameterMap;

    private Source source;

    @Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
    public final void setup(final SourceResolver resolver, final Map objectModel, final String src, final Parameters par) {
        this.model = objectModel;
        this.parameterMap = new ParametersMap(par);
        if (null != src) {
            try {
                this.source = resolver.resolveURI(src);
            } catch (IOException e) {
                throw new IllegalArgumentException(e);
            }
        }
        initialize();
    }

    protected void initialize() {
    }
    //TODO: implement these more useful protected methods instead of ModelUtil
//    protected String getValue(String name) {
//    	return getValue(name, null);
//    }
//    
//    protected String getValue(String name, String defaultValue) {
//    	String value = this.parameterMap.get(name);
//    	if (value == null) {
//    		value = ModelUtil.getString(this.model, name, defaultValue);
//    	}
//    	return value;
//    }
}
