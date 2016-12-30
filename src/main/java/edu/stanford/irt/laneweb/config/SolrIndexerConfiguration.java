package edu.stanford.irt.laneweb.config;

import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.solr.repository.config.EnableSolrRepositories;

@Configuration
@EnableSolrRepositories(basePackages = {
        "edu.stanford.irt.solr.repository.index" }, multicoreSupport = true, solrClientRef = "solrIndexerServer")
public class SolrIndexerConfiguration {

    private String imageIndexerURL;

    public SolrIndexerConfiguration(@Value("%{laneweb.solr-url-indexer-imageSearch}") final String imageIndexerURL) {
        this.imageIndexerURL = imageIndexerURL;
    }

    @Bean(name = "solrIndexerServer")
    public HttpSolrClient solrClient() {
        return new HttpSolrClient(this.imageIndexerURL);
    }
}
