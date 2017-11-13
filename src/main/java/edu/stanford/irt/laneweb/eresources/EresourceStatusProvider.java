package edu.stanford.irt.laneweb.eresources;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.stanford.irt.laneweb.status.Status;
import edu.stanford.irt.laneweb.status.StatusItem;
import edu.stanford.irt.laneweb.status.StatusProvider;

public class EresourceStatusProvider implements StatusProvider {

    private static final String BIB_SUCCESS_FORMAT = "bib record count: %d";

    private static final String FAIL_FORMAT = "solr record counts failed: %s";

    private static final Logger log = LoggerFactory.getLogger(EresourceStatusProvider.class);

    private static final String PUBMED_SUCCESS_FORMAT = "pubmed record count: %d";

    private long minBibCount;

    private long minPubmedCount;

    private SolrService solrService;

    public EresourceStatusProvider(final SolrService solrService, final long minBibCount, final long minPubmedCount) {
        this.solrService = solrService;
        this.minBibCount = minBibCount;
        this.minPubmedCount = minPubmedCount;
    }

    @Override
    public StatusItem getStatus() {
        boolean error = getStatuses().stream().anyMatch((final StatusItem item) -> item.getStatus() == Status.ERROR);
        if (error) {
            return new StatusItem(Status.ERROR, "solr record counts failed");
        } else {
            return new StatusItem(Status.OK, "solr record counts successful");
        }
    }

    @Override
    public List<StatusItem> getStatuses() {
        List<StatusItem> items = new ArrayList<>();
        try {
            Map<String, Long> results = this.solrService.recordCount();
            long bibCount = results.containsKey("bib") ? results.get("bib").longValue() : 0;
            Status bibStatus = bibCount > this.minBibCount ? Status.OK : Status.ERROR;
            items.add(new StatusItem(bibStatus, String.format(BIB_SUCCESS_FORMAT, bibCount)));
            long pubmedCount = results.containsKey("pubmed") ? results.get("pubmed").longValue() : 0;
            Status pubmedStatus = pubmedCount > this.minPubmedCount ? Status.OK : Status.ERROR;
            items.add(new StatusItem(pubmedStatus, String.format(PUBMED_SUCCESS_FORMAT, pubmedCount)));
        } catch (Exception e) {
            String message = String.format(FAIL_FORMAT, e);
            items.add(new StatusItem(Status.ERROR, message));
            log.error(message);
        }
        return items;
    }
}
