/*
 * Created on Jan 10, 2006
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package edu.stanford.irt.laneweb.eresources;

import java.io.IOException;
import java.util.Map;

import org.apache.avalon.framework.parameters.Parameters;
import org.apache.cocoon.ProcessingException;
import org.apache.cocoon.environment.ObjectModelHelper;
import org.apache.cocoon.environment.Request;
import org.apache.cocoon.environment.SourceResolver;
import org.xml.sax.SAXException;

public class EresourcesSubsetGenerator extends AbstractEresources {

    private static final String BROWSE_SQL_1 =
            "SELECT ERESOURCE2.ERESOURCE_ID, VERSION2.VERSION_ID, LINK_ID, TITLE, PUBLISHER,\n"
                    + "HOLDINGS, DATES, DESCRIPTION, PROXY, LABEL, URL, INSTRUCTION,\n"
                    + "NLSSORT(TITLE,'NLS_SORT=GENERIC_BASELETTER') AS SORT_TITLE\n" + "FROM ERESOURCE2, VERSION2, LINK2, SUBSET2";

    private static final String BROWSE_SQL_3 =
            "\nWHERE ERESOURCE2.ERESOURCE_ID = VERSION2.ERESOURCE_ID\n" + "AND VERSION2.VERSION_ID = LINK2.VERSION_ID\n";

    private static final String BROWSE_SQL_4 = "AND VERSION2.VERSION_ID = SUBSET2.VERSION_ID\n" + "AND SUBSET2.SUBSET = '";

    private static final String BROWSE_SQL_8 =
            "UNION SELECT ERESOURCE2.ERESOURCE_ID, VERSION2.VERSION_ID, LINK_ID, PREFERRED_TITLE AS TITLE, PUBLISHER,\n"
                    + "HOLDINGS, DATES, DESCRIPTION, PROXY, LABEL, URL, INSTRUCTION,\n"
                    + "NLSSORT(PREFERRED_TITLE,'NLS_SORT=GENERIC_BASELETTER') AS SORT_TITLE\n"
                    + "FROM ERESOURCE2, VERSION2, LINK2, SUBSET2";

    private static final String BROWSE_SQL_11 = "AND PREFERRED_TITLE IS NOT NULL\n";

    private static final String BROWSE_SQL_12 = "ORDER BY SORT_TITLE, VERSION_ID, LINK_ID";

    private static final String SUBSET = "s";

    @Override
    public void setup(final SourceResolver resolver, final Map objectModel, final String src, final Parameters par)
            throws ProcessingException, SAXException, IOException {
        super.setup(resolver, objectModel, src, par);
        Request request = ObjectModelHelper.getRequest(objectModel);
        String subset = request.getParameter(SUBSET);
        if (subset != null) {
            if (subset.length() == 0) {
                subset = null;
            } else {
                subset = subset.toLowerCase().replaceAll("'", "''");
            }
        }
        if (null == subset) {
            throw new ProcessingException("no subset parameter provided");
        }
        this.sql = createSQL(subset);
    }

    private String createSQL(final String subset) {
        StringBuffer sb = new StringBuffer(BROWSE_SQL_1);
        sb.append(BROWSE_SQL_3);
        if (null != subset) {
            sb.append(BROWSE_SQL_4).append(subset).append("'\n");
        }
        sb.append(BROWSE_SQL_8);
        sb.append(BROWSE_SQL_3);
        if (null != subset) {
            sb.append(BROWSE_SQL_4).append(subset).append("'\n");
        }
        sb.append(BROWSE_SQL_11);
        sb.append(BROWSE_SQL_12);
        return sb.toString();
    }

}
