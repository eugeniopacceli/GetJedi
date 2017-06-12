package edu.getjedi.frontend.mobile.network;

/**
 * Created by Administrador on 11/06/2017.
 */

public class QueryURLBuilder {
    private QueryURL queryUrl;

    public QueryURLBuilder(String base) {
        this.queryUrl = new QueryURL(base);
    }

    public QueryURLBuilder addParameter(String parameter){
        this.queryUrl.addParameter(parameter);
        return this;
    }

    public String build(){
        return queryUrl.getQueryUrl();
    }
}
