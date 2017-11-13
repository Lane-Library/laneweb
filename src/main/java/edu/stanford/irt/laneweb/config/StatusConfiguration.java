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
import edu.stanford.irt.laneweb.status.StatusProvider;
import edu.stanford.irt.laneweb.status.StatusService;
import edu.stanford.irt.laneweb.suggest.SuggestStatusProvider;
import edu.stanford.irt.suggest.SuggestionManager;

@Configuration
public class StatusConfiguration {

    @Bean
    @Order(3)
    public EresourceStatusProvider eresourceStatusProvider(final SolrService solrService) {
        return new EresourceStatusProvider(solrService, 310_000, 27_000_000);
    }

    @Bean
    @Order(2)
    public IndexDotHtmlStatusProvider indexDotHtmlStatusProvider(final ComponentFactory componentFactory,
            @Qualifier("edu.stanford.irt.cocoon.sitemap.Sitemap/sitemap") final Sitemap sitemap,
            final SourceResolver sourceResolver, @Value("${edu.stanford.irt.laneweb.live-base}") final URI contentBase,
            @Qualifier("java.net.URI/classes-service") final URI classesServiceURI) {
        return new IndexDotHtmlStatusProvider(sitemap, componentFactory, sourceResolver, 250, contentBase,
                classesServiceURI);
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
        return new StatusService("laneweb", version, host, pid, providers);
    }

    @Bean
    @Order(1)
    public SuggestStatusProvider suggestStatusProvider(
            @Qualifier("edu.stanford.irt.suggest.SuggestionManager/eresource") final SuggestionManager suggestionManager) {
        return new SuggestStatusProvider(suggestionManager, 250, "cardio");
    }
    
    public static void main(String[] args) {

        String name = ManagementFactory.getRuntimeMXBean().getName();
       System.out.println(name);
        Matcher matcher = Pattern.compile("(\\d+)@(.+)").matcher(name);
        System.out.println(matcher.matches());
        System.out.println(matcher.group(1));
        System.out.println(matcher.group(2));
    }
}
