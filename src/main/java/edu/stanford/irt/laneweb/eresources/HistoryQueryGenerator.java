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
import org.apache.cocoon.generation.AbstractGenerator;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

public class HistoryQueryGenerator extends AbstractGenerator {

    // ERESOURCE.TITLE, lower(ERESOURCE.TITLE) AS LTITLE,
    private static final String COUNT_QUERY_1 =
            "WITH FOUND AS (SELECT HISTORY_ERESOURCE.ERESOURCE_ID FROM HISTORY_ERESOURCE \n"
                    + "WHERE CONTAINS(HISTORY_ERESOURCE.TEXT,'";

    private static final String COUNT_QUERY_2 =
            "') > 0) \n" + "SELECT COUNT(*) AS HITS, 'books' AS GENRE FROM FOUND, HISTORY_TYPE\n"
                    + "WHERE FOUND.ERESOURCE_ID = HISTORY_TYPE.ERESOURCE_ID AND TYPE = 'books'\n" + "UNION\n"
                    + "SELECT COUNT(*) AS HITS, 'movie' AS GENRE FROM FOUND, HISTORY_TYPE\n"
                    + "WHERE FOUND.ERESOURCE_ID = HISTORY_TYPE.ERESOURCE_ID AND TYPE = 'movie'\n" + "UNION\n"
                    + "SELECT COUNT(*) AS HITS, 'serial' AS GENRE FROM FOUND, HISTORY_TYPE\n"
                    + "WHERE FOUND.ERESOURCE_ID = HISTORY_TYPE.ERESOURCE_ID AND TYPE = 'serial'\n" + "UNION\n"
                    + "SELECT COUNT(*) AS HITS, 'graphic' AS GENRE FROM FOUND, HISTORY_TYPE\n"
                    + "WHERE FOUND.ERESOURCE_ID = HISTORY_TYPE.ERESOURCE_ID AND TYPE = 'graphic'\n" + "UNION\n"
                    + "SELECT COUNT(*) AS HITS, 'thesis' AS GENRE FROM FOUND, HISTORY_TYPE\n"
                    + "WHERE FOUND.ERESOURCE_ID = HISTORY_TYPE.ERESOURCE_ID AND TYPE = 'thesis'\n" + "UNION\n"
                    + "SELECT COUNT(*) AS HITS, 'object' AS GENRE FROM FOUND, HISTORY_TYPE\n"
                    + "WHERE FOUND.ERESOURCE_ID = HISTORY_TYPE.ERESOURCE_ID AND TYPE = 'object'\n" + "UNION\n"
                    + "SELECT COUNT(*) AS HITS, 'people' AS GENRE FROM FOUND, HISTORY_TYPE\n"
                    + "WHERE FOUND.ERESOURCE_ID = HISTORY_TYPE.ERESOURCE_ID AND TYPE = 'people'\n" + "UNION\n"
                    + "SELECT COUNT(*) AS HITS, 'organization' AS GENRE FROM FOUND, HISTORY_TYPE\n"
                    + "WHERE FOUND.ERESOURCE_ID = HISTORY_TYPE.ERESOURCE_ID AND TYPE = 'organization'\n" + "UNION\n"
                    + "SELECT COUNT(*) AS HITS, 'article' AS GENRE FROM FOUND, HISTORY_TYPE\n"
                    + "WHERE FOUND.ERESOURCE_ID = HISTORY_TYPE.ERESOURCE_ID AND TYPE = 'article'\n" + "UNION\n"
                    + "SELECT COUNT(*) AS HITS, 'chapter' AS GENRE FROM FOUND, HISTORY_TYPE\n"
                    + "WHERE FOUND.ERESOURCE_ID = HISTORY_TYPE.ERESOURCE_ID AND TYPE = 'chapter'\n" + "UNION\n"
                    + "SELECT COUNT(*) AS HITS, 'event' AS GENRE FROM FOUND, HISTORY_TYPE\n"
                    + "WHERE FOUND.ERESOURCE_ID = HISTORY_TYPE.ERESOURCE_ID AND TYPE = 'event'\n" + "UNION\n"
                    + "SELECT COUNT(*) AS HITS, 'findingAid' AS GENRE FROM FOUND, HISTORY_TYPE\n"
                    + "WHERE FOUND.ERESOURCE_ID = HISTORY_TYPE.ERESOURCE_ID AND TYPE = 'finding aid'\n";

    private static final String TYPE = "t";

    private static final String ALPHA = "a";

    private static final String KEYWORDS = "keywords";

    private static final String QUERY = "q";

    private static final String CORE = "c";

    private static final String XMLNS = "http://apache.org/cocoon/SQL/2.0";

    private static final String EXECUTE_QUERY_ELEMENT = "execute-query";

    private static final String QUERY_ELEMENT = "query";

