package edu.stanford.irt.laneweb;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TemporaryLinkTableCreator {
    
    private Logger log = LoggerFactory.getLogger(TemporaryLinkTableCreator.class);
    
    private DataSource dataSource;
    
    public void initialize() {
        Connection conn = null;
        Statement stmt = null;
        try {
            conn = this.dataSource.getConnection();
            stmt = conn.createStatement();
            stmt.executeQuery("DROP TABLE USER_LINKS");
            stmt.executeQuery("CREATE TABLE USER_LINKS (SUNETID VARCHAR2(32), URL VARCHAR2(356), LABEL VARCHAR2(64), ORD NUMBER(2))");
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
        } finally {
            JdbcUtils.closeConnection(conn);
            JdbcUtils.closeStatement(stmt);
        }
    }
    
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }
}

