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
import java.util.List;

import org.apache.cocoon.ProcessingException;

import edu.stanford.irt.eresources.Eresource;

public class EresourcesSearchGenerator extends AbstractEresourcesGenerator {

    private static final String SEARCH_SQL_1 = "WITH FOUND AS (\n"
            + "SELECT TITLE, ERESOURCE.ERESOURCE_ID, SCORE(1) * 3 AS SCORE_TEXT,\n"
            + "CONTAINS(TITLE,?) * 3 AS SCORE_TITLE\n"
            + "FROM ERESOURCE\n"
            + "WHERE CONTAINS(TEXT,?,1) > 0\n"
            + "AND CORE = 'Y'\n"
            + "UNION\n"
            + "SELECT PREFERRED_TITLE AS TITLE, ERESOURCE.ERESOURCE_ID, SCORE(1) * 3 AS SCORE_TEXT,\n"
            + "CONTAINS(PREFERRED_TITLE,?) * 3 AS SCORE_TITLE\n"
            + "FROM ERESOURCE\n"
            + "WHERE CONTAINS(TEXT,?,1) > 0\n"
            + "AND CORE = 'Y'\n"
            + "AND PREFERRED_TITLE IS NOT NULL\n"
            + "UNION\n"
            + "SELECT TITLE, ERESOURCE.ERESOURCE_ID, SCORE(1) AS SCORE_TEXT,\n"
            + "CONTAINS(TITLE,?) AS SCORE_TITLE\n"
            + "FROM ERESOURCE\n"
            + "WHERE CONTAINS(TEXT,?,1) > 0\n"
            + "AND CORE IS NULL\n"
            + "UNION\n"
            + "SELECT PREFERRED_TITLE AS TITLE, ERESOURCE.ERESOURCE_ID,\n"
            + "SCORE(1) AS SCORE_TEXT, CONTAINS(PREFERRED_TITLE,?) AS SCORE_TITLE\n"
            + "FROM ERESOURCE\n"
            + "WHERE CONTAINS(TEXT,?,1) > 0\n"
            + "AND CORE IS NULL\n"
            + "AND PREFERRED_TITLE IS NOT NULL\n"
            + ")\n"
            + "SELECT FOUND.ERESOURCE_ID, VERSION.VERSION_ID, LINK_ID, TYPE, SUBSET, TITLE, PUBLISHER,\n"
            + "HOLDINGS, DATES, DESCRIPTION, PROXY, LABEL, URL, INSTRUCTION,\n"
            + "SCORE_TITLE, SCORE_TEXT, NLSSORT(TITLE,'NLS_SORT=GENERIC_BASELETTER') AS SORT_TITLE\n"
            + "FROM FOUND, VERSION, LINK, TYPE, SUBSET\n"
            + "WHERE FOUND.ERESOURCE_ID = VERSION.ERESOURCE_ID\n"
            + "AND VERSION.VERSION_ID = LINK.VERSION_ID\n"
            + "AND FOUND.ERESOURCE_ID = TYPE.ERESOURCE_ID(+)\n"
            + "AND VERSION.VERSION_ID = SUBSET.VERSION_ID(+)\n";

    public static final String SEARCH_SQL_2 = "ORDER BY SCORE_TITLE DESC, SCORE_TEXT DESC, SORT_TITLE, VERSION_ID, LINK_ID";

    private QueryTranslator queryTranslator = new QueryTranslator();

    @Override
    protected PreparedStatement getStatement(final Connection conn)
            throws SQLException {
        String translatedQuery = this.queryTranslator.translate(this.query);
        StringBuffer sb = new StringBuffer(SEARCH_SQL_1);
        if (null != this.subset) {
            sb.append("AND SUBSET ='").append(
                    this.subset.toLowerCase().replaceAll("'", "''")).append(
                    "'\n");
        }
        if (null != this.type) {
            sb.append("AND TYPE = '").append(
                    this.type.toLowerCase().replaceAll("'", "''"))
                    .append("'\n");
        }
        sb.append(SEARCH_SQL_2);
        PreparedStatement stmt = conn.prepareStatement(sb.toString());
        stmt.setString(1, translatedQuery);
        stmt.setString(2, translatedQuery);
        stmt.setString(3, translatedQuery);
        stmt.setString(4, translatedQuery);
        stmt.setString(5, translatedQuery);
        stmt.setString(6, translatedQuery);
        stmt.setString(7, translatedQuery);
        stmt.setString(8, translatedQuery);
        return stmt;
    }

    @Override
    protected List<Eresource> getEresourceList() throws ProcessingException {
        List<Eresource> eresources = super.getEresourceList();
        for (int i = 0; (i < eresources.size()) && (i < 10); i++) {
            if (this.query.equalsIgnoreCase(eresources.get(i).getTitle())) {
                eresources.add(0, eresources.remove(i));
                break;
            }
        }
        return eresources;
    }

}
