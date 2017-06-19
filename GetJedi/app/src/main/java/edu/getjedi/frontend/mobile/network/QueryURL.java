package edu.getjedi.frontend.mobile.network;

/**
 * An object representing a query URL for our service. A query URL contains our server address plus
 * possible GET parameters separated by '/'.
 */
public class QueryURL {
    private String queryUrl;

    public QueryURL(String queryUrl) {
        this.queryUrl = queryUrl;
    }

    public String getQueryUrl() {
        return queryUrl;
    }

    public void addParameter(String param){
        this.queryUrl += "/" + param;
    }

    public void setQueryUrl(String queryUrl) {
        this.queryUrl = queryUrl;
    }
}
