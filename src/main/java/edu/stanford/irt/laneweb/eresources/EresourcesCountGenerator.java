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

public class EresourcesCountGenerator extends AbstractGenerator {

    // TODO only text search in with clause maybe probably should test again
    // since biotools more efficient with eresource_id
    private static final String COUNT_QUERY_1 =
            "WITH FOUND AS (SELECT ERESOURCE2.ERESOURCE_ID, TYPE2.TYPE, SUBSET2.SUBSET FROM ERESOURCE2, TYPE2, SUBSET2 \n"
                    + "WHERE CONTAINS(ERESOURCE2.TEXT,'";
    
    private static final String COUNT_QUERY_2 = "') > 0 \n" + "AND ERESOURCE2.ERESOURCE_ID = TYPE2.ERESOURCE_ID \n"
                    + "AND ERESOURCE2.ERESOURCE_ID = SUBSET2.ERESOURCE_ID(+)) \n"
                    + "SELECT COUNT(DISTINCT ERESOURCE_ID) AS HITS, 'all' AS GENRE FROM FOUND\n"
                    + "UNION\n"
                    + "SELECT COUNT(DISTINCT ERESOURCE_ID) AS HITS, 'ej' AS GENRE FROM FOUND WHERE TYPE = 'ej'\n"
                    + "UNION\n"
                    + "SELECT COUNT(DISTINCT ERESOURCE_ID) AS HITS, 'database' AS GENRE FROM FOUND WHERE TYPE = 'database'\n"
                    + "UNION\n"
                    + "SELECT COUNT(DISTINCT ERESOURCE_ID) AS HITS, 'video' AS GENRE FROM FOUND WHERE TYPE = 'video'\n"
                    + "UNION\n"
                    + "SELECT COUNT(DISTINCT ERESOURCE_ID) AS HITS, 'book' AS GENRE FROM FOUND WHERE TYPE = 'book'\n"
                    + "UNION\n"
                    + "SELECT COUNT(DISTINCT ERESOURCE_ID) AS HITS, 'cc' AS GENRE FROM FOUND WHERE TYPE = 'cc'\n"
                    + "UNION\n"
                    + "SELECT COUNT(DISTINCT ERESOURCE_ID) AS HITS, 'lanefaq' AS GENRE FROM FOUND WHERE TYPE = 'lanefaq'\n"
                    + "UNION\n"
                    + "SELECT COUNT(DISTINCT ERESOURCE_ID) AS HITS, 'biotools' AS GENRE FROM FOUND WHERE SUBSET = 'biotools'";

    private static final String KEYWORDS = "keywords";

    private static final String QUERY = "q";

    private static final String XMLNS = "http://apache.org/cocoon/SQL/2.0";

    private static final String EXECUTE_QUERY_ELEMENT = "execute-query";

    private static final String QUERY_ELEMENT = "query";

    private static final Attributes EMPTY_ATTS = new AttributesImpl();
    
    private static final char[] DUAL_SELECT = "SELECT * FROM DUAL".toCharArray();

    private char[] selectStatementChars;

    private QueryTranslator queryTranslator = new QueryTranslator();
    
    @Override
    public void setup(final SourceResolver resolver, final Map objectModel, final String src, final Parameters par)
            throws ProcessingException, SAXException, IOException {
        super.setup(resolver, objectModel, src, par);
        Request request = ObjectModelHelper.getRequest(objectModel);
        String query = request.getParameter(QUERY);
        if (query == null) {
            query = request.getParameter(KEYWORDS);
        }
        if (null != query) {
            query = query.trim();
            if (query.length() == 0) {
                query = null;
            }

        }
        try {
            String translatedQuery = this.queryTranslator.translate(query.replaceAll("'","''"));
            StringBuffer sb = new StringBuffer(COUNT_QUERY_1);
            sb.append(translatedQuery);
            sb.append(COUNT_QUERY_2);
            this.selectStatementChars = sb.toString().toCharArray();
        } catch (Exception e) {
            if (getLogger().isErrorEnabled()) {
                getLogger().error(e.getMessage(), e);
            }
            this.selectStatementChars = DUAL_SELECT;
        }
    }

    public void generate() throws SAXException {
        this.xmlConsumer.startDocument();
        if (this.selectStatementChars.length > 0) {
            this.xmlConsumer.startElement(XMLNS, EXECUTE_QUERY_ELEMENT, EXECUTE_QUERY_ELEMENT, EMPTY_ATTS);
            this.xmlConsumer.startElement(XMLNS, QUERY_ELEMENT, QUERY_ELEMENT, EMPTY_ATTS);
            this.xmlConsumer.characters(this.selectStatementChars, 0, this.selectStatementChars.length);
            this.xmlConsumer.endElement(XMLNS, QUERY_ELEMENT, QUERY_ELEMENT);
            this.xmlConsumer.endElement(XMLNS, EXECUTE_QUERY_ELEMENT, EXECUTE_QUERY_ELEMENT);
        }
        this.xmlConsumer.endDocument();
    }

    @Override
    public void recycle() {
        this.selectStatementChars = null;
    }
}
