package edu.stanford.irt.laneweb.links;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.stanford.irt.eresources.Link;
import edu.stanford.irt.eresources.impl.LinkImpl;
import edu.stanford.irt.laneweb.util.JdbcUtils;

public class SQLLinkListDAO implements LinkListDAO {

    private static final String ADD_LINK = "INSERT INTO USER_LINKS (SUNETID, URL, LABEL, ORD) VALUES(?,?,?,?)";

    private static final String GET_LINKS = "SELECT * FROM USER_LINKS WHERE SUNETID = ? ORDER BY ORD";

    private static final String REMOVE_LINKS = "DELETE FROM USER_LINKS WHERE SUNETID = ?";

    private DataSource dataSource;

    private Logger log = LoggerFactory.getLogger(SQLLinkListDAO.class);

    public LinkList getLinks(final String sunetid) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            LinkList links = new LinkList();
            conn = this.dataSource.getConnection();
            stmt = conn.prepareStatement(GET_LINKS);
            stmt.setString(1, sunetid);
            rs = stmt.executeQuery();
            while (rs.next()) {
                LinkImpl link = new LinkImpl();
                link.setUrl(rs.getString("URL"));
                link.setLabel(rs.getString("LABEL"));
            }
            return links;
        } catch (SQLException e) {
            this.log.error(e.getMessage(), e);
            return null;
        } finally {
            JdbcUtils.closeConnection(conn);
            JdbcUtils.closeStatement(stmt);
            JdbcUtils.closeResultSet(rs);
        }
    }

    public void saveLinks(final String sunetid, final List<Link> links) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = this.dataSource.getConnection();
            conn.setAutoCommit(false);
            stmt = conn.prepareStatement(REMOVE_LINKS);
            stmt.setString(1, sunetid);
            stmt.executeUpdate();
            stmt = conn.prepareStatement(ADD_LINK);
            int i = 0;
            for (Link link : links) {
                stmt.setString(1, sunetid);
                stmt.setString(2, link.getUrl());
                stmt.setString(3, link.getLabel());
                stmt.setInt(4, i++);
                stmt.addBatch();
            }
            stmt.executeBatch();
            conn.commit();
        } catch (SQLException e) {
            this.log.error(e.getMessage(), e);
            try {
                conn.rollback();
            } catch (SQLException e1) {
                this.log.error(e1.getMessage(), e1);
            }
        } finally {
            JdbcUtils.closeConnection(conn);
            JdbcUtils.closeStatement(stmt);
            JdbcUtils.closeResultSet(rs);
        }
    }

    public void setDataSource(final DataSource dataSource) {
        this.dataSource = dataSource;
    }
}
