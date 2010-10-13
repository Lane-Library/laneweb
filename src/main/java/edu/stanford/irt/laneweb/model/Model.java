package edu.stanford.irt.laneweb.model;

import java.util.HashMap;

/**
 * Model for sitemap components.
 * 
 * @author ceyates $Id$
 */
@SuppressWarnings("serial")
public class Model extends HashMap<String, Object> {

    @Override
    public Object put(String key, Object value) {
        if (value == null) {
            throw new IllegalArgumentException("null value for " + key);
        }
        return super.put(key, value);
    }

    public static final String ALPHA = "alpha";

    public static final String BASE_PATH = "base-path";

    public static final String BASSETT_NUMBER = "basset-number";

    public static final String CALLBACK = "callback";

    public static final String CONTENT_BASE = "content-base";

    public static final String DEBUG = "debug";

    public static final String DEFAULT_CONTENT_BASE = "default-content-base";

    public static final String DEFAULT_RESOURCES_BASE = "default-resources-base";

    public static final String EMRID = "emrid";

    public static final String ENGINES = "engines";

    public static final String ENTRY_URL = "entry-url";

    public static final String FACETS = "facets";

    public static final String HOST = "host";

    public static final String IPGROUP = "ipgroup";

    public static final String LIMIT = "limit";

    public static final String MEDBLOG_BASE = "medblog-base";

    public static final String MESH = "mesh";

    public static final String MODEL = "model";

    public static final String NAME = "name";

    public static final String PAGE = "page";

    public static final String PAGE_NUMBER = "pageNumber";

    public static final String PARENT_PATH = "parent-path";

    public static final String PASSWORD = "password";

    public static final String PERSISTENT_LOGIN = "persistent-login";

    public static final String PID = "pid";

    public static final String PROXY_LINKS = "proxy-links";

    public static final String QUERY = "query";

    public static final String QUERY_STRING = "query-string";

    public static final String REFERRER = "referrer";

    public static final String REGION = "region";

    public static final String RELEASE = "release";

    public static final String REMOTE_ADDR = "remote-addr";

    public static final String REMOVE_PERSISTENT_LOGIN = "remove-persistent-login";

    public static final String REQUEST_URI = "request-uri";

    public static final String RESOURCE_ID = "rid";

    public static final String RESOURCES = "resources";

    public static final String RESOURCES_BASE = "resources-base";

    public static final String SELECTION = "selection";

    public static final String SHOW = "show";

    public static final String SOURCE = "source";

    public static final String SOURCEID = "sourceid";

    public static final String SUBSET = "subset";

    public static final String SUNETID = "sunetid";

    public static final String SYNCHRONOUS = "synchronous";

    public static final String TEMPLATE = "template";

    public static final String TICKET = "ticket";

    public static final String TITLE = "title";

    public static final String TYPE = "type";

    public static final String UNIVID = "univid";

    public static final String URL = "url";

    public static final String USER_COOKIE = "user-cookie";

    public static final String VERSION = "version";
}
