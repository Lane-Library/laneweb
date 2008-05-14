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

public class EresourcesSubsetGenerator extends AbstractEresourcesGenerator {

    private static final String BROWSE_SQL_1 = "SELECT ERESOURCE.ERESOURCE_ID, VERSION.VERSION_ID, LINK_ID, TITLE, PUBLISHER,\n"
            + "HOLDINGS, DATES, DESCRIPTION, PROXY, LABEL, URL, INSTRUCTION,\n"
            + "NLSSORT(TITLE,'NLS_SORT=GENERIC_BASELETTER') AS SORT_TITLE\n"
            + "FROM ERESOURCE, VERSION, LINK, SUBSET";

    private static final String BROWSE_SQL_3 = "\nWHERE ERESOURCE.ERESOURCE_ID = VERSION.ERESOURCE_ID\n"
            + "AND VERSION.VERSION_ID = LINK.VERSION_ID\n";

    private static final String BROWSE_SQL_4 = "AND VERSION.VERSION_ID = SUBSET.VERSION_ID\n"
            + "AND SUBSET.SUBSET = '";

    private static final String BROWSE_SQL_8 = "UNION SELECT ERESOURCE.ERESOURCE_ID, VERSION.VERSION_ID, LINK_ID, PREFERRED_TITLE AS TITLE, PUBLISHER,\n"
            + "HOLDINGS, DATES, DESCRIPTION, PROXY, LABEL, URL, INSTRUCTION,\n"
            + "NLSSORT(PREFERRED_TITLE,'NLS_SORT=GENERIC_BASELETTER') AS SORT_TITLE\n"
            + "FROM ERESOURCE, VERSION, LINK, SUBSET";

    private static final String BROWSE_SQL_11 = "AND PREFERRED_TITLE IS NOT NULL\n";

    private static final String BROWSE_SQL_12 = "ORDER BY SORT_TITLE, VERSION_ID, LINK_ID";

    @Override
    protected PreparedStatement getStatement(final Connection conn)
            throws SQLException {
        StringBuffer sb = new StringBuffer(BROWSE_SQL_1);
        sb.append(BROWSE_SQL_3);
        if (null != this.subset) {
            sb.append(BROWSE_SQL_4).append(
                    this.subset.toLowerCase().replaceAll("'", "''")).append(
                    "'\n");
        }
        sb.append(BROWSE_SQL_8);
        sb.append(BROWSE_SQL_3);
        if (null != this.subset) {
            sb.append(BROWSE_SQL_4).append(
                    this.subset.toLowerCase().replaceAll("'", "''")).append(
                    "'\n");
        }
        sb.append(BROWSE_SQL_11);
        sb.append(BROWSE_SQL_12);
        return conn.prepareStatement(sb.toString());
    }

}
