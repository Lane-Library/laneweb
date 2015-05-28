package edu.stanford.irt.laneweb.search;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.solr.core.query.result.FacetFieldEntry;
import org.springframework.data.solr.core.query.result.FacetPage;
import org.springframework.oxm.Marshaller;

import edu.stanford.irt.cocoon.pipeline.ModelAware;
import edu.stanford.irt.cocoon.xml.XMLConsumer;
import edu.stanford.irt.laneweb.LanewebException;
import edu.stanford.irt.laneweb.cocoon.AbstractMarshallingGenerator;
import edu.stanford.irt.laneweb.eresources.Eresource;
import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.laneweb.model.ModelUtil;
import edu.stanford.irt.laneweb.solr.Facet;
import edu.stanford.irt.laneweb.solr.SolrSearchService;

public class SolrSearchFacetsGenerator extends AbstractMarshallingGenerator implements ModelAware {

    private static final String COLON_QUOTE = ":\"";

    private static final String EMPTY = "";

    private static final int FACETS_SIZE = 21;

    private static final String QUOTE = "\"";

    private String facet;

    private String facets;

    private int pageNumber = 0;

    private String query;

    private SolrSearchService service;

    public SolrSearchFacetsGenerator(final SolrSearchService service, final Marshaller marshaller) {
        super(marshaller);
        this.service = service;
    }

    @Override
    public void setModel(final Map<String, Object> model) {
        this.facet = ModelUtil.getString(model, Model.FACET, null);
        this.facets = ModelUtil.getString(model, Model.FACETS, EMPTY);
        String page = ModelUtil.getString(model, Model.PAGE);
        if (page != null) {
            this.pageNumber = Integer.valueOf(page) - 1;
        }
        this.query = ModelUtil.getString(model, Model.QUERY);
    }

    @Override
    protected void doGenerate(final XMLConsumer xmlConsumer) {
        FacetPage<Eresource> fps = null;
        if (null == this.facet) {
            fps = this.service.facetByManyFields(this.query, this.facets, new PageRequest(this.pageNumber, 1));
        } else {
            fps = this.service.facetByField(this.query, this.facets, this.facet, new PageRequest(this.pageNumber,
                    FACETS_SIZE));
        }
        marshal(processFacets(fps), xmlConsumer);
    }

    private String encodeString(final String string) {
        String encoded = null;
        try {
            encoded = URLEncoder.encode(string, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new LanewebException(e);
        }
        return encoded;
    }

    private String getUrl(final String fieldName, final String facetName, final boolean on) {
        String url = null;
        String joiner = (this.facets.isEmpty()) ? EMPTY : SolrSearchService.FACETS_SEPARATOR;
        if (on) {
            url = this.facets.replaceFirst("(^|::)" + fieldName + COLON_QUOTE + facetName + QUOTE, EMPTY);
        } else {
            url = this.facets + joiner + fieldName + COLON_QUOTE + facetName + QUOTE;
        }
        url = url.replaceAll("(^::|::$)", EMPTY);
        return encodeString(url);
    }

    private boolean isEnabled(final String fieldName, final String facetName) {
        if (null == this.facets) {
            return false;
        }
        return this.facets.matches(".*" + fieldName + COLON_QUOTE + facetName + "\".*");
    }

    private Map<String, Object> processFacets(final FacetPage<Eresource> facetpage) {
        Map<String, Object> facetsMap = new LinkedHashMap<String, Object>();
        for (Page<FacetFieldEntry> page : facetpage.getFacetResultPages()) {
            if (!page.hasContent()) {
                continue;
            }
            List<Facet> facetList = new ArrayList<Facet>();
            String fieldName = null;
            for (FacetFieldEntry entry : page) {
                if (fieldName == null) {
                    fieldName = entry.getField().getName();
                }
                String facetName = entry.getValue();
                boolean isEnabled = isEnabled(fieldName, facetName);
                facetList.add(new Facet(facetName, entry.getValueCount(), isEnabled(fieldName, facetName), getUrl(
                        fieldName, facetName, isEnabled)));
            }
            facetsMap.put(fieldName, facetList);
        }
        return facetsMap;
    }
}
