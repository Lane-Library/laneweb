package edu.stanford.irt.laneweb.config;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.solr.repository.config.EnableSolrRepositories;

@Configuration
@EnableSolrRepositories(basePackages = {
        "edu.stanford.irt.solr.repository.index" }, multicoreSupport = true, solrClientRef = "solrIndexerServer")
public class SolrIndexerConfiguration {

    private static final int SOLR_CONNECT_TIMEOUT = 5_000;

    private static final int SOLR_READ_TIMEOUT = 15_000;

    @Bean(name = "solrIndexerServer")
    public SolrClient solrClient(@Value("${laneweb.solr-url-indexer-imageSearch}") final String imageIndexerURL) {
        HttpSolrClient solrClient = new HttpSolrClient(imageIndexerURL);
        solrClient.setConnectionTimeout(SOLR_CONNECT_TIMEOUT);
        solrClient.setSoTimeout(SOLR_READ_TIMEOUT);
        return solrClient;
    }
}
