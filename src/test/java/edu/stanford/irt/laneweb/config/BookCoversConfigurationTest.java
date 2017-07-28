package edu.stanford.irt.laneweb.config;

import static org.easymock.EasyMock.createMock;
import static org.junit.Assert.assertNotNull;

import javax.sql.DataSource;

import org.junit.Before;
import org.junit.Test;

public class BookCoversConfigurationTest {

    private BookCoversConfiguration configuration;

    private DataSource dataSource;

    @Before
    public void setUp() {
        this.dataSource = createMock(DataSource.class);
        this.configuration = new BookCoversConfiguration();
    }

    @Test
    public void testBookCoverService() {
        assertNotNull(this.configuration.bookCoverService(null, null, null));
    }

    @Test
    public void testHttpISBNService() {
        assertNotNull(this.configuration.httpISBNService(null, null));
    }

    @Test
    public void testJdbcISBNService() {
        this.configuration.jdbcISBNService(this.dataSource);
        assertNotNull(this.configuration.jdbcISBNService(this.dataSource));
    }
}
