package edu.getjedi.frontend.mobile.network;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import org.json.JSONArray;
import org.json.JSONException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class ProfessionalsRequest extends Request<JSONArray> {

    private Response.Listener<JSONArray> listener;
    private Map<String, String> params;

    public ProfessionalsRequest(String url, String token,
                        Response.Listener<JSONArray> responseListener, Response.ErrorListener errorListener) {
        super(Method.POST, url, errorListener);
        this.listener = responseListener;
        buildPOSTParams(token);
    }

    private void buildPOSTParams(String token){
        params  = new HashMap<String, String>();
        params.put("token",token);
    }

    protected Map<String, String> getParams()
            throws com.android.volley.AuthFailureError {
        return params;
    };

    @Override
    protected Response<JSONArray> parseNetworkResponse(NetworkResponse response) {
        try {
            String jsonString = new String(response.data,
                    HttpHeaderParser.parseCharset(response.headers));
            return Response.success(new JSONArray(jsonString),
                    HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (JSONException je) {
            return Response.error(new ParseError(je));
        }
    }

    @Override
    protected void deliverResponse(JSONArray response) {
        listener.onResponse(response);
    }

}
