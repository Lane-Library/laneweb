package edu.stanford.irt.laneweb;

import org.apache.excalibur.xml.sax.XMLizable;


public interface Resource extends XMLizable {

    public static final String RESULT = "result";

    public static final String TYPE = "type";

    public static final String SCORE = "score";

    public static final String ID = "id";

    public static final String CONTENT_ID = "contentId";

    public static final String EMPTY_NS = "";

    public static final String TITLE = "title";

    public static final String KEYWORD = "keyword";

    public static final String VERSIONS = "versions";

    public static final String VERSION = "version";

    public static final String SUMMARY_HOLDINGS = "summaryHoldings";

    public static final String PUBLISHER = "publisher";

    public static final String DATES = "dates";

    public static final String DESCRIPTION = "description";

    public static final String LINKS = "links";

    public static final String LINK = "link";

    public static final String LABEL = "label";

    public static final String URL = "url";

    public static final String INSTRUCTION = "instruction";

    public static final String RESOURCE_ID = "resourceId";

    public static final String RESOURCE_NAME = "resourceName";

    public static final String RESOURCE_URL = "resourceUrl";

    public static final String RESOURCE_HITS = "resourceHits";

    public static final String PUBLICATION_TITLE = "pub-title";

    public static final String PUBLICATION_ISSUE = "pub-issue";

    public static final String PUBLICATION_VOLUME = "pub-volume";

    public static final String PUBLICATION_DATE = "pub-date";

    public static final String AUTHOR = "pub-author";

    public static final String PAGES = "pub-pages";
    
    public static final String RESOURCES = "resources";
    
    public static final String QUERY = "query";

//    public static final String NAMESPACE = "http://lane.stanford.edu/search-results/1.0";

    public static final String NAMESPACE = "http://lane.stanford.edu/resources/1.0";
}
