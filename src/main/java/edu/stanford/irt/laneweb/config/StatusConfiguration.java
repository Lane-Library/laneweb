package edu.stanford.irt.laneweb.config;

import java.lang.management.ManagementFactory;
import java.net.InetAddress;
import java.net.URI;
import java.net.UnknownHostException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import edu.stanford.irt.cocoon.sitemap.ComponentFactory;
import edu.stanford.irt.cocoon.sitemap.Sitemap;
import edu.stanford.irt.cocoon.source.SourceResolver;
import edu.stanford.irt.laneweb.eresources.EresourceStatusProvider;
import edu.stanford.irt.laneweb.eresources.SolrService;
import edu.stanford.irt.laneweb.servlet.mvc.IndexDotHtmlStatusProvider;
import edu.stanford.irt.laneweb.status.LanewebStatusService;
import edu.stanford.irt.laneweb.suggest.SuggestStatusProvider;
import edu.stanford.irt.laneweb.suggest.SuggestionService;
import edu.stanford.irt.status.DefaultStatusService;
import edu.stanford.irt.status.RuntimeMXBeanStatusProvider;
import edu.stanford.irt.status.StatusProvider;
import edu.stanford.irt.status.StatusService;

@Configuration
public class StatusConfiguration {

    private static final int MIN_BIB_COUNT = 310_000;

    private static final int MIN_PUBMED_COUNT = 27_000_000;

    private static final int SLOW_INDEX_HTML_TIME = 250;

    private static final int SLOW_SUGGESTION_TIME = 250;

    @Bean
    @Order(4)
    public EresourceStatusProvider eresourceStatusProvider(final SolrService solrService) {
        return new EresourceStatusProvider(solrService, MIN_BIB_COUNT, MIN_PUBMED_COUNT);
    }

    @Bean
    @Order(3)
    public IndexDotHtmlStatusProvider indexDotHtmlStatusProvider(final ComponentFactory componentFactory,
            @Qualifier("edu.stanford.irt.cocoon.sitemap.Sitemap/sitemap") final Sitemap sitemap,
            final SourceResolver sourceResolver, @Value("${edu.stanford.irt.laneweb.live-base}") final URI contentBase,
            @Qualifier("java.net.URI/classes-service") final URI classesServiceURI) {
        return new IndexDotHtmlStatusProvider(sitemap, componentFactory, sourceResolver, SLOW_INDEX_HTML_TIME,
                contentBase, classesServiceURI);
    }

    @Bean
    @Order(1)
    public RuntimeMXBeanStatusProvider jvmStatusProvider() {
        return new RuntimeMXBeanStatusProvider();
    }

    @Bean
    public LanewebStatusService lanewebStatusService(final List<StatusService> services) {
        return new LanewebStatusService(services);
    }

    @Bean
    public StatusService statusService(final List<StatusProvider> providers,
            @Value("${edu.stanford.irt.laneweb.version}") final String version) throws UnknownHostException {
        String host;
        int pid;
        String name = ManagementFactory.getRuntimeMXBean().getName();
        Matcher matcher = Pattern.compile("(\\d+)@(.+)").matcher(name);
        if (matcher.matches()) {
            host = matcher.group(2);
            pid = Integer.valueOf(matcher.group(1));
        } else {
            host = InetAddress.getLocalHost().getHostName();
            pid = -1;
        }
        return new DefaultStatusService("laneweb", version, host, pid, providers);
    }

    @Bean
    @Order(2)
    public SuggestStatusProvider suggestStatusProvider(final SuggestionService suggestionService) {
        return new SuggestStatusProvider(suggestionService, SLOW_SUGGESTION_TIME, "cardio");
    }
}
