package edu.stanford.irt.laneweb.proxy;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;

import org.springframework.data.domain.Page;
import org.springframework.data.solr.core.query.FacetOptions.FacetSort;
import org.springframework.data.solr.core.query.result.FacetFieldEntry;
import org.springframework.data.solr.core.query.result.FacetPage;

import edu.stanford.irt.laneweb.LanewebException;
import edu.stanford.irt.laneweb.eresources.Eresource;
import edu.stanford.irt.laneweb.eresources.search.redesign.FacetService;

public class SolrProxyServersService implements ProxyServersService {

    private static final byte[] COLON_443 = { ':', '4', '4', '3' };

    private static final byte[] HJ = { 'H', 'J', ' ' };

    private static final int MAX_HOSTS = 100_000;

    private static final byte[] T = { 'T', ' ' };

    private static final byte[] U_HTTPS = { 'U', ' ', 'h', 't', 't', 'p', 's', ':', '/', '/' };

    private FacetService solrService;

    public SolrProxyServersService(final FacetService solrService) {
        this.solrService = solrService;
    }

    @Override
    public Set<String> getHosts() {
        FacetPage<Eresource> fps = this.solrService.facetByField("*", null, "proxyHosts",  MAX_HOSTS, 1, FacetSort.INDEX);
        Set<String> hosts = extractHosts(fps.getFacetResultPages());
        hosts.add("bodoni.stanford.edu");
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

    private Set<String> extractHosts(final Collection<Page<FacetFieldEntry>> facetResultPages) {
        Set<String> hosts = new TreeSet<>();
        for (Page<FacetFieldEntry> page : facetResultPages) {
            for (FacetFieldEntry entry : page) {
                hosts.add(entry.getValue());
            }
        }
        return hosts;
    }
}
