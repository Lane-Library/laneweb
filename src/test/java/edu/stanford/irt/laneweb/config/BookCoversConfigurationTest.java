package edu.stanford.irt.laneweb.config;

import static org.easymock.EasyMock.createMock;
import static org.junit.Assert.assertNotNull;

import javax.sql.DataSource;

import org.junit.Before;
import org.junit.Test;

public class BookCoversConfigurationTest {

    private String apiKey;

    private BookCoversConfiguration configuration;

    private DataSource dataSource;

    @Before
    public void setUp() {
        this.dataSource = createMock(DataSource.class);
        this.apiKey = "apiKey";
        this.configuration = new BookCoversConfiguration(this.dataSource, this.dataSource, this.apiKey);
    }

    @Test
    public void testBookCoverService() {
        assertNotNull(this.configuration.bookCoverService());
    }
}
