package edu.getjedi.frontend.mobile.network;

import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
;
import edu.getjedi.frontend.mobile.MainMapActivity;

public class HTTPHandler implements Response.Listener, Response.ErrorListener {
    private static RequestQueue queue;
    private MainMapActivity mainActivity;
    private static HTTPHandler httpHandler;
    public static final String SERVER_URL = "http://getjedipro.com";

    private HTTPHandler(MainMapActivity context){
        mainActivity = context;
        queue = Volley.newRequestQueue(context);
    }

    public void makeRequest(String[] paramsInOrder, RequestType method){
        QueryURLBuilder builder = new QueryURLBuilder(SERVER_URL);
        for(String e : paramsInOrder){
            builder.addParameter(e);
        }
        JsonArrayRequest request = new JsonArrayRequest(method.ordinal(),builder.build(), null, this, this);
        queue.add(request);
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Log.i("<--- ERROR --->",error.toString());
    }

    @Override
    public void onResponse(Object response) {
        mainActivity.getAppContext().performAction(response);
    }

    public static HTTPHandler getInstanceOf(MainMapActivity context){
        if(httpHandler == null){
            httpHandler = new HTTPHandler(context);
        }
        return httpHandler;
    }
}
