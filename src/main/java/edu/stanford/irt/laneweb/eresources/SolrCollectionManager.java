package edu.stanford.irt.laneweb.eresources;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.FacetField;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.params.ModifiableSolrParams;
import org.apache.solr.common.params.SolrParams;

import com.fasterxml.jackson.databind.ObjectMapper;

import edu.stanford.irt.laneweb.LanewebException;

public class SolrCollectionManager implements CollectionManager {

    private static final String LANE_BROWSE_HANDLER = "/lane-browse";

    private static final String LANE_SEARCH_HANDLER = "/lane-search";

    private static final String QUOTE = "\"";

    private ObjectMapper mapper = new ObjectMapper();

    //private SolrScoreStrategy scoreStrategy = new SolrScoreStrategy();

    private SolrServer solrServer;

    public SolrCollectionManager(final SolrServer solrServer) {
        this.solrServer = solrServer;
    }

    protected List<Eresource> doGet(final String handler, final SolrParams params) {
        SolrQuery solrQuery = new SolrQuery();
        solrQuery.setRequestHandler(handler);
        solrQuery.add(params);
        if (null == params.get("rows")) {
            // TODO: efficiencies to be gained here by NOT returning all results
            solrQuery.add("rows", Integer.toString(Integer.MAX_VALUE));
        }
        String q = solrQuery.getQuery();
        QueryResponse rsp;
        try {
            rsp = this.solrServer.query(solrQuery);
        } catch (SolrServerException e) {
            throw new LanewebException(e);
        }
        Float maxScore = rsp.getResults().getMaxScore();
        SolrDocumentList rdocs = rsp.getResults();
        LinkedList<Eresource> eresources = new LinkedList<Eresource>();
        for (SolrDocument doc : rdocs) {
            String description = null;
            int docId = 0;
            String recordId = null;
            String recordType = null;
            int score = 0;
            String title = null;
            String publicationAuthorsText = null;
            String publicationText = null;
            int year = 0;
            if (doc.containsKey("description")) {
                description = (String) doc.getFieldValue("description");
            }
            if (doc.containsKey("id")) {
                docId = ((String) doc.getFieldValue("id")).hashCode();
            }
            if (doc.containsKey("recordId")) {
                recordId = (String) doc.getFieldValue("recordId");
            }
            if (doc.containsKey("recordType")) {
                recordType = (String) doc.getFieldValue("recordType");
            }
            if (doc.containsKey("title")) {
                title = (String) doc.getFieldValue("title");
            }
            if (doc.containsKey("year")) {
                year = (int) doc.getFieldValue("year");
            }
            if (doc.containsKey("publicationText")) {
                publicationText = (String) doc.getFieldValue("publicationText");
            }
            if (doc.containsKey("publicationAuthorsText")) {
                publicationAuthorsText = (String) doc.getFieldValue("publicationAuthorsText");
            }
            if (doc.containsKey("score")) {
                //score = this.scoreStrategy.computeScore(q, title, year, (Float) doc.getFieldValue("score"), maxScore);
                score = (int) ((Float) doc.getFieldValue("score")).floatValue();
            }
            Eresource eresource = new Eresource(description, docId, Integer.parseInt(recordId), recordType, score,
                    title, publicationAuthorsText, publicationText);
            List<LinkedHashMap<String, Object>> versionData;
            try {
                versionData = this.mapper.readValue((String) doc.getFieldValue("versionsJson"), List.class);
            } catch (IOException e) {
                throw new LanewebException(e);
            }
            for (LinkedHashMap<String, Object> versionMap : versionData) {
                boolean versionHasGetPasswordLink = false;
                if (versionMap.containsKey("hasGetPasswordLink")) {
                    versionHasGetPasswordLink = (boolean) versionMap.get("hasGetPasswordLink");
                }
                if (versionMap.containsKey("links")) {
                    boolean isFirstLink = true;
                    for (Object linkObj : (ArrayList<Object>) versionMap.get("links")) {
                        LinkedHashMap<String, Object> link = (LinkedHashMap<String, Object>) linkObj;
                        String linkLabel = null;
                        String linkUrl = null;
                        String linkText = null;
                        String linkAdditionalText = null;
                        LinkType linkType = null;
                        if (link.containsKey("label")) {
                            linkLabel = (String) link.get("label");
                        }
                        if (link.containsKey("linkText")) {
                            linkText = (String) link.get("linkText");
                            if (isFirstLink) {
                                linkText = title;
                                isFirstLink = false;
                            }
                        }
                        if (link.containsKey("additionalText")) {
                            linkAdditionalText = (String) link.get("additionalText");
                        }
                        if (link.containsKey("url")) {
                            linkUrl = (String) link.get("url");
                        }
                        if (versionHasGetPasswordLink) {
                            linkType = LinkType.GETPASSWORD;
                        } else if (linkLabel != null && "impact factor".equalsIgnoreCase(linkLabel)) {
                            linkType = LinkType.IMPACTFACTOR;
                        } else {
                            linkType = LinkType.NORMAL;
                        }
                        eresource.addLink(new Link(linkLabel, linkType, linkUrl, linkText, linkAdditionalText));
                    }
                }
            }
            eresources.add(eresource);
        }
        return eresources;
    }

