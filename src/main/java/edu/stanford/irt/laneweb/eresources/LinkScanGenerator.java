package edu.stanford.irt.laneweb.eresources;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import edu.stanford.irt.cocoon.cache.Validity;
import edu.stanford.irt.cocoon.pipeline.CacheablePipelineComponent;
import edu.stanford.irt.cocoon.pipeline.generate.AbstractGenerator;
import edu.stanford.irt.cocoon.xml.XMLConsumer;
import edu.stanford.irt.laneweb.LanewebException;
import edu.stanford.irt.laneweb.solr.SolrSearchService;
import edu.stanford.irt.laneweb.util.XMLUtils;

public class LinkScanGenerator extends AbstractGenerator implements CacheablePipelineComponent {

    private static final String KEY = "link-scan";

    private static final String TYPE = KEY;

    private static final String XHTML_NS = "http://www.w3.org/1999/xhtml";

    private SolrSearchService searchService;

    private Validity validity;

    @Autowired
    public LinkScanGenerator(final SolrSearchService searchService) {
        this.searchService = searchService;
        this.validity = new LinkScanValidity();
    }

    @Override
    public Serializable getKey() {
        return KEY;
    }

    @Override
    public String getType() {
        return TYPE;
    }

    @Override
    public Validity getValidity() {
        return this.validity;
    }

    @Override
    protected void doGenerate(final XMLConsumer xmlConsumer) {
        List<Eresource> results = this.searchService.searchFindAllNotRecordTypePubmed();
        int p = 1;
        String position, id, title;
        try {
            xmlConsumer.startDocument();
            XMLUtils.startElement(xmlConsumer, XHTML_NS, "ul");
            for (Eresource eresource : results) {
                id = eresource.getId();
                title = eresource.getTitle();
                if (title == null) {
                    title = "NULL TITLE";
                }
                for (String url : getLinks(eresource)) {
                    position = " #" + p++ + ' ';
                    url = url.toString();
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

    private Set<String> getLinks(final Eresource eresource) {
        Set<String> urls = new HashSet<>();
        for (Link link : eresource.getLinks()) {
            String linkUrl = link.getUrl();
            if (null != linkUrl && linkUrl.startsWith("http")) {
                urls.add(linkUrl);
            }
        }
        return urls;
    }
}
