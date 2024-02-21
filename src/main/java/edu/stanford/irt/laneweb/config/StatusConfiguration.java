package edu.stanford.irt.laneweb.config;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import edu.stanford.irt.cocoon.sitemap.ComponentFactory;
import edu.stanford.irt.cocoon.sitemap.Sitemap;
import edu.stanford.irt.cocoon.source.SourceResolver;
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

    private static final int SLOW_INDEX_HTML_TIME = 250;

    private static final int SLOW_SUGGESTION_TIME = 250;

    @Bean
    @Order(3)
    public IndexDotHtmlStatusProvider indexDotHtmlStatusProvider(final ComponentFactory componentFactory,
            @Qualifier("edu.stanford.irt.cocoon.sitemap.Sitemap/sitemap") final Sitemap sitemap,
            final SourceResolver sourceResolver, @Value("${edu.stanford.irt.laneweb.live-base}") final URI contentBase,
            @Qualifier("java.net.URI/libcal-service") final URI classesServiceURI) {
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
            @Value("${edu.stanford.irt.laneweb.version}") final String version) {
        return new DefaultStatusService("laneweb", version, providers);
    }

    @Bean
    @Order(2)
    public SuggestStatusProvider suggestStatusProvider(final SuggestionService suggestionService) {
        return new SuggestStatusProvider(suggestionService, SLOW_SUGGESTION_TIME, "cardio");
    }
}