    @Override
    public List<Eresource> getCore(final String type) {
        if (null == type) {
            throw new IllegalArgumentException("null type");
        }
        ModifiableSolrParams params = new ModifiableSolrParams();
        params.set("fq", "isCore:1");
        params.add("fq", "type:" + quoteWrap(type));
        params.set("q", "*:*");
        return doGet(LANE_BROWSE_HANDLER, params);
    }

    @Override
    public List<Eresource> getMesh(final String type, final String mesh) {
        if (null == type) {
            throw new IllegalArgumentException("null type");
        }
        if (null == mesh) {
            throw new IllegalArgumentException("null mesh");
        }
        ModifiableSolrParams params = new ModifiableSolrParams();
        params.set("fq", "mesh:" + quoteWrap(mesh));
        params.add("fq", "type:" + quoteWrap(type));
        params.set("q", "*:*");
        return doGet(LANE_BROWSE_HANDLER, params);
    }

    // TODO: remove these when upgrading to eresources-1.8
    public List<Eresource> getMeshCore(final String type, final String mesh) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<Eresource> getSubset(final String subset) {
        if (null == subset) {
            throw new IllegalArgumentException("null subset");
        }
        ModifiableSolrParams params = new ModifiableSolrParams();
        params.set("fq", "subset:" + quoteWrap(subset));
        params.set("q", "*:*");
        return doGet(LANE_BROWSE_HANDLER, params);
    }

    @Override
    public List<Eresource> getType(final String type) {
        if (null == type) {
            throw new IllegalArgumentException("null type");
        }
        ModifiableSolrParams params = new ModifiableSolrParams();
        params.set("fq", "type:" + quoteWrap(type));
        params.set("q", "*:*");
        return doGet(LANE_BROWSE_HANDLER, params);
    }

    @Override
    public List<Eresource> getType(final String type, final char alpha) {
        if (null == type) {
            throw new IllegalArgumentException("null type");
        }
        char sAlpha = alpha;
        // solr stores starts with numeric as '1'
        if ('#' == sAlpha) {
            sAlpha = '1';
        }
        ModifiableSolrParams params = new ModifiableSolrParams();
        params.set("fq", "type:" + quoteWrap(type));
        // ertlsw = random, uncommon string so single letter isn't stopword'd out of results
        params.set("q", "ertlsw" + sAlpha);
        return doGet(LANE_BROWSE_HANDLER, params);
    }

    private String quoteWrap(final String string) {
        StringBuffer sb = new StringBuffer();
        sb.append(QUOTE).append(string).append(QUOTE);
        return sb.toString();
    }

    @Override
    public List<Eresource> search(final String query) {
        ModifiableSolrParams params = new ModifiableSolrParams();
        params.set("q", query);
        // TODO: just for demo ... need pagination
        params.set("rows", "100");
        return doGet(LANE_SEARCH_HANDLER, params);
    }

    // TODO: remove these when upgrading to 1.8
    @Override
    public Map<String, Integer> searchCount(final Set<String> types, final Set<String> subsets, final String query) {
        return searchCount(types, query);
    }

    public Map<String, Integer> searchCount(final Set<String> types, final String query) {
        Map<String, Integer> result = new HashMap<String, Integer>();
        SolrQuery solrQuery = new SolrQuery();
        solrQuery.setRequestHandler(LANE_SEARCH_HANDLER);
        solrQuery.setQuery(query);
        solrQuery.setFacet(true);
        solrQuery.addFacetField("type");
        QueryResponse rsp;
        try {
            rsp = this.solrServer.query(solrQuery);
        } catch (SolrServerException e) {
            throw new LanewebException(e);
        }
        result.put("all", Integer.valueOf((int) rsp.getResults().getNumFound()));
        for (FacetField facetField : rsp.getFacetFields()) {
            for (FacetField.Count count : facetField.getValues()) {
                if (types.contains(count.getName())) {
                    result.put(count.getName(), Integer.valueOf((int) count.getCount()));
                }
            }
        }
        return result;
    }

    public List<Eresource> searchSubset(final String subset, final String query) {
        if (null == subset) {
            throw new IllegalArgumentException("null subset");
        }
        ModifiableSolrParams params = new ModifiableSolrParams();
        params.set("fq", "subset:" + quoteWrap(subset));
        params.set("q", query);
        // TODO: just for demo ... need pagination
        params.set("rows", "100");
        return doGet(LANE_SEARCH_HANDLER, params);
    }

    @Override
    public List<Eresource> searchType(final String type, final String query) {
        if (null == type) {
            throw new IllegalArgumentException("null type");
        }
        ModifiableSolrParams params = new ModifiableSolrParams();
        params.set("fq", "type:" + quoteWrap(type));
        params.set("q", query);
        // TODO: just for demo ... need pagination
        params.set("rows", "100");
        return doGet(LANE_SEARCH_HANDLER, params);
    }
}