    private static final String SELECT =
            "SELECT " + "HISTORY_LINK.LINK_ID, " + "HISTORY_ERESOURCE.ERESOURCE_ID, " + "HISTORY_VERSION.VERSION_ID, "
                    + "HISTORY_VERSION.PUBLISHER, " + "HISTORY_VERSION.HOLDINGS, " + "HISTORY_VERSION.DATES, "
                    + "HISTORY_VERSION.DESCRIPTION, " + "HISTORY_VERSION.PROXY, " + "HISTORY_LINK.URL, " + "HISTORY_LINK.LABEL, "
                    + "HISTORY_LINK.INSTRUCTION, ";

    private static final String FROM = "FROM " + "HISTORY_ERESOURCE, HISTORY_VERSION, HISTORY_LINK";

    private static final String WHERE =
            "WHERE " + "HISTORY_ERESOURCE.ERESOURCE_ID = HISTORY_VERSION.ERESOURCE_ID " + "AND "
                    + "HISTORY_VERSION.VERSION_ID = HISTORY_LINK.VERSION_ID ";

    private static final String ORDER_BY = "\nORDER BY ";

    private static final String ORDER = " STITLE, LINK_ID";

    private static final Attributes EMPTY_ATTS = new AttributesImpl();

    private String type;

    private String alpha;

    private String core;

    private String query;

    private String translatedQuery;

    private boolean count;

    private boolean haveParameters;

    private String coreWeight;

    private QueryTranslator queryTranslator = new QueryTranslator();

    @Override
    public void setup(final SourceResolver resolver, final Map objectModel, final String src, final Parameters par)
            throws ProcessingException, SAXException, IOException {
        super.setup(resolver, objectModel, src, par);
        this.coreWeight = par.getParameter("core-weight", "3");
        Request request = ObjectModelHelper.getRequest(objectModel);
        this.haveParameters = false;
        this.count = request.getRequestURI().indexOf("count") > 0;
        this.core = request.getParameter(CORE);
        if (null != this.core) {
            if (this.core.equals("y")) {
                this.haveParameters = true;
            } else {
                this.core = null;
            }
        }
        this.type = request.getParameter(TYPE);
        if (null != this.type) {
            if (this.type.length() == 0) {
                this.type = null;
            } else {
                this.type = this.type.toLowerCase();
                this.haveParameters = true;
            }
        }
        this.alpha = request.getParameter(ALPHA);
        if (null != this.alpha) {
            if (this.alpha.length() == 0) {
                this.alpha = null;
            } else {
                this.alpha = this.alpha.substring(0, 1).toLowerCase();
                this.haveParameters = true;
            }
        }
        this.query = request.getParameter(QUERY);
        if (null == this.query) {
            this.query = request.getParameter(KEYWORDS);
        }
        if (null != this.query) {
            if (this.query.length() == 0) {
                this.query = null;
            } else {
                try {
                    this.translatedQuery = this.queryTranslator.translate(this.query);
                    this.haveParameters = true;
                } catch (Exception e) {
                    throw new ProcessingException(e);
                }
            }
        }
    }

    public void generate() throws SAXException {
        this.xmlConsumer.startDocument();
        if (this.haveParameters) {
            char[] selectStatmentChars = getSelectStatmentChars();
            this.xmlConsumer.startElement(XMLNS, EXECUTE_QUERY_ELEMENT, EXECUTE_QUERY_ELEMENT, EMPTY_ATTS);
            this.xmlConsumer.startElement(XMLNS, QUERY_ELEMENT, QUERY_ELEMENT, EMPTY_ATTS);
            this.xmlConsumer.characters(selectStatmentChars, 0, selectStatmentChars.length);
            this.xmlConsumer.endElement(XMLNS, QUERY_ELEMENT, QUERY_ELEMENT);
            this.xmlConsumer.endElement(XMLNS, EXECUTE_QUERY_ELEMENT, EXECUTE_QUERY_ELEMENT);
        }
        this.xmlConsumer.endDocument();
    }

    @Override
    public void recycle() {
        this.type = null;
        this.alpha = null;
        this.query = null;
        this.translatedQuery = null;
        super.recycle();
    }

