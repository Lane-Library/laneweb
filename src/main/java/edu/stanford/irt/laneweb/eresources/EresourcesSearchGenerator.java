/*
 * Created on Jan 10, 2006
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package edu.stanford.irt.laneweb.eresources;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.Map;

import org.apache.avalon.framework.parameters.Parameters;
import org.apache.cocoon.ProcessingException;
import org.apache.cocoon.environment.ObjectModelHelper;
import org.apache.cocoon.environment.Request;
import org.apache.cocoon.environment.SourceResolver;
import org.xml.sax.SAXException;

public class EresourcesSearchGenerator extends AbstractEresources {

    public static void main(final String[] args) {
        System.out.println(SEARCH_SQL_1.replaceAll("{0}", "(({cardiology}))") + SEARCH_SQL_2);
    }

    private static final String SEARCH_SQL_1 =
        "WITH FOUND AS (\n" + "SELECT TITLE, ERESOURCE2.ERESOURCE_ID, SCORE(1) * 3 AS SCORE_TEXT,\n"
        + "CONTAINS(TITLE,{0}) * 3 AS SCORE_TITLE\n" + "FROM ERESOURCE2\n" + "WHERE CONTAINS(TEXT,{0},1) > 0\n"
        + "AND CORE = ''Y''\n" + "UNION\n"
        + "SELECT PREFERRED_TITLE AS TITLE, ERESOURCE2.ERESOURCE_ID, SCORE(1) * 3 AS SCORE_TEXT,\n"
        + "CONTAINS(PREFERRED_TITLE,{0}) * 3 AS SCORE_TITLE\n" + "FROM ERESOURCE2\n"
        + "WHERE CONTAINS(TEXT,{0},1) > 0\n" + "AND CORE = ''Y''\n" + "AND PREFERRED_TITLE IS NOT NULL\n" + "UNION\n"
        + "SELECT TITLE, ERESOURCE2.ERESOURCE_ID, SCORE(1) AS SCORE_TEXT,\n" + "CONTAINS(TITLE,{0}) AS SCORE_TITLE\n"
        + "FROM ERESOURCE2\n" + "WHERE CONTAINS(TEXT,{0},1) > 0\n" + "AND CORE IS NULL\n" + "UNION\n"
        + "SELECT PREFERRED_TITLE AS TITLE, ERESOURCE2.ERESOURCE_ID,\n"
        + "SCORE(1) AS SCORE_TEXT, CONTAINS(PREFERRED_TITLE,{0}) AS SCORE_TITLE\n" + "FROM ERESOURCE2\n"
        + "WHERE CONTAINS(TEXT,{0},1) > 0\n" + "AND CORE IS NULL\n" + "AND PREFERRED_TITLE IS NOT NULL\n" + ")\n"
        + "SELECT FOUND.ERESOURCE_ID, VERSION2.VERSION_ID, LINK_ID, TYPE, SUBSET, TITLE, PUBLISHER,\n"
        + "HOLDINGS, DATES, DESCRIPTION, PROXY, LABEL, URL, INSTRUCTION,\n"
        + "SCORE_TITLE, SCORE_TEXT, NLSSORT(TITLE,''NLS_SORT=GENERIC_BASELETTER'') AS SORT_TITLE\n"
        + "FROM FOUND, VERSION2, LINK2, TYPE2, SUBSET2\n" + "WHERE FOUND.ERESOURCE_ID = VERSION2.ERESOURCE_ID\n"
        + "AND VERSION2.VERSION_ID = LINK2.VERSION_ID\n" + "AND FOUND.ERESOURCE_ID = TYPE2.ERESOURCE_ID(+)\n"
        + "AND VERSION2.VERSION_ID = SUBSET2.VERSION_ID(+)\n";


    public static final String SEARCH_SQL_2 = "ORDER BY SCORE_TITLE DESC, SCORE_TEXT DESC, SORT_TITLE, VERSION_ID, LINK_ID";

    private static final String KEYWORDS = "keywords";

    private static final String QUERY = "q";

    private static final String TYPE = "t";

    private static final String SUBSET = "s";

    private QueryTranslator queryTranslator = new QueryTranslator();
    
    private MessageFormat selectStatement = new MessageFormat(SEARCH_SQL_1);
	

    @Override
    public void setup(final SourceResolver resolver, final Map objectModel, final String src, final Parameters par)
            throws ProcessingException, SAXException, IOException {
        super.setup(resolver, objectModel, src, par);
        Request request = ObjectModelHelper.getRequest(objectModel);
        String query = request.getParameter(QUERY);
        if (null == query) {
            query = request.getParameter(KEYWORDS);
        }
        String type = request.getParameter(TYPE);
        if (null != type) {
            if (type.length() == 0) {
                type = null;
            } else {
                type = type.toLowerCase().replaceAll("'", "''");
            }
        }
        String subset = request.getParameter(SUBSET);
        if (null != subset) {
            if (subset.length() == 0) {
                subset = null;
            } else {
                subset = subset.toLowerCase();
            }
        }
        if (null != query) {
            query = query.trim();
            if (query.length() == 0 || "%".equals(query) || "*".equals(query)) {
                query = null;
            }

        }
        if (null == query) {
            this.sql = "";
        } else {
            query = query.replaceAll("'", "''");
            String translatedQuery = this.queryTranslator.translate(query);
            if (translatedQuery.indexOf("()") == -1 && translatedQuery.indexOf("{}") == -1 && translatedQuery.indexOf("NOT") != 1) {
	           Object[] argument = {"'".concat(translatedQuery).concat("'")};  
	        	StringBuffer sb = new StringBuffer( selectStatement.format(argument));
            	if (null != subset) {
                    sb.append("AND SUBSET ='").append(subset).append("'\n");
                }
                if (null != type) {
                    sb.append("AND TYPE = '").append(type).append("'\n");
                }
                sb.append(SEARCH_SQL_2);
                this.sql = sb.toString();
            } else {
                this.sql = "";
            }
        }
    }

}
