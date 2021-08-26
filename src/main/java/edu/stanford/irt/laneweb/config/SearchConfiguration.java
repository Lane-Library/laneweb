package edu.stanford.irt.laneweb.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import edu.stanford.irt.cocoon.pipeline.Transformer;
import edu.stanford.irt.laneweb.search.DescriptionLabelTransformer;
import edu.stanford.irt.laneweb.search.DescriptionLinkTransformer;
import edu.stanford.irt.laneweb.search.QueryHighlightingTransformer;
import edu.stanford.irt.laneweb.search.SolrQueryHighlightingTransformer;

@Configuration
public class SearchConfiguration {

    @Bean(name = "edu.stanford.irt.cocoon.pipeline.Transformer/description-labeling")
    @Scope("prototype")
    public Transformer descriptionLabelTransformer() {
        return new DescriptionLabelTransformer();
    }

    @Bean(name = "edu.stanford.irt.cocoon.pipeline.Transformer/description-linking")
    @Scope("prototype")
    public Transformer descriptionLinkTransformer() {
        return new DescriptionLinkTransformer();
    }

    @Bean(name = "edu.stanford.irt.cocoon.pipeline.Transformer/query-highlighting")
    @Scope("prototype")
    public Transformer queryHighlightingTransformer() {
        return new QueryHighlightingTransformer();
    }

    @Bean(name = "edu.stanford.irt.cocoon.pipeline.Transformer/solr-query-highlighting")
    @Scope("prototype")
    public Transformer solrHighlightingTransformer() {
        return new SolrQueryHighlightingTransformer();
    }
}
