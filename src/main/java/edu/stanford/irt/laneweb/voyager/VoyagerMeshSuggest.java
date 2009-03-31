package edu.stanford.irt.laneweb.voyager;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.sql.DataSource;

import org.apache.cocoon.ProcessingException;
import org.apache.log4j.Logger;

public class VoyagerMeshSuggest {
  
    private DataSource dataSource;
    
    private static final int MIN_QUERY_LENGTH = 3;
    
    public String query;
    
    private String limit;

    private ArrayList<String> meshList;
    
    private final String sql_1 = "select distinct display_heading from cifdb.bib_index" +
                                    " where index_code = '2451' and bib_id in" + 
                                    " (select distinct bib_id from cifdb.bib_index where index_code = '6502' and (normal_heading like '% ";

    private final String sql_2 ="%' or normal_heading like '";
    
    private final String sql_3 ="%')" +
                                    "  intersect " +
                                    " select distinct bib_id from cifdb.bib_index where index_code = '655H' and normal_heading = 'MESH'";
    
    private final String disease_limit = " intersect " +
                                    "select distinct bib_id from cifdb.bib_index where " +
                                    "((index_code = '655H' and normal_heading = 'TOPIC DISEASE')" +
                                    " or " + 
                                    "(index_code = '6502' and (normal_heading = 'DISEASE OR SYNDROME' or normal_heading = 'MENTAL OR BEHAVIORAL DYSFUNCTION')))";
    
    private final String intervention_limit = " intersect " +
                                    "(select distinct bib_id from cifdb.bib_index where index_code = '655H' and (normal_heading = 'TOPIC SUBSTANCE')" +
                                    " union " +
                                    "select distinct bib_id from cifdb.bib_index where index_code = '6502'" +
                                    " and " + 
                                    "(normal_heading = 'THERAPEUTIC OR PREVENTIVE PROCEDURE' or normal_heading = 'MEDICAL DEVICE'))";
    
    private final String sql_4 =") order by display_heading";
    
    private Logger logger = Logger.getLogger(VoyagerMeshSuggest.class);
    
    public ArrayList<String> getMesh(String q, String l) throws ProcessingException{
        this.meshList = new ArrayList<String>();
        this.limit = l.toLowerCase();
        this.query = q.toUpperCase();
        if ( null == this.query ){
          throw new ProcessingException("null query");
        }
        if ( MIN_QUERY_LENGTH > this.query.length() ){
          throw new ProcessingException("query too short");
        }
        this.query = this.filterQuery(this.query);
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            conn = this.dataSource.getConnection();
            stmt = conn.createStatement();
            if ("p".equals(this.limit) || "d".equals(this.limit)){ // patient or disease
              this.logger.debug(this.sql_1 + this.query + this.sql_2 + this.query + this.sql_3 + this.disease_limit + this.sql_4);
              rs = stmt.executeQuery(this.sql_1 + this.query + this.sql_2 + this.query + this.sql_3 + this.disease_limit + this.sql_4);
            }
            else if ("i".equals(this.limit)){ // intervention
              this.logger.debug(this.sql_1 + this.query + this.sql_2 + this.query + this.sql_3 + this.intervention_limit + this.sql_4);
              rs = stmt.executeQuery(this.sql_1 + this.query + this.sql_2 + this.query + this.sql_3 + this.intervention_limit + this.sql_4);
            }
            else{
              this.logger.debug(this.sql_1 + this.query + this.sql_2 + this.query + this.sql_3 + this.sql_4);
              rs = stmt.executeQuery(this.sql_1 + this.query + this.sql_2 + this.query + this.sql_3 + this.sql_4);
            }
            while (rs.next()) {
              this.meshList.add(rs.getString(1));
            }
            return this.meshList;
        } catch (SQLException e) {
            throw new ProcessingException(e);
        } finally {
            if (null != rs) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    // ?
                }
            }
            if (null != stmt) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    // ?
                }
            }
            if (null != conn) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    // ?
                }
            }
        }
    }
    
    /**
     * filter query string: strip ' and " and ' characters; non alphanumerics to blanks
     * 
     * @param query
     * @return String filtered query string
     */
    public String filterQuery(String query){
      String q = query;
      StringBuffer sb = new StringBuffer();
      for (int i = 0; i < q.length(); i++) {
          char c = q.charAt(i);
          if ( Character.isLetter(c) || Character.isDigit(c) ) {
              sb.append(c);
          }
          else if ( ('\'' != c) && ('"' != c) && (',' != c) ) {
              sb.append(' ');
          }
      }
      return sb.toString();
    }

    public void setDataSource(final DataSource dataSource) {
        if (null == dataSource) {
            throw new IllegalArgumentException("null dataSource");
        }
        this.dataSource = dataSource;
    }

}
