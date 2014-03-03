package edu.stanford.irt.laneweb.eresources;

import java.io.IOException;
import java.io.Serializable;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import com.fasterxml.jackson.databind.ObjectMapper;

import edu.stanford.irt.cocoon.cache.Validity;
import edu.stanford.irt.cocoon.pipeline.CacheablePipelineComponent;
import edu.stanford.irt.cocoon.pipeline.generate.AbstractGenerator;
import edu.stanford.irt.cocoon.xml.XMLConsumer;
import edu.stanford.irt.laneweb.LanewebException;
import edu.stanford.irt.laneweb.util.XMLUtils;

public class LinkScanGenerator extends AbstractGenerator implements CacheablePipelineComponent {

    private static final String KEY = "link-scan";

    private static final String TYPE = KEY;

    private static final String XHTML_NS = "http://www.w3.org/1999/xhtml";

    private ObjectMapper mapper = new ObjectMapper();

    private SolrServer solrServer;

    private Validity validity;

    public LinkScanGenerator(final SolrServer solrServer) {
        this.solrServer = solrServer;
        this.validity = new LinkScanValidity();
    }

    @Override
    protected void doGenerate(final XMLConsumer xmlConsumer) {
        SolrQuery solrQuery = new SolrQuery();
        solrQuery.setRequestHandler("/lane-search");
        solrQuery.setQuery("-recordType:pubmed");
        solrQuery.add("rows", Integer.toString(Integer.MAX_VALUE));
        QueryResponse rsp;
        try {
            rsp = this.solrServer.query(solrQuery);
        } catch (SolrServerException e) {
            throw new LanewebException(e);
        }
        SolrDocumentList rdocs = rsp.getResults();
        int p = 1;
        String position, url, id, title;
        try {
            xmlConsumer.startDocument();
            XMLUtils.startElement(xmlConsumer, XHTML_NS, "ul");
            for (SolrDocument doc : rdocs) {
                id = (String) doc.getFieldValue("id");
                title = (String) doc.getFieldValue("title");
                if (title == null) {
                    title = "NULL TITLE";
                }
                for (URI uri : getLinks(doc)) {
                    position = " #" + p++ + ' ';
                    url = uri.toString();
                    XMLUtils.startElement(xmlConsumer, XHTML_NS, "li");
                    XMLUtils.data(xmlConsumer, position);
                    XMLUtils.startElement(xmlConsumer, XHTML_NS, "ul");
                    XMLUtils.startElement(xmlConsumer, XHTML_NS, "li");
                    AttributesImpl atts = new AttributesImpl();
                    atts.addAttribute("", "href", "href", "CDATA", url);
                    XMLUtils.startElement(xmlConsumer, XHTML_NS, "a", atts);
                    XMLUtils.data(xmlConsumer, " id: " + id + " title: " + title);
                    XMLUtils.endElement(xmlConsumer, XHTML_NS, "a");
                    XMLUtils.endElement(xmlConsumer, XHTML_NS, "li");
                    XMLUtils.endElement(xmlConsumer, XHTML_NS, "ul");
                    XMLUtils.endElement(xmlConsumer, XHTML_NS, "li");
                }
            }
            XMLUtils.endElement(xmlConsumer, XHTML_NS, "ul");
            xmlConsumer.endDocument();
        } catch (SAXException e) {
            throw new LanewebException(e);
        }
    }

    @Override
    public Serializable getKey() {
        return KEY;
    }

    public Set<URI> getLinks(final SolrDocument document) {
        HashSet<URI> urls = new HashSet<URI>();
        List<LinkedHashMap<String, Object>> versionData;
        try {
            versionData = this.mapper.readValue((String) document.getFieldValue("versionsJson"), List.class);
        } catch (IOException e) {
            throw new LanewebException(e);
        }
        for (LinkedHashMap<String, Object> versionMap : versionData) {
            if (versionMap.containsKey("links")) {
                for (Object linkObj : (ArrayList<Object>) versionMap.get("links")) {
                    LinkedHashMap<String, Object> link = (LinkedHashMap<String, Object>) linkObj;
                    if (link.containsKey("url")) {
                        String linkUrl = (String) link.get("url");
                        if (null != linkUrl) {
                            URI url;
                            try {
                                url = new URI(linkUrl);
                                String scheme = url.getScheme();
                                if (null != scheme && scheme.startsWith("http")) {
                                    urls.add(url);
                                }
                            } catch (URISyntaxException e) {
                                // TODO report these to RM?
                                // System.out.println(document.getFieldValue("id") + ": " + e);
                            }
                        }
                    }
                }
            }
        }
        return urls;
    }

    @Override
    public String getType() {
        return TYPE;
    }

    @Override
    public Validity getValidity() {
        return this.validity;
    }
}
