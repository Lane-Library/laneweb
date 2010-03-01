package edu.stanford.irt.laneweb.model;

/**
 * Model for sitemap components.
 * 
 * @author ceyates
 *
 * $Id$
 */
public interface Model {
    
    public static final String BASE_PATH = "base-path";
    
    public static final String URL = "url";

    public static final String QUERY = "query";
    
    public static final String EMRID = "emrid";
    
    public static final String SUNETID = "sunetid";
    
    public static final String TICKET = "ticket";

    public static final String IPGROUP = "ipgroup";
    
    public static final String PROXY_LINKS = "proxy-links";

    public static final String LIMIT = "limit";
    
    public static final String BASSETT_NUMBER = "basset-number";
    
    public static final String REGION = "region";
    
    public static final String SUBSET = "subset";
    
    public static final String TYPE = "type";
    
    public static final String ALPHA = "alpha";
    
    public static final String MESH = "mesh";
    
    public static final String ENGINES = "engines";
    
    public static final String RESOURCES = "resources";

    public static final String FACETS = "facets";
    
    public static final String SOURCE = "source";

    public static final String NAME = "name";

    public static final String UNIVID = "univid";
    
    public static final String AFFILIATION = "affiliation";

    public static final String QUERY_STRING = "query-string";

    public static final String HOST = "host";

    public static final String NONCE = "nonce";

    public static final String SYSTEM_USER_ID = "system_user_id";

    public static final String RELEASE = "release";
    
    public static final String PASSWORD = "password";

    public static final String PID = "pid";

    public static final String LIAISON = "liaison";

    public static final String TEMPLATE = "template";

    public static final String MODEL = "model";
    
    <T> T getObject(String name, Class<T> clazz);
    
    <T> T getObject(String name, Class<T> clazz, T defaultValue);
    
    String getString(String name);
    
    String getString(String name, String defaultValue);
}
