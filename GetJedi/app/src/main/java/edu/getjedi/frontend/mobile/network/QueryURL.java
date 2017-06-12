package edu.getjedi.frontend.mobile.network;

/**
 * Created by Administrador on 11/06/2017.
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
