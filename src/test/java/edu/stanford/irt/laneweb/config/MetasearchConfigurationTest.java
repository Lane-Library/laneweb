package edu.stanford.irt.laneweb.config;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.net.URISyntaxException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class MetasearchConfigurationTest {

    private MetasearchConfiguration configuration;

    @BeforeEach
    public void setUp() {
        this.configuration = new MetasearchConfiguration();
    }

    @Test
    public void testAllClinicalSearchResultsGenerator() {
        assertNotNull(this.configuration.allClinicalSearchResultsGenerator(null, null));
    }

    @Test
    public void testClinicalSearchResultsFactory() {
        assertNotNull(this.configuration.clinicalSearchResultsFactory(null));
    }

    @Test
    public void testClinicalSearchResultsSAXStrategy() {
        assertNotNull(this.configuration.clinicalSearchResultsSAXStrategy());
    }

    @Test
    public void testContentResultConversionStrategy() {
        assertNotNull(this.configuration.contentResultConversionStrategy());
    }

    @Test
    public void testContentSearchGenerator() {
        assertNotNull(this.configuration.contentSearchGenerator(null, null));
    }

    @Test
    public void testDescribeGenerator() {
        assertNotNull(this.configuration.describeGenerator(null));
    }

    @Test
    public void testEngineSearchGenerator() {
        assertNotNull(this.configuration.engineSearchGenerator(null));
    }

    @Test
    public void testFilePathTransformer() {
        assertNotNull(this.configuration.filePathTransformer(null, null));
    }

    @Test
    public void testMetasearchResultSAXStrategy() {
        assertNotNull(this.configuration.metasearchResultSAXStrategy());
    }

    @Test
    public void testPagingSearchResultListSAXStrategy() {
        assertNotNull(this.configuration.pagingSearchResultListSAXStrategy());
    }

    @Test
    public void testPedsClinicalSearchResultsGenerator() {
        assertNotNull(this.configuration.pedsClinicalSearchResultsGenerator(null, null));
    }

    @Test
    public void testRestMetaSearchService() throws URISyntaxException {
        assertNotNull(this.configuration.restMetaSearchService(null, null, null, 0, null));
    }

    @Test
    public void testSearchDirectoryTransformer() {
        assertNotNull(this.configuration.searchDirectoryTransformer());
    }

    @Test
    public void testSearchGenerator() {
        assertNotNull(this.configuration.searchGenerator(null));
    }
}
