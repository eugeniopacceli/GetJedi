package edu.getjedi.frontend.mobile.network;

/**
 * A Query URL builder for our service, which adds GET parameters to the URL and returns the full path
 * at build(). This and QueryURL implements the Builder pattern.
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
