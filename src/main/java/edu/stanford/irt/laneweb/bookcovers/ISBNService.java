package edu.stanford.irt.laneweb.bookcovers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import edu.stanford.irt.laneweb.LanewebException;

/**
 * <p>
 * A Service that retrieves isbns from the catalog for a given list of bibids. In order to simplify the SQL query it can
 * only take up to 10 bibids.
 * </p>
 */
public class ISBNService {

    private static final String SQL = "SELECT BIB_ID, NORMAL_HEADING FROM LMLDB.BIB_INDEX WHERE BIB_ID IN (?,?,?,?,?,?,?,?,?,?) AND INDEX_CODE = '020N' ORDER BY BIB_ID";

    private DataSource dataSource;

    public ISBNService(final DataSource dataSource) {
        this.dataSource = dataSource;
    }

    private static Map<Integer, List<String>> createISBNMap(final List<Integer> bibids, final PreparedStatement stmt)
            throws SQLException {
        Map<Integer, List<String>> isbnMap = new HashMap<>();
        int i = 1;
        for (int id : bibids) {
            isbnMap.put(Integer.valueOf(id), new ArrayList<>());
            stmt.setInt(i++, id);
        }
        while (i <= 10) {
            stmt.setInt(i++, bibids.get(bibids.size() - 1));
        }
        try (ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                isbnMap.get(Integer.valueOf(rs.getInt(1))).add(rs.getString(2));
            }
        }
        return isbnMap;
    }

    /**
     * @param bibids
     *            a list of bibids
     * @return a Map with bibids as key and a list of isbn Strings as values
     */
    public Map<Integer, List<String>> getISBNs(final List<Integer> bibids) {
        if (bibids.size() > 10) {
            throw new LanewebException(String.format("too many ids: %d", bibids.size()));
        }
        Map<Integer, List<String>> isbnMap;
        try (Connection conn = this.dataSource.getConnection()) {
            try (PreparedStatement stmt = conn.prepareStatement(SQL)) {
                isbnMap = createISBNMap(bibids, stmt);
            }
        } catch (SQLException e) {
            throw new LanewebException(e);
        }
        return isbnMap;
    }
}
