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

public class EresourcesBrowseGenerator extends AbstractEresourcesGenerator {

    private static final String BROWSE_SQL_1 = "SELECT ERESOURCE2.ERESOURCE_ID, VERSION2.VERSION_ID, LINK_ID, TITLE, PUBLISHER,\n"
            + "HOLDINGS, DATES, DESCRIPTION, PROXY, LABEL, URL, INSTRUCTION,\n"
            + "NLSSORT(TITLE,'NLS_SORT=GENERIC_BASELETTER') AS SORT_TITLE\n"
            + "FROM ERESOURCE2, VERSION2, LINK2";

    private static final String BROWSE_SQL_2 = ", TYPE2";

    private static final String BROWSE_SQL_3 = "\nWHERE ERESOURCE2.ERESOURCE_ID = VERSION2.ERESOURCE_ID\n"
            + "AND VERSION2.VERSION_ID = LINK2.VERSION_ID\n";

    private static final String BROWSE_SQL_4 = "AND ERESOURCE2.ERESOURCE_ID = TYPE2.ERESOURCE_ID\n"
            + "AND TYPE2.TYPE = '";

    private static final String BROWSE_SQL_5 = "AND NLSSORT(SUBSTR(TITLE,1,1),'NLS_SORT=GENERIC_BASELETTER') = NLSSORT('";

    private static final String BROWSE_SQL_6 = "','NLS_SORT=GENERIC_BASELETTER')\n";

    private static final String BROWSE_SQL_7 = "AND (NLSSORT(SUBSTR(TITLE,1,1),'NLS_SORT=GENERIC_BASELETTER') < NLSSORT('A','NLS_SORT=GENERIC_BASELETTER')\n"
            + "OR NLSSORT(SUBSTR(TITLE,1,1),'NLS_SORT=GENERIC_BASELETTER') > NLSSORT('z','NLS_SORT=GENERIC_BASELETTER'))\n";

    private static final String BROWSE_SQL_8 = "UNION SELECT ERESOURCE2.ERESOURCE_ID, VERSION2.VERSION_ID, LINK_ID, PREFERRED_TITLE AS TITLE, PUBLISHER,\n"
            + "HOLDINGS, DATES, DESCRIPTION, PROXY, LABEL, URL, INSTRUCTION,\n"
            + "NLSSORT(PREFERRED_TITLE,'NLS_SORT=GENERIC_BASELETTER') AS SORT_TITLE\n"
            + "FROM ERESOURCE2, VERSION2, LINK2";

    private static final String BROWSE_SQL_9 = "AND NLSSORT(SUBSTR(PREFERRED_TITLE,1,1),'NLS_SORT=GENERIC_BASELETTER') = NLSSORT('";

    private static final String BROWSE_SQL_10 = "AND (NLSSORT(SUBSTR(PREFERRED_TITLE,1,1),'NLS_SORT=GENERIC_BASELETTER') < NLSSORT('A','NLS_SORT=GENERIC_BASELETTER')\n"
            + "OR NLSSORT(SUBSTR(PREFERRED_TITLE,1,1),'NLS_SORT=GENERIC_BASELETTER') > NLSSORT('z','NLS_SORT=GENERIC_BASELETTER'))\n";

    private static final String BROWSE_SQL_11 = "AND PREFERRED_TITLE IS NOT NULL\n";

    private static final String BROWSE_SQL_12 = "ORDER BY SORT_TITLE, VERSION_ID, LINK_ID";

    @Override
    protected PreparedStatement getStatement(final Connection conn)
            throws SQLException {
        String firstAlpha = (null != this.alpha) && (this.alpha.length() > 0) ? this.alpha
                .substring(0, 1)
                : null;
        StringBuffer sb = new StringBuffer(BROWSE_SQL_1);
        if (null != this.type) {
            sb.append(BROWSE_SQL_2);
        }
        sb.append(BROWSE_SQL_3);
        if (null != this.type) {
            sb.append(BROWSE_SQL_4).append(this.type).append("'\n");
        }
        if (null != firstAlpha) {
            if ("#".equals(firstAlpha)) {
                sb.append(BROWSE_SQL_7);
            } else {
                sb.append(BROWSE_SQL_5).append(firstAlpha).append(BROWSE_SQL_6);
            }
        }
        sb.append(BROWSE_SQL_8);
        if (null != this.type) {
            sb.append(BROWSE_SQL_2);
        }
        sb.append(BROWSE_SQL_3);
        if (null != this.type) {
            sb.append(BROWSE_SQL_4).append(this.type).append("'\n");
        }
        if (null != firstAlpha) {
            if ("#".equals(firstAlpha)) {
                sb.append(BROWSE_SQL_10);
            } else {
                sb.append(BROWSE_SQL_9).append(firstAlpha).append(BROWSE_SQL_6);
            }
        }
        sb.append(BROWSE_SQL_11);
        sb.append(BROWSE_SQL_12);
        return conn.prepareStatement(sb.toString());
    }

}
