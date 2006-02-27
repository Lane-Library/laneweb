/*
 * Created on Jan 10, 2006
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package edu.stanford.laneweb;

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

public class EresourcesQueryGenerator extends AbstractGenerator {
    
    private static final String TYPE = "t";
    private static final String MESH = "m";
    private static final String ALPHA = "a";
    private static final String KEYWORDS = "keywords";
    private static final String QUERY = "q";
    private static final String SUBSET = "s";
    private static final String FUZZY = "f";
    private static final String XMLNS = "http://apache.org/cocoon/SQL/2.0";
    private static final String EXECUTE_QUERY_ELEMENT = "execute-query";
    private static final String QUERY_ELEMENT = "query";
    private static final String SELECT = "SELECT " +
"DISTINCT LINK.LINK_ID, ERESOURCE.ERESOURCE_ID, VERSION.VERSION_ID, ERESOURCE.TITLE, lower(ERESOURCE.TITLE) as LTITLE, VERSION.PUBLISHER, VERSION.HOLDINGS, VERSION.DATES, VERSION.PROXY, LINK.URL, LINK.LABEL, LINK.INSTRUCTION";
    private static final String FROM = " FROM " +
"ERESOURCE, VERSION, LINK";
    private static final String WHERE =
" WHERE " +
"ERESOURCE.ERESOURCE_ID = VERSION.ERESOURCE_ID " +
"AND " + 
"VERSION.VERSION_ID = LINK.VERSION_ID ";
    private static String UNION = " UNION ";
    private static final String ALT_SELECT = "SELECT " +
    "DISTINCT LINK.LINK_ID, ERESOURCE.ERESOURCE_ID, VERSION.VERSION_ID, ERESOURCE.PREFERRED_TITLE, lower(ERESOURCE.PREFERRED_TITLE) as LTITLE, VERSION.PUBLISHER, VERSION.HOLDINGS, VERSION.DATES, VERSION.PROXY, LINK.URL, LINK.LABEL, LINK.INSTRUCTION";
    private static final String ALT_AND = " AND ERESOURCE.PREFERRED_TITLE IS NOT NULL";
    private static final String ORDER_BY = " ORDER BY ";
    private static final String ORDER = " LTITLE, LINK_ID";
    private static final Attributes EMPTY_ATTS = new AttributesImpl();
    
    private String type;
    private String mesh;
    private String subset; 
    private String alpha;
    private String query;
    private String fuzzy;
    private boolean haveParameters;
    private QueryTranslator queryTranslator = new QueryTranslator();

    public void setup(SourceResolver resolver, Map objectModel, String src,
            Parameters par) throws ProcessingException, SAXException,
            IOException {
        super.setup(resolver, objectModel, src, par);
        Request request = ObjectModelHelper.getRequest(objectModel);
        this.haveParameters = false;
        this.type = request.getParameter(TYPE);
        if (this.type != null) {
            if (this.type.length() == 0) {
                this.type = null;
            } else {
                this.type = this.type.toLowerCase();
                this.haveParameters = true;
            }
        }
        this.mesh = request.getParameter(MESH);
        if (this.mesh != null) {
            if (this.mesh.length() == 0) {
                this.mesh = null;
            } else {
                this.mesh = this.mesh.toLowerCase();
                this.haveParameters = true;
            }
        }
        this.subset = request.getParameter(SUBSET);
        if (this.subset != null) {
            if (this.subset.length() == 0) {
                this.subset = null;
            } else {
                this.subset = this.subset.toLowerCase();
                this.haveParameters = true;
            }
        }
        this.alpha = request.getParameter(ALPHA);
        if (this.alpha != null) {
            if (this.alpha.length() == 0) {
                this.alpha = null;
            } else {
                this.alpha = this.alpha.toLowerCase();
                this.haveParameters = true;
            }
        }
        this.query = request.getParameter(QUERY);
        if (this.query != null) {
            if (this.query.length() ==0) {
                this.query = null;
            } else {
                this.haveParameters = true;
            }
        }
        if (this.query == null) {
			this.query = request.getParameter(KEYWORDS);
			if (this.query != null) {
				if (this.query.length() == 0) {
					this.query = null;
				} else {
					this.haveParameters = true;
				}
			}
		}
        this.fuzzy = request.getParameter(FUZZY);
        if (this.fuzzy != null) {
        	if (this.fuzzy.length() == 0) {
        		this.fuzzy = "60";
        	}
        } else {
        	this.fuzzy = "60";
        }
    }

    public void generate() throws SAXException {
        char[] selectStatmentChars = null;
        if (this.haveParameters)  {
            selectStatmentChars = getSelectStatmentChars();
        } else {
            selectStatmentChars = new char[0];
        }
        this.xmlConsumer.startDocument();
        this.xmlConsumer.startElement(XMLNS,EXECUTE_QUERY_ELEMENT,EXECUTE_QUERY_ELEMENT,EMPTY_ATTS);
        this.xmlConsumer.startElement(XMLNS,QUERY_ELEMENT,QUERY_ELEMENT,EMPTY_ATTS);
        this.xmlConsumer.characters(selectStatmentChars,0,selectStatmentChars.length);
        this.xmlConsumer.endElement(XMLNS,QUERY_ELEMENT,QUERY_ELEMENT);
        this.xmlConsumer.endElement(XMLNS,EXECUTE_QUERY_ELEMENT,EXECUTE_QUERY_ELEMENT);
        this.xmlConsumer.endDocument();
    }
    
    public void recycle() {
        this.type = null;
        this.mesh = null;
        this.subset = null;
        this.alpha = null;
        this.query = null;
        this.fuzzy = null;
    }
    
    private char[] getSelectStatmentChars() {
        StringBuffer queryBuffer = new StringBuffer(SELECT);
        String translatedQuery = null;
        if (this.query != null) {
            queryBuffer.append(", SCORE(1) AS SCORE_TITLE, SCORE(2) AS SCORE_ALL");
            translatedQuery = queryTranslator.translate(this.query);
        }
        queryBuffer.append(FROM);
        if (this.type != null) {
            queryBuffer.append(", TYPE");
        }
        if (this.mesh != null) {
            queryBuffer.append(", MESH");
        }
        if (this.subset != null) {
            queryBuffer.append(", SUBSET");
        }
        queryBuffer.append(WHERE);
        if (this.type != null) {
            queryBuffer.append(" AND TYPE.ERESOURCE_ID = ERESOURCE.ERESOURCE_ID AND TYPE.TYPE = '").append(this.type).append("'");
        }
        if (this.mesh != null) {
            queryBuffer.append(" AND MESH.ERESOURCE_ID = ERESOURCE.ERESOURCE_ID AND MESH.TERM = '").append(this.mesh).append("'");
        }
        if (this.subset != null) {
            queryBuffer.append(" AND SUBSET.VERSION_ID = VERSION.VERSION_ID AND SUBSET.SUBSET = '").append(this.subset).append("'");
        }
        if (this.alpha != null) {
            queryBuffer.append(" AND lower(ERESOURCE.TITLE) LIKE '").append(this.alpha).append("%'");
        }
        if (this.query != null) {
            queryBuffer.append(" AND (CONTAINS(TITLE,'");
            queryBuffer.append(translatedQuery);
            queryBuffer.append("', 1) > 0 OR CONTAINS(TEXT,'");
            queryBuffer.append(translatedQuery);
            queryBuffer.append("', 2) > 0) ");
        }
        queryBuffer.append(UNION);
        queryBuffer.append(ALT_SELECT);
        if (this.query != null) {
            queryBuffer.append(", SCORE(1) AS SCORE_TITLE, SCORE(2) AS SCORE_ALL");
        }
        queryBuffer.append(FROM);
        if (this.type != null) {
            queryBuffer.append(", TYPE");
        }
        if (this.mesh != null) {
            queryBuffer.append(", MESH");
        }
        if (this.subset != null) {
            queryBuffer.append(", SUBSET");
        }
        queryBuffer.append(WHERE);
        queryBuffer.append(ALT_AND);
        if (this.type != null) {
            queryBuffer.append(" AND TYPE.ERESOURCE_ID = ERESOURCE.ERESOURCE_ID AND TYPE.TYPE = '").append(this.type).append("'");
        }
        if (this.mesh != null) {
            queryBuffer.append(" AND MESH.ERESOURCE_ID = ERESOURCE.ERESOURCE_ID AND MESH.TERM = '").append(this.mesh).append("'");
        }
        if (this.subset != null) {
            queryBuffer.append(" AND SUBSET.VERSION_ID = VERSION.VERSION_ID AND SUBSET.SUBSET = '").append(this.subset).append("'");
        }
        if (this.alpha != null) {
            queryBuffer.append(" AND lower(ERESOURCE.PREFERRED_TITLE) LIKE '").append(this.alpha).append("%'");
        }
        if (this.query != null) {
            queryBuffer.append(" AND (CONTAINS(PREFERRED_TITLE,'");
            queryBuffer.append(translatedQuery);
            queryBuffer.append("', 1) > 0 OR CONTAINS(TEXT,'");
            queryBuffer.append(translatedQuery);
            queryBuffer.append("', 2) > 0) ");
        }
        queryBuffer.append(ORDER_BY);
        if (this.query != null) {
            queryBuffer.append("SCORE_TITLE DESC, SCORE_ALL DESC, ");
        }
        queryBuffer.append(ORDER);
        return  queryBuffer.toString().toCharArray();
    }

}
