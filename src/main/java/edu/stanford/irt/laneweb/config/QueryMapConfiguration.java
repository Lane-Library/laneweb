package edu.stanford.irt.laneweb.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.solr.core.SolrTemplate;

import edu.stanford.irt.querymap.DescriptorManager;
import edu.stanford.irt.querymap.QueryMapper;
import edu.stanford.irt.querymap.SolrQueryToDescriptor;

@Configuration
public class QueryMapConfiguration {

    private SolrTemplate solrTemplate;

    @Autowired
    public QueryMapConfiguration(@Qualifier("laneSearchSolrTemplate") final SolrTemplate solrTemplate) {
        this.solrTemplate = solrTemplate;
    }

    @Bean
    public QueryMapper queryMapper() {
        QueryMapper queryMapper = new QueryMapper();
        DescriptorManager descriptorManager = new DescriptorManager();
        queryMapper.setDescriptorManager(descriptorManager);
        queryMapper.setQueryToDescriptor(new SolrQueryToDescriptor(descriptorManager, this.solrTemplate));
        return queryMapper;
    }
}
