package edu.stanford.irt.laneweb.eresources;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.stanford.irt.status.Status;
import edu.stanford.irt.status.StatusItem;
import edu.stanford.irt.status.StatusProvider;

public class EresourceStatusProvider implements StatusProvider {

    private static final String BIB = "bib";

    private static final String FAIL_FORMAT = "solr record counts failed: %s";

    private static final Logger log = LoggerFactory.getLogger(EresourceStatusProvider.class);

    private static final String PUBMED = "pubmed";

    private static final String RECORD_SUCCESS_FORMAT = "%s record count: %d";

    private static final String SUL = "sul";

    private long minBibCount;

    private long minPubmedCount;

    private long minSulCount;

    private SolrService solrService;

    public EresourceStatusProvider(final SolrService solrService, final long minBibCount, final long minPubmedCount,
            final long minSulCount) {
        this.solrService = solrService;
        this.minBibCount = minBibCount;
        this.minPubmedCount = minPubmedCount;
        this.minSulCount = minSulCount;
    }

    @Override
    public List<StatusItem> getStatusItems() {
        List<StatusItem> items = new ArrayList<>();
        try {
            Map<String, Long> results = this.solrService.recordCount();
            long bibCount = results.containsKey(BIB) ? results.get(BIB).longValue() : 0;
            Status bibStatus = bibCount > this.minBibCount ? Status.OK : Status.ERROR;
            items.add(new StatusItem(bibStatus, String.format(RECORD_SUCCESS_FORMAT, BIB, bibCount)));
            long pubmedCount = results.containsKey(PUBMED) ? results.get(PUBMED).longValue() : 0;
            Status pubmedStatus = pubmedCount > this.minPubmedCount ? Status.OK : Status.ERROR;
            items.add(new StatusItem(pubmedStatus, String.format(RECORD_SUCCESS_FORMAT, PUBMED, pubmedCount)));
            long searchworksCount = results.containsKey(SUL) ? results.get(SUL).longValue() : 0;
            Status searchworksStatus = searchworksCount > this.minSulCount ? Status.OK : Status.ERROR;
            items.add(new StatusItem(searchworksStatus, String.format(RECORD_SUCCESS_FORMAT, SUL, searchworksCount)));
        } catch (RuntimeException e) {
            String message = String.format(FAIL_FORMAT, e);
            items.add(new StatusItem(Status.ERROR, message));
            log.error(message);
        }
        return items;
    }
}
