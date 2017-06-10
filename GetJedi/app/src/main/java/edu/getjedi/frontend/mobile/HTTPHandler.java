package edu.getjedi.frontend.mobile;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import edu.getjedi.frontend.mobile.network.LoginRequest;

public class HTTPHandler implements Response.Listener, Response.ErrorListener {
    private RequestQueue queue;

    public HTTPHandler(Context context){
        queue = Volley.newRequestQueue(context);
    }

    public void loginToServer(String email, String password){
        LoginRequest request = new LoginRequest("http://45.55.209.101/users", email, password, this, this);
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
}
