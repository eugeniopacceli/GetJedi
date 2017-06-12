package edu.getjedi.frontend.mobile.network;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import java.util.List;

public class HTTPHandler implements Response.Listener, Response.ErrorListener {
    private static RequestQueue queue;
    private static HTTPHandler httpHandler;
    public static final String SERVER_URL = "http://45.55.209.101/";

    private HTTPHandler(Context context){
        queue = Volley.newRequestQueue(context);
    }

    public void makeRequest(List<String> paramsInOrder){
        QueryURLBuilder builder = new QueryURLBuilder(SERVER_URL);
        for(String e : paramsInOrder){
            builder.addParameter(e);
        }
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET,builder.build(), null, this, this);
        queue.add(request);
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Log.i("<--- ERROR --->",error.toString());
    }

    @Override
    public void onResponse(Object response) {
        Log.i("<--- RESPONSE --->",response.toString());
    }

    public static HTTPHandler getInstanceOf(Context context){
        if(httpHandler == null){
            httpHandler = new HTTPHandler(context);
        }
        return httpHandler;
    }
}
