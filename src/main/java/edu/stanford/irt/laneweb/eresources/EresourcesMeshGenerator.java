/*
 * Created on Jan 10, 2006
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package edu.stanford.irt.laneweb.eresources;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class EresourcesMeshGenerator extends AbstractEresourcesGenerator {

    private static final String MESH_SQL_1 = "SELECT ERESOURCE2.ERESOURCE_ID, VERSION2.VERSION_ID, LINK_ID, TITLE, PUBLISHER,\n"
            + "HOLDINGS, DATES, DESCRIPTION, PROXY, LABEL, URL, INSTRUCTION,\n"
            + "NLSSORT(TITLE,'NLS_SORT=GENERIC_BASELETTER') AS SORT_TITLE\n"
            + "FROM ERESOURCE2, VERSION2, LINK2, MESH2";

    private static final String MESH_SQL_2 = ", TYPE2";

    private static final String MESH_SQL_3 = "\nWHERE ERESOURCE2.ERESOURCE_ID = VERSION2.ERESOURCE_ID\n"
            + "AND VERSION2.VERSION_ID = LINK2.VERSION_ID\n";

    private static final String MESH_SQL_4 = "AND ERESOURCE2.ERESOURCE_ID = MESH2.ERESOURCE_ID\n"
            + "AND MESH2.TERM = '";

    private static final String MESH_SQL_5 = "AND ERESOURCE2.ERESOURCE_ID = TYPE2.ERESOURCE_ID\n"
            + "AND TYPE2.TYPE = '";

    private static final String MESH_SQL_8 = "UNION SELECT ERESOURCE2.ERESOURCE_ID, VERSION2.VERSION_ID, LINK_ID, PREFERRED_TITLE AS TITLE, PUBLISHER,\n"
            + "HOLDINGS, DATES, DESCRIPTION, PROXY, LABEL, URL, INSTRUCTION,\n"
            + "NLSSORT(PREFERRED_TITLE,'NLS_SORT=GENERIC_BASELETTER') AS SORT_TITLE\n"
            + "FROM ERESOURCE2, VERSION2, LINK2, MESH2";

    private static final String MESH_SQL_11 = "AND PREFERRED_TITLE IS NOT NULL\n";

    private static final String MESH_SQL_12 = "ORDER BY SORT_TITLE, VERSION_ID, LINK_ID";

    @Override
    protected PreparedStatement getStatement(final Connection conn)
            throws SQLException {
        StringBuffer sb = new StringBuffer(MESH_SQL_1);
        if (null != this.type) {
            sb.append(MESH_SQL_2);
        }
        sb.append(MESH_SQL_3);
        if (null != this.mesh) {
            sb.append(MESH_SQL_4).append(
                    this.mesh.toLowerCase().replaceAll("'", "''"))
                    .append("'\n");
        }
        if (null != this.type) {
            sb.append(MESH_SQL_5).append(
                    this.type.toLowerCase().replaceAll("'", "''"))
                    .append("'\n");
        }
        sb.append(MESH_SQL_8);
        if (null != this.type) {
            sb.append(MESH_SQL_2);
        }
        sb.append(MESH_SQL_3);
        if (null != this.mesh) {
            sb.append(MESH_SQL_4).append(
                    this.mesh.toLowerCase().replaceAll("'", "''"))
                    .append("'\n");
        }
        if (null != this.type) {
            sb.append(MESH_SQL_5).append(
                    this.type.toLowerCase().replaceAll("'", "''"))
                    .append("'\n");
        }
        sb.append(MESH_SQL_11);
        sb.append(MESH_SQL_12);
        return conn.prepareStatement(sb.toString());
    }

}
