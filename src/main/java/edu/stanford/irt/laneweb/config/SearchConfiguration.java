package edu.stanford.irt.laneweb.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import edu.stanford.irt.laneweb.search.DescriptionLabelTransformer;
import edu.stanford.irt.laneweb.search.QueryHighlightingTransformer;

@Configuration
public class SearchConfiguration {

    @Bean(name = "edu.stanford.irt.cocoon.pipeline.Transformer/description-labeling")
    @Scope("prototype")
    public DescriptionLabelTransformer descriptionLabelTransformer() {
        return new DescriptionLabelTransformer();
    }

    @Bean(name = "edu.stanford.irt.cocoon.pipeline.Transformer/query-highlighting")
    @Scope("prototype")
    public QueryHighlightingTransformer queryHighlightingTransformer() {
        return new QueryHighlightingTransformer();
    }
}
