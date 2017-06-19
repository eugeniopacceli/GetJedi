package edu.getjedi.frontend.mobile.network;

import android.util.Log;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;

import edu.getjedi.frontend.mobile.MainMapActivity;


/**
 * The HTTP Handler Singleton. Used to make calls and receive responses to/from the Volley API provided
 * by Android, which is an way to handle async HTTP communications.
 *
 * The Android Volley receives the application's packages into a RequestQueue, consumes and sends them to
 * target, then handles the server response into it's Response.Listener and Response.ErrorListener, which
 * this class also implements.
 *
 * All the requests are made to getjedipro.com, and the params are built by the URL builder (for HTTP GETs)
 * or by overwritting the Volley's parameters in case of a HTTP POST request - as specified in the Volleys doc -.
 *
 * All the responses from the server come in JSON format. The responses are passed to the current state object.
 */
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

    /**
     * Receives a response and passes it to the current state handler of our application.
     */
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
