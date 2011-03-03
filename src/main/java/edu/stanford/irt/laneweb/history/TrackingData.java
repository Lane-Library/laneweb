package edu.stanford.irt.laneweb.history;


public class TrackingData {
    
    private String host;
    
    private String path;
    
    private String query;
    
    private boolean external;
    
    private String title;
    
    private String searchTerms;
    
    private String searchSource;

    
    public String getHost() {
        return host;
    }

    
    public void setHost(String host) {
        this.host = host;
    }

    
    public String getPath() {
        return path;
    }

    
    public void setPath(String path) {
        this.path = path;
    }

    
    public String getQuery() {
        return query;
    }

    
    public void setQuery(String query) {
        this.query = query;
    }

    
    public boolean isExternal() {
        return external;
    }

    
    public void setExternal(boolean external) {
        this.external = external;
    }

    
    public String getTitle() {
        return title;
    }

    
    public void setTitle(String title) {
        this.title = title;
    }

    
    public String getSearchTerms() {
        return searchTerms;
    }

    
    public void setSearchTerms(String searchTerms) {
        this.searchTerms = searchTerms;
    }

    
    public String getSearchSource() {
        return searchSource;
    }

    
    public void setSearchSource(String searchSource) {
        this.searchSource = searchSource;
    }
    
    public String toString() {
        StringBuilder sb = new StringBuilder(this.title)
        .append(" http://").append(this.host).append(this.path);
        if (this.query != null) {
            sb.append("?").append(this.query);
        }
        return sb.toString();
    }
}
