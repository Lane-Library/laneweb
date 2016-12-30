package edu.stanford.irt.laneweb.config;

import org.apache.solr.client.solrj.SolrClient;
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

    private SolrClient solrClient;

    @Autowired
    public QueryMapConfiguration(@Qualifier("laneSearchSolrServer") final SolrClient solrClient) {
        this.solrClient = solrClient;
    }

    @Bean
    public QueryMapper queryMapper() {
        QueryMapper queryMapper = new QueryMapper();
        DescriptorManager descriptorManager = new DescriptorManager();
        queryMapper.setDescriptorManager(descriptorManager);
        queryMapper.setQueryToDescriptor(new SolrQueryToDescriptor(descriptorManager, queryMapperSolrTemplate()));
        return queryMapper;
    }

    @Bean
    public SolrTemplate queryMapperSolrTemplate() {
        return new SolrTemplate(this.solrClient);
    }
}
