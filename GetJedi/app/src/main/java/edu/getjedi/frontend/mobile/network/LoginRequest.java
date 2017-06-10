package edu.getjedi.frontend.mobile.network;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class LoginRequest extends Request<JSONObject> {

    private Response.Listener<JSONObject> listener;
    private Map<String, String> params;

    public LoginRequest(String url, String email, String password,
                        Response.Listener<JSONObject> responseListener, Response.ErrorListener errorListener) {
        super(Method.POST, url, errorListener);
        this.listener = responseListener;
        buildPOSTParams(email,password);
    }

    private void buildPOSTParams(String email, String password){
        params  = new HashMap<String, String>();
        params.put("email",email);
        params.put("password",password);
    }

    protected Map<String, String> getParams()
            throws com.android.volley.AuthFailureError {
        return params;
    };

    @Override
    protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
        try {
            String jsonString = new String(response.data,
                    HttpHeaderParser.parseCharset(response.headers));
            return Response.success(new JSONObject(jsonString),
                    HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (JSONException je) {
            return Response.error(new ParseError(je));
        }
    }

    @Override
    protected void deliverResponse(JSONObject response) {
        listener.onResponse(response);
    }

}
