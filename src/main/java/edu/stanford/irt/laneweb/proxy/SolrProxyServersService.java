package edu.stanford.irt.laneweb.proxy;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;



import edu.stanford.irt.laneweb.LanewebException;
import edu.stanford.irt.laneweb.eresources.EresourceFacetService;
import edu.stanford.irt.laneweb.eresources.model.solr.FacetFieldEntry;
import edu.stanford.irt.laneweb.eresources.model.solr.FacetSort;

public class SolrProxyServersService implements ProxyServersService {

    private static final byte[] COLON_443 = { ':', '4', '4', '3' };

    private static final byte[] HJ = { 'H', 'J', ' ' };

    private static final int MAX_HOSTS = 100_000;

    private static final byte[] T = { 'T', ' ' };

    private static final byte[] U_HTTPS = { 'U', ' ', 'h', 't', 't', 'p', 's', ':', '/', '/' };

    private EresourceFacetService restFacetService;
    
    private static final String FACET_NAME = "proxyHosts";

    public SolrProxyServersService(final EresourceFacetService solrService) {
        this.restFacetService = solrService;
    }

    @Override
    public Set<String> getHosts() {
        Map<String, List<FacetFieldEntry>> fps = this.restFacetService.facetByField("*", "proxyHosts:*",
                FACET_NAME, MAX_HOSTS, 1, FacetSort.INDEX);
        Set<String> hosts = extractHosts(fps);
        hosts.add("library.stanford.edu");
        hosts.add("searchworks.stanford.edu");
        return hosts;
    }

    @Override
    public void write(final OutputStream outputStream) throws IOException {
        Set<String> hosts = getHosts();
        Objects.requireNonNull(outputStream, "null outputStream");
        try (OutputStream out = outputStream) {
            for (String host : hosts) {
                byte[] hostBytes = host.getBytes(StandardCharsets.UTF_8);
                out.write(T);
                out.write(hostBytes);
                out.write('\n');
                out.write(U_HTTPS);
                out.write(hostBytes);
                out.write('\n');
                out.write(HJ);
                out.write(hostBytes);
                out.write('\n');
                out.write(HJ);
                out.write(hostBytes);
                out.write(COLON_443);
                out.write('\n');
                out.write('\n');
            }
        } catch (IOException e) {
            throw new LanewebException(e);
        }
    }

    private Set<String> extractHosts(final Map<String,List<FacetFieldEntry>> facetResultPages) {
        Set<String> hosts = new TreeSet<>();
        List<FacetFieldEntry> facetEntries = facetResultPages.get(FACET_NAME);
        for (FacetFieldEntry entry : facetEntries) {
                hosts.add(entry.getValue());
           
        }
        return hosts;
    }
}
