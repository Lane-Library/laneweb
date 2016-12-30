package edu.stanford.irt.laneweb.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import edu.stanford.irt.laneweb.cme.CMELinkSelector;
import edu.stanford.irt.laneweb.cme.HTMLCMELinkTransformer;

@Configuration
public class CMEConfiguration {

    @Bean(name = "edu.stanford.irt.cocoon.sitemap.select.Selector/cme-links")
    public CMELinkSelector cmeLinksSelector() {
        return new CMELinkSelector();
    }

    @Bean(name = "edu.stanford.irt.cocoon.pipeline.Transformer/html-cme-links")
    @Scope("prototype")
    public HTMLCMELinkTransformer htmlCMELinkTransformer() {
        return new HTMLCMELinkTransformer();
    }
}
