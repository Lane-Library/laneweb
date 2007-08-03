/*
 * Created on Jan 10, 2006
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package edu.stanford.irt.laneweb;

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
	
	private static final String COUNT_QUERY =
		"WITH FOUND AS (SELECT ERESOURCE2.ERESOURCE_ID, TYPE2.TYPE, SUBSET2.SUBSET FROM ERESOURCE2, TYPE2, SUBSET2 \n" +
		"WHERE CONTAINS(ERESOURCE2.TEXT,'XX') > 0 \n" +
		"AND ERESOURCE2.ERESOURCE_ID = TYPE2.ERESOURCE_ID \n" +
		"AND ERESOURCE2.ERESOURCE_ID = SUBSET2.ERESOURCE_ID(+)) \n"+
		"SELECT COUNT(DISTINCT ERESOURCE_ID) AS HITS, 'er' AS GENRE FROM FOUND WHERE TYPE = 'er'\n"+
		"UNION\n"+
		"SELECT COUNT(DISTINCT ERESOURCE_ID) AS HITS, 'ej' AS GENRE FROM FOUND WHERE TYPE = 'ej'\n"+
		"UNION\n"+
		"SELECT COUNT(DISTINCT ERESOURCE_ID) AS HITS, 'database' AS GENRE FROM FOUND WHERE TYPE = 'database'\n"+
		"UNION\n"+
		"SELECT COUNT(DISTINCT ERESOURCE_ID) AS HITS, 'video' AS GENRE FROM FOUND WHERE TYPE = 'video'\n"+
		"UNION\n"+
		"SELECT COUNT(DISTINCT ERESOURCE_ID) AS HITS, 'book' AS GENRE FROM FOUND WHERE TYPE = 'book'\n"+
		"UNION\n"+
		"SELECT COUNT(DISTINCT ERESOURCE_ID) AS HITS, 'cc' AS GENRE FROM FOUND WHERE TYPE = 'cc'\n"+
		"UNION\n"+
		"SELECT COUNT(DISTINCT ERESOURCE_ID) AS HITS, 'lanefaq' AS GENRE FROM FOUND WHERE TYPE = 'lanefaq'\n"+
		"UNION\n"+
		"SELECT COUNT(DISTINCT ERESOURCE_ID) AS HITS, 'biotools' AS GENRE FROM FOUND WHERE SUBSET = 'biotools'";
    
    private static final String TYPE = "t";
    private static final String MESH = "m";
    private static final String ALPHA = "a";
    private static final String KEYWORDS = "keywords";
    private static final String QUERY = "q";
    private static final String SUBSET = "s";
    private static final String CORE = "c";
    private static final String XMLNS = "http://apache.org/cocoon/SQL/2.0";
    private static final String EXECUTE_QUERY_ELEMENT = "execute-query";
    private static final String QUERY_ELEMENT = "query";
    private static final String SELECT = 
    	"SELECT " +
    	"LINK2.SEQNUM, " +
    	"ERESOURCE2.ERESOURCE_ID, " +
    	"VERSION2.VERSION_ID, " +
    	"VERSION2.PUBLISHER, " +
    	"VERSION2.HOLDINGS, " +
    	"VERSION2.DATES, " +
    	"VERSION2.DESCRIPTION, " +
    	"VERSION2.PROXY, " +
    	"LINK2.URL, " +
    	"LINK2.LABEL, " +
    	"LINK2.INSTRUCTION, ";
    private static final String FROM = "FROM " +
    "ERESOURCE2, VERSION2, LINK2";
    private static final String WHERE =
    	"WHERE " +
    	"ERESOURCE2.ERESOURCE_ID = VERSION2.ERESOURCE_ID " +
    	"AND " + 
    	"VERSION2.VERSION_ID = LINK2.VERSION_ID ";
    private static final String ORDER_BY = "\nORDER BY ";
    private static final String ORDER = " STITLE, SEQNUM";
    private static final Attributes EMPTY_ATTS = new AttributesImpl();
    
    private String type;
    private String mesh;
    private String subset; 
    private String alpha;
    private String core;
    private String query;
    private String translatedQuery;
    private boolean count;
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
        this.count = request.getRequestURI().indexOf("count") > 0;
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
        this.count = this.count && this.translatedQuery != null;
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
        this.count = false;
        this.translatedQuery = null;
    }
    
    private char[] getSelectStatmentChars() {
    	if (this.count) {
    		String countQuery = COUNT_QUERY.replaceAll("XX", this.translatedQuery);
    		return countQuery.toCharArray();
    	}
    	StringBuffer queryBuffer = new StringBuffer();
    	if (this.translatedQuery != null) {
    		getScoredSelectSQL(queryBuffer, "ERESOURCE2.TITLE", true);
    		getFromSQL(queryBuffer);
    		getScoredWhereSQL(queryBuffer, "ERESOURCE2.TITLE", true);
    		queryBuffer.append("\nUNION\n");
    		getScoredSelectSQL(queryBuffer, "ERESOURCE2.TITLE", false);
    		getFromSQL(queryBuffer);
    		getScoredWhereSQL(queryBuffer, "ERESOURCE2.TITLE", false);
    		queryBuffer.append("\nUNION\n");
    		getScoredSelectSQL(queryBuffer, "ERESOURCE2.PREFERRED_TITLE", true);
    		getFromSQL(queryBuffer);
    		getScoredWhereSQL(queryBuffer, "ERESOURCE2.PREFERRED_TITLE", true);
    		queryBuffer.append("\nUNION\n");
    		getScoredSelectSQL(queryBuffer, "ERESOURCE2.PREFERRED_TITLE", false);
    		getFromSQL(queryBuffer);
    		getScoredWhereSQL(queryBuffer, "ERESOURCE2.PREFERRED_TITLE", false);
    		getOrderBySQL(queryBuffer);
    	} else {
    		getSelectSQL(queryBuffer, "ERESOURCE2.TITLE");
    		getFromSQL(queryBuffer);
    		getWhereSQL(queryBuffer, "ERESOURCE2.TITLE");
    		queryBuffer.append("\nUNION\n");
    		getSelectSQL(queryBuffer, "ERESOURCE2.PREFERRED_TITLE");
    		getFromSQL(queryBuffer);
    		getWhereSQL(queryBuffer, "ERESOURCE2.PREFERRED_TITLE");
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
    	queryBuffer.append(SELECT)
    	.append(titleTable).append(", lower(").append(titleTable).append(") AS LTITLE")
    	.append(", NLSSORT(").append(titleTable).append(",'NLS_SORT=GENERIC_BASELETTER') AS STITLE");
    }
    
    private void getFromSQL(StringBuffer queryBuffer) {
    	queryBuffer.append('\n');
    	queryBuffer.append(FROM);
      if (this.type != null) {
			queryBuffer.append(", TYPE2");
		}
		if (this.mesh != null) {
			queryBuffer.append(", MESH2");
		}
		if (this.subset != null) {
			queryBuffer.append(", SUBSET2");
		}
    }
    private void getScoredWhereSQL(StringBuffer queryBuffer, String titleTable, boolean core) {
    	getWhereSQL(queryBuffer, titleTable);
    	queryBuffer.append("\nAND ERESOURCE2.CORE ");
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
							"\nAND TYPE2.ERESOURCE_ID = ERESOURCE2.ERESOURCE_ID AND TYPE2.TYPE = '")
					.append(this.type).append("'");
		}
		if (this.mesh != null) {
			queryBuffer
					.append(
							"\nAND MESH2.ERESOURCE_ID = ERESOURCE2.ERESOURCE_ID AND MESH2.TERM = '")
					.append(this.mesh).append("'");
		}
		if (this.subset != null) {
			queryBuffer
					.append(
							"\nAND SUBSET2.VERSION_ID = VERSION2.VERSION_ID AND SUBSET2.SUBSET = '")
					.append(this.subset).append("'");
		}
		if (this.core != null) {
			queryBuffer.append("\nAND ERESOURCE2.CORE = 'Y'");
		}
		if (this.alpha != null) {
			queryBuffer.append("\nAND ");
			if (this.alpha.equals("#")) {
				queryBuffer.append("(NLSSORT(SUBSTR(")
				.append(titleTable)
				.append(",1,1),'NLS_SORT=GENERIC_BASELETTER') < NLSSORT('A','NLS_SORT=GENERIC_BASELETTER') OR NLSSORT(SUBSTR(")
				.append(titleTable)
				.append(",1,1),'NLS_SORT=GENERIC_BASELETTER') > NLSSORT('z','NLS_SORT=GENERIC_BASELETTER'))");
			} else {
				queryBuffer.append("NLSSORT(SUBSTR(").append(titleTable)
				.append(",1,1),'NLS_SORT=GENERIC_BASELETTER') = NLSSORT('").append(this.alpha).append("','NLS_SORT=GENERIC_BASELETTER')");
			}
		}
		if (this.translatedQuery != null) {
			queryBuffer.append("\nAND CONTAINS(ERESOURCE2.TEXT,'").append(
					translatedQuery).append("', 2) > 0 ");
		}
		if (titleTable.equals("ERESOURCE2.PREFERRED_TITLE")) {
			queryBuffer.append("\nAND ERESOURCE2.PREFERRED_TITLE IS NOT NULL");
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
