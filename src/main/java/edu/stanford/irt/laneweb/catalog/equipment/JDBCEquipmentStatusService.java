package edu.stanford.irt.laneweb.catalog.equipment;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import edu.stanford.irt.laneweb.LanewebException;
import edu.stanford.lane.catalog.CatalogSQLException;
import edu.stanford.lane.catalog.VoyagerInputStream2;

public class JDBCEquipmentStatusService implements EquipmentService {

    private static final String AVAILABLE_QUERY = "SELECT bi.bib_id, COUNT(*) FROM lmldb.bib_item bi, "
            + "  lmldb.item_status item_status_1 LEFT OUTER JOIN lmldb.item_status item_status_2 "
            + "ON (item_status_1.item_id          = item_status_2.item_id "
            + "AND item_status_1.item_status_date < item_status_2.item_status_date) "
            + "WHERE item_status_2.item_id       IS NULL "
            + "AND bi.item_id                     = item_status_1.item_id " + "AND item_status_1.item_status      = 1 "
            + "AND bi.bib_id in (select regexp_substr(?,'[^,]+', 1, level) from dual connect by regexp_substr(?, '[^,]+', 1, level) is not null) "
            + "GROUP BY bi.bib_id";

    private static final int BIB_ID = 1;

    private static final int COUNT = 2;

    private static final String[] NO_PARAMS = new String[0];

    private DataSource dataSource;

    private String sql;

    public JDBCEquipmentStatusService(final DataSource dataSource, final String sql) throws IOException {
        this.dataSource = dataSource;
        this.sql = sql;
    }

    @Override
    public InputStream getRecords(final List<String> params) {
        return new VoyagerInputStream2(this.dataSource, this.sql, 1, NO_PARAMS) {

            @Override
            public int read() throws IOException {
                try {
                    return super.read();
                } catch (CatalogSQLException e) {
                    throw new LanewebException(e);
                }
            }
        };
    }

    @Override
    public List<EquipmentStatus> getStatus(final String idList) {
        List<EquipmentStatus> status = new ArrayList<>();
        try (Connection conn = this.dataSource.getConnection();
                PreparedStatement stmt = conn.prepareStatement(AVAILABLE_QUERY)) {
            stmt.setString(1, idList);
            stmt.setString(2, idList);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    status.add(new EquipmentStatus(rs.getString(BIB_ID), rs.getString(COUNT)));
                }
            }
        } catch (SQLException e) {
            throw new LanewebException(e);
        }
        return status;
    }
}
