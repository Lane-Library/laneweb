package edu.stanford.irt.laneweb.search;

import java.io.IOException;

import org.apache.cocoon.ProcessingException;
import org.xml.sax.SAXException;

import edu.stanford.irt.laneweb.cocoon.AbstractReader;
import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.laneweb.util.ModelUtil;
import edu.stanford.irt.search.spring.SearchCacheManager;

public class SearchCacheManagerReader extends AbstractReader {

    private SearchCacheManager searchCache;

    public void generate() throws IOException, SAXException, ProcessingException {
        String query = ModelUtil.getString(this.model, Model.QUERY);
        if (query != null) {
            this.searchCache.clearCache(query);
        } else {
            this.searchCache.clearAllCaches();
        }
        this.outputStream.write("OK".getBytes());
    }

    @Override
    public String getMimeType() {
        return "text/plain";
    }

    public void setMetaSearchManagerSource(final MetaSearchManagerSource msms) {
        if (null == msms) {
            throw new IllegalArgumentException("null metaSearchManagerSource");
        }
        this.searchCache = msms.getSearchCacheManager();
        if (null == this.searchCache) {
            throw new IllegalStateException("null searchCacheManager");
        }
    }
}
