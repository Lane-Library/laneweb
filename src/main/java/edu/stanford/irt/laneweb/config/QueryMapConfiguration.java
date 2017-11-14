package edu.stanford.irt.laneweb.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.ObjectMapper;

import edu.stanford.irt.querymap.DescriptorManager;
import edu.stanford.irt.querymap.DescriptorWeightMap;
import edu.stanford.irt.querymap.JsonSource;
import edu.stanford.irt.querymap.QueryMapper;
import edu.stanford.irt.querymap.QueryToDescriptor;
import edu.stanford.irt.querymap.StreamResourceMapping;

@Configuration
public class QueryMapConfiguration {

    private static final String ENDPOINT =
            "/laneSearch/lane-facet?q=%s&qt=%%2Flane-facet&facet=true&facet.mincount=1&facet.limit=10&facet.field=mesh&wt=json";

    @Bean
    public QueryMapper queryMapper(final ObjectMapper objectMapper,
            @Value("${edu.stanford.irt.laneweb.solr-url-laneSearch}") final String solrServerUrl) {
        DescriptorManager descriptorManager = new DescriptorManager();
        return new QueryMapper(new StreamResourceMapping(QueryMapper.class.getResourceAsStream("resource-maps.xml")),
                descriptorManager,
                new QueryToDescriptor(descriptorManager,
                        new DescriptorWeightMap(QueryMapper.class.getResourceAsStream("descriptor-weights.xml")),
                        objectMapper, new JsonSource(solrServerUrl + ENDPOINT)));
    }
}