    private char[] getSelectStatmentChars() {
        if (this.count) {
            StringBuffer sb = new StringBuffer(COUNT_QUERY_1);
            sb.append(this.translatedQuery);
            sb.append(COUNT_QUERY_2);
            return sb.toString().toCharArray();
        }
        StringBuffer queryBuffer = new StringBuffer();
        if (null != this.translatedQuery) {
            getScoredSelectSQL(queryBuffer, "HISTORY_ERESOURCE.TITLE", true);
            getFromSQL(queryBuffer);
            getScoredWhereSQL(queryBuffer, "HISTORY_ERESOURCE.TITLE", true);
            queryBuffer.append("\nUNION\n");
            getScoredSelectSQL(queryBuffer, "HISTORY_ERESOURCE.TITLE", false);
            getFromSQL(queryBuffer);
            getScoredWhereSQL(queryBuffer, "HISTORY_ERESOURCE.TITLE", false);
            queryBuffer.append("\nUNION\n");
            getScoredSelectSQL(queryBuffer, "HISTORY_ERESOURCE.PREFERRED_TITLE", true);
            getFromSQL(queryBuffer);
            getScoredWhereSQL(queryBuffer, "HISTORY_ERESOURCE.PREFERRED_TITLE", true);
            queryBuffer.append("\nUNION\n");
            getScoredSelectSQL(queryBuffer, "HISTORY_ERESOURCE.PREFERRED_TITLE", false);
            getFromSQL(queryBuffer);
            getScoredWhereSQL(queryBuffer, "HISTORY_ERESOURCE.PREFERRED_TITLE", false);
            getOrderBySQL(queryBuffer);
        } else {
            getSelectSQL(queryBuffer, "HISTORY_ERESOURCE.TITLE");
            getFromSQL(queryBuffer);
            getWhereSQL(queryBuffer, "HISTORY_ERESOURCE.TITLE");
            queryBuffer.append("\nUNION\n");
            getSelectSQL(queryBuffer, "HISTORY_ERESOURCE.PREFERRED_TITLE");
            getFromSQL(queryBuffer);
            getWhereSQL(queryBuffer, "HISTORY_ERESOURCE.PREFERRED_TITLE");
            getOrderBySQL(queryBuffer);
        }
        return queryBuffer.toString().toCharArray();
    }

    private void getScoredSelectSQL(final StringBuffer queryBuffer, final String titleTable, final boolean coreWeighting) {
        getSelectSQL(queryBuffer, titleTable);
        queryBuffer.append(", CONTAINS(").append(titleTable).append(",'").append(this.translatedQuery).append("',1)");
        if (coreWeighting) {
            queryBuffer.append(" * ").append(this.coreWeight);
        }
        queryBuffer.append(" AS SCORE_TITLE").append(", SCORE(2) AS SCORE_TEXT");
    }

    private void getSelectSQL(final StringBuffer queryBuffer, final String titleTable) {
        queryBuffer.append(SELECT).append(titleTable).append(", lower(").append(titleTable).append(") AS LTITLE")
                   .append(", NLSSORT(").append(titleTable).append(",'NLS_SORT=GENERIC_BASELETTER') AS STITLE");
    }

    private void getFromSQL(final StringBuffer queryBuffer) {
        queryBuffer.append('\n');
        queryBuffer.append(FROM);
        if (null != this.type) {
            queryBuffer.append(", HISTORY_TYPE");
        }
    }

    private void getScoredWhereSQL(final StringBuffer queryBuffer, final String titleTable, final boolean core) {
        getWhereSQL(queryBuffer, titleTable);
        queryBuffer.append("\nAND HISTORY_ERESOURCE.CORE ");
        if (core) {
            queryBuffer.append("= 'Y'");
        } else {
            queryBuffer.append("IS NULL");
        }
    }

    private void getWhereSQL(final StringBuffer queryBuffer, final String titleTable) {
        queryBuffer.append('\n');
        queryBuffer.append(WHERE);
        if (null != this.type) {
            queryBuffer.append("\nAND HISTORY_TYPE.ERESOURCE_ID = HISTORY_ERESOURCE.ERESOURCE_ID AND HISTORY_TYPE.TYPE = '")
                       .append(this.type).append("'");
        }
        if (null != this.core) {
            queryBuffer.append("\nAND HISTORY_ERESOURCE.CORE = 'Y'");
        }
        if (null != this.alpha) {
            queryBuffer.append("\nAND ");
            if (this.alpha.equals("#")) {
                queryBuffer
                           .append("(NLSSORT(SUBSTR(")
                           .append(titleTable)
                           .append(
                                   ",1,1),'NLS_SORT=GENERIC_BASELETTER') < NLSSORT('A','NLS_SORT=GENERIC_BASELETTER') OR NLSSORT(SUBSTR(")
                           .append(titleTable)
                           .append(",1,1),'NLS_SORT=GENERIC_BASELETTER') > NLSSORT('z','NLS_SORT=GENERIC_BASELETTER'))");
            } else {
                queryBuffer.append("NLSSORT(SUBSTR(").append(titleTable).append(",1,1),'NLS_SORT=GENERIC_BASELETTER') = NLSSORT('")
                           .append(this.alpha).append("','NLS_SORT=GENERIC_BASELETTER')");
            }
        }
        if (null != this.translatedQuery) {
            queryBuffer.append("\nAND CONTAINS(HISTORY_ERESOURCE.TEXT,'").append(this.translatedQuery).append("', 2) > 0 ");
        }
        if (titleTable.equals("HISTORY_ERESOURCE.PREFERRED_TITLE")) {
            queryBuffer.append("\nAND HISTORY_ERESOURCE.PREFERRED_TITLE IS NOT NULL");
        }
    }

    private void getOrderBySQL(final StringBuffer queryBuffer) {
        queryBuffer.append(ORDER_BY);
        if (null != this.translatedQuery) {
            queryBuffer.append("SCORE_TITLE DESC, SCORE_TEXT DESC, ");
        }
        queryBuffer.append(ORDER);
    }
}