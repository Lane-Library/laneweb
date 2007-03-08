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

public class HistoryQueryGenerator extends AbstractGenerator {
	
	//ERESOURCE.TITLE, lower(ERESOURCE.TITLE) AS LTITLE, 
	
    
    private static final String TYPE = "t";
    private static final String ALPHA = "a";
    private static final String KEYWORDS = "keywords";
    private static final String QUERY = "q";
    private static final String CORE = "c";
    private static final String XMLNS = "http://apache.org/cocoon/SQL/2.0";
    private static final String EXECUTE_QUERY_ELEMENT = "execute-query";
    private static final String QUERY_ELEMENT = "query";
    private static final String SELECT = 
    	"SELECT " +
    	"DISTINCT HISTORY_LINK.LINK_ID, " +
    	"HISTORY_ERESOURCE.ERESOURCE_ID, " +
    	"HISTORY_VERSION.VERSION_ID, " +
    	"HISTORY_VERSION.PUBLISHER, " +
    	"HISTORY_VERSION.HOLDINGS, " +
    	"HISTORY_VERSION.DATES, " +
    	"HISTORY_VERSION.DESCRIPTION, " +
    	"HISTORY_VERSION.PROXY, " +
    	"HISTORY_LINK.URL, " +
    	"HISTORY_LINK.LABEL, " +
    	"HISTORY_LINK.INSTRUCTION, ";
    private static final String FROM = "FROM " +
    "HISTORY_ERESOURCE, HISTORY_VERSION, HISTORY_LINK";
    private static final String WHERE =
    	"WHERE " +
    	"HISTORY_ERESOURCE.ERESOURCE_ID = HISTORY_VERSION.ERESOURCE_ID " +
    	"AND " + 
    	"HISTORY_VERSION.VERSION_ID = HISTORY_LINK.VERSION_ID ";
    private static final String ORDER_BY = "\nORDER BY ";
    private static final String ORDER = " LTITLE, LINK_ID";
    private static final Attributes EMPTY_ATTS = new AttributesImpl();
    
    private String type;
    private String alpha;
    private String core;
    private String query;
    private String translatedQuery;
    private boolean haveParameters;
    private String coreWeight;
    private QueryTranslator queryTranslator = new QueryTranslator();

    public void setup(SourceResolver resolver, Map objectModel, String src,
            Parameters par) throws ProcessingException, SAXException,
            IOException {
        super.setup(resolver, objectModel, src, par);
        this.coreWeight = par.getParameter("core-weight", "3");
        Request request = ObjectModelHelper.getRequest(objectModel);
        this.haveParameters = false;
        this.core = request.getParameter(CORE);
        if (this.core != null) {
        	if (this.core.equals("y")) {
        		this.haveParameters = true;
        	} else {
        		this.core = null;
        	}
        }
        this.type = request.getParameter(TYPE);
        if (this.type != null) {
            if (this.type.length() == 0) {
                this.type = null;
            } else {
                this.type = this.type.toLowerCase();
                this.haveParameters = true;
            }
        }
        this.alpha = request.getParameter(ALPHA);
        if (this.alpha != null) {
            if (this.alpha.length() == 0) {
                this.alpha = null;
            } else {
                this.alpha = this.alpha.substring(0,1).toLowerCase();
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
        if (this.query != null) {
        	this.query = this.query.replaceAll("'","''");
        	this.translatedQuery = this.queryTranslator.translate(this.query);
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
        this.alpha = null;
        this.query = null;
        this.translatedQuery = null;
    }
    
    private char[] getSelectStatmentChars() {
    	StringBuffer queryBuffer = new StringBuffer();
    	if (this.translatedQuery != null) {
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
        return  queryBuffer.toString().toCharArray();
    }
    
    private void getScoredSelectSQL(StringBuffer queryBuffer, String titleTable, boolean coreWeighting) {
    	getSelectSQL(queryBuffer, titleTable);
        queryBuffer.append(", CONTAINS(").append(titleTable).append(",'")
        .append(this.translatedQuery)
        .append("',1)");
        if (coreWeighting) {
        	queryBuffer.append(" * ").append(this.coreWeight);
        }
        queryBuffer.append(" AS SCORE_TITLE")
        .append(", SCORE(2) AS SCORE_TEXT");
    }
    
    private void getSelectSQL(StringBuffer queryBuffer, String titleTable) {
    	queryBuffer.append(SELECT);
    	queryBuffer.append(titleTable).append(", lower(").append(titleTable).append(") AS LTITLE");
    }
    
    private void getFromSQL(StringBuffer queryBuffer) {
    	queryBuffer.append('\n');
    	queryBuffer.append(FROM);
      if (this.type != null) {
			queryBuffer.append(", TYPE");
		}
    }
    private void getScoredWhereSQL(StringBuffer queryBuffer, String titleTable, boolean core) {
    	getWhereSQL(queryBuffer, titleTable);
    	queryBuffer.append("\nAND HISTORY_ERESOURCE.CORE ");
    	if (core) {
    		queryBuffer.append("= 'Y'");
    	} else {
    		queryBuffer.append("IS NULL");
    	}
    }
    
    private void getWhereSQL(StringBuffer queryBuffer, String titleTable) {
		queryBuffer.append('\n');
		queryBuffer.append(WHERE);
		if (this.type != null) {
			queryBuffer
					.append(
							"\nAND HISTORY_TYPE.ERESOURCE_ID = HISTORY_ERESOURCE.ERESOURCE_ID AND HISTORY_TYPE.TYPE = '")
					.append(this.type).append("'");
		}
		if (this.core != null) {
			queryBuffer.append("\nAND HISTORY_ERESOURCE.CORE = 'Y'");
		}
		if (this.alpha != null) {
			queryBuffer.append("\nAND ");
			if (this.alpha.equals("#")) {
				
			} else {
				queryBuffer.append("LOWER(SUBSTR(").append(titleTable)
				.append(",1,1)) = '").append(this.alpha).append("'");
			}
			if (this.alpha.equals("#")) {
				queryBuffer.append("(SUBSTR(")
				.append(titleTable)
				.append(",1,1) < 'A' OR SUBSTR(")
				.append(titleTable)
				.append(",1,1) > 'z')");
			}
		}
		if (this.translatedQuery != null) {
			queryBuffer.append("\nAND CONTAINS(HISTORY_ERESOURCE.TEXT,'").append(
					translatedQuery).append("', 2) > 0 ");
		}
		if (titleTable.equals("HISTORY_ERESOURCE.PREFERRED_TITLE")) {
			queryBuffer.append("\nAND HISTORY_ERESOURCE.PREFERRED_TITLE IS NOT NULL");
		}
	}
    private void getOrderBySQL(StringBuffer queryBuffer) {
        queryBuffer.append(ORDER_BY);
		if (this.translatedQuery != null) {
			queryBuffer.append("SCORE_TITLE DESC, SCORE_TEXT DESC, ");
		}
		queryBuffer.append(ORDER);
    }
}
