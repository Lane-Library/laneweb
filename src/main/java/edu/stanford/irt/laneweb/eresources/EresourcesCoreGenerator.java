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

public class EresourcesCoreGenerator extends AbstractEresources {

    private static final String CORE_SQL_1 =
            "SELECT ERESOURCE2.ERESOURCE_ID, VERSION2.VERSION_ID, LINK_ID, TITLE, PUBLISHER,\n"
                    + "HOLDINGS, DATES, DESCRIPTION, PROXY, LABEL, URL, INSTRUCTION,\n"
                    + "NLSSORT(TITLE,'NLS_SORT=GENERIC_BASELETTER') AS SORT_TITLE\n" + "FROM ERESOURCE2, VERSION2, LINK2";

    private static final String CORE_SQL_2 = ", TYPE2";

    private static final String CORE_SQL_3 =
            "\nWHERE ERESOURCE2.ERESOURCE_ID = VERSION2.ERESOURCE_ID\n" + "AND VERSION2.VERSION_ID = LINK2.VERSION_ID\n"
                    + "AND ERESOURCE2.CORE = 'Y'";

    private static final String CORE_SQL_5 = "AND ERESOURCE2.ERESOURCE_ID = TYPE2.ERESOURCE_ID\n" + "AND TYPE2.TYPE = '";

    private static final String CORE_SQL_8 =
            "UNION SELECT ERESOURCE2.ERESOURCE_ID, VERSION2.VERSION_ID, LINK_ID, PREFERRED_TITLE AS TITLE, PUBLISHER,\n"
                    + "HOLDINGS, DATES, DESCRIPTION, PROXY, LABEL, URL, INSTRUCTION,\n"
                    + "NLSSORT(PREFERRED_TITLE,'NLS_SORT=GENERIC_BASELETTER') AS SORT_TITLE\n" + "FROM ERESOURCE2, VERSION2, LINK2";

    private static final String CORE_SQL_11 = "AND PREFERRED_TITLE IS NOT NULL\n";

    private static final String CORE_SQL_12 = "ORDER BY SORT_TITLE, VERSION_ID, LINK_ID";

    private static final String TYPE = "t";

   
    @Override
    public void setup(final SourceResolver resolver, final Map objectModel, final String src, final Parameters par)
            throws ProcessingException, SAXException, IOException {
        super.setup(resolver, objectModel, src, par);
        Request request = ObjectModelHelper.getRequest(objectModel);
        String type = request.getParameter(TYPE);
        if (type != null) {
            if (type.length() == 0) {
                type = null;
            } else {
                type = type.toLowerCase().replaceAll("'", "''");
            }
        }
        this.sql = createSQL(type);
    }

  

    private String createSQL(final String type) {
        StringBuffer sb = new StringBuffer(CORE_SQL_1);
        if (null != type) {
            sb.append(CORE_SQL_2);
        }
        sb.append(CORE_SQL_3);
        if (null != type) {
            sb.append(CORE_SQL_5).append(type).append("'\n");
        }
        sb.append(CORE_SQL_8);
        if (null != type) {
            sb.append(CORE_SQL_2);
        }
        sb.append(CORE_SQL_3);
        if (null != type) {
            sb.append(CORE_SQL_5).append(type).append("'\n");
        }
        sb.append(CORE_SQL_11);
        sb.append(CORE_SQL_12);
        return sb.toString();
    }

   
}
