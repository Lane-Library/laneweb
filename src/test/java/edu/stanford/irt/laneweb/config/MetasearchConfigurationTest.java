package edu.stanford.irt.laneweb.config;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

public class MetasearchConfigurationTest {

    private MetasearchConfiguration configuration;

    @Before
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
    public void testMetaSearchManagerSource() {
        assertNotNull(this.configuration.springContextMetaSearchService());
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
    public void testResourceSearchGenerator() {
        assertNotNull(this.configuration.resourceSearchGenerator(null));
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
