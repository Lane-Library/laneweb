package edu.stanford.irt.laneweb.model;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Model for sitemap components.
 */
public class Model implements Map<String, Object> {

    public static final String ACTION = "action";

    public static final String ALPHA = "alpha";

    public static final String AUTH = "auth";

    public static final String BANNER = "banner";

    public static final String BASE_PATH = "base-path";

    public static final String BASE_PROXY_URL = "base-proxy-url";

    public static final String BASSETT_NUMBER = "basset-number";

    public static final String BOOKMARKS = "bookmarks";

    public static final String CALLBACK = "callback";

    public static final String CATEGORY = "category";

    public static final String CLASS_ID = "class-id";

    public static final String CONTENT_BASE = "content-base";

    public static final String DEBUG = "debug";

    public static final String DISASTER_MODE = "disaster-mode";

    public static final String EMAIL = "email";

    public static final String EMRID = "emrid";

    public static final String ENGINES = "engines";

    public static final String ENTRY_URL = "entry-url";

    public static final String EXPIRES = "expires";

    public static final String FACETS = "facets";

    public static final String HOST = "host";

    public static final String ID = "id";

    public static final String IPGROUP = "ipgroup";

    public static final String IS_ACTIVE_SUNETID = "isActiveSunetID";

    public static final String ITEMS = "items";

    public static final String LIMIT = "limit";

    public static final String LIVE_CHAT_AVAILABLE = "live-chat-available";

    public static final String MESH = "mesh";

    public static final String MODEL = "model";

    public static final String NAME = "name";

    public static final String PAGE = "page";

    public static final String PAGE_NUMBER = "pageNumber";

    public static final String PARAMETER_MAP = "parameter-map";

    public static final String PASSWORD = "password";

    public static final String PERSISTENT_LOGIN = "persistent-login";

    public static final String PERSISTENT_LOGIN_EXPIRATION_DATE = "lane-login-expiration-date";

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

    public static final String RETURN = "return";

    public static final String SELECTION = "selection";

    public static final String SERVLET_PATH = "servlet-path";

    public static final String SHOW = "show";

    public static final String SITEMAP_URI = "sitemap-uri";

    public static final String SOURCE = "source";

    public static final String SOURCEID = "sourceid";

    public static final String SUBSET = "subset";

    public static final String TEMPLATE = "template";

    public static final String TEXT = "text";

    public static final String TICKET = "ticket";

    public static final String TIMEOUT = "timeout";

    public static final String TITLE = "title";

    public static final String TODAYS_HOURS = "todays-hours";

    public static final String TYPE = "type";

    public static final String UNIVID = "univid";

    public static final String URL = "url";

    public static final String URL_ENCODED_QUERY = "url-encoded-query";

    public static final String USER = "user";

    public static final String USER_AGENT = "user-agent";

    public static final String USER_ID = "userid";

    public static final String VERSION = "version";

    private Map<String, Object> map;

    private ModelValidator validator;

    public Model(final ModelValidator validator) {
        this.map = new HashMap<String, Object>();
        this.validator = validator;
    }

    @Override
    public void clear() {
        this.map.clear();
    }

    @Override
    public boolean containsKey(final Object key) {
        return this.map.containsKey(key);
    }

    @Override
    public boolean containsValue(final Object value) {
        return this.map.containsValue(value);
    }

    @Override
    public Set<java.util.Map.Entry<String, Object>> entrySet() {
        return this.map.entrySet();
    }

    @Override
    public boolean equals(final Object o) {
        return this.map.equals(o);
    }

    @Override
    public Object get(final Object key) {
        return this.map.get(key);
    }

    @Override
    public int hashCode() {
        return this.map.hashCode();
    }

    @Override
    public boolean isEmpty() {
        return this.map.isEmpty();
    }

    @Override
    public Set<String> keySet() {
        return this.map.keySet();
    }

    @Override
    public Object put(final String key, final Object value) {
        if (!this.validator.isValid(key, value)) {
            throw new InvalidModelEntryException(key, value);
        }
        return this.map.put(key, value);
    }

    @Override
    public void putAll(final Map<? extends String, ? extends Object> m) {
        for (Entry<? extends String, ? extends Object> entry : m.entrySet()) {
            if (!this.validator.isValid(entry.getKey(), entry.getValue())) {
                throw new InvalidModelEntryException(entry.getKey(), entry.getValue());
            }
        }
        this.map.putAll(m);
    }

    @Override
    public Object remove(final Object key) {
        return this.map.remove(key);
    }

    @Override
    public int size() {
        return this.map.size();
    }

    @Override
    public Collection<Object> values() {
        return this.map.values();
    }
}
