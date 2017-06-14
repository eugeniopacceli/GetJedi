package edu.getjedi.frontend.mobile.network;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
;
import org.json.JSONObject;

import java.util.HashMap;

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

    public void makeRequestForArray(String[] paramsInOrder){
        QueryURLBuilder builder = new QueryURLBuilder(SERVER_URL);
        for(String e : paramsInOrder){
            builder.addParameter(e);
        }
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET,builder.build(), null, this, this);
        queue.add(request);
    }

    public void makeRequestForObject(String[] paramsInOrder){
        QueryURLBuilder builder = new QueryURLBuilder(SERVER_URL);
        for(String e : paramsInOrder){
            builder.addParameter(e);
        }
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET,builder.build(), null, this, this);
        queue.add(request);
    }

    public void makePOSTRequestForObject(String[] paramsInOrder, final HashMap<String, String> postParams){
        QueryURLBuilder builder = new QueryURLBuilder(SERVER_URL);
        for(String e : paramsInOrder){
            builder.addParameter(e);
        }
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST,builder.build(), null, this, this) {
            @Override
            public byte[] getBody(){
                return new JSONObject(postParams).toString().getBytes();
            }

            @Override
            public String getBodyContentType() {
                return "application/json";
            }
        };
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
