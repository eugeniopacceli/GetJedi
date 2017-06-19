package edu.getjedi.frontend.mobile.state;

import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import edu.getjedi.frontend.mobile.DialogDecorator;
import edu.getjedi.frontend.mobile.DialogType;
import edu.getjedi.frontend.mobile.StringTable;
import edu.getjedi.frontend.mobile.network.RequestType;
import edu.getjedi.schema.Client;

/**
 * State of the application when a client is logged.
 * Also a Google Map event handler, as the client user interacts with the map requesting services.
 */
public class ClientLoggedState implements AppState, GoogleMap.OnInfoWindowClickListener{
    private Handler timer;
    private String rayFilter;
    private String priceFilter;
    private GoogleMap map;
    private AppContext context;
    private JSONArray services;
    private JSONArray users;
    private ArrayList<Marker> markers;
    private String choosenOne;

    @Override
    public void performAction(final AppContext context, Object action) {
        if (action == null) { // Verifies if user is still logged, this is called when the application begins or is resumed
            if (context.getUser() == null) {
                context.setState(new BeginState());
            } else {
                init(context);
            }
        } else if (action instanceof GoogleMap){ // Received a working Google Map instance to be used.
            initMap((GoogleMap) action);
        } else if(action instanceof JSONArray){ // Receives the service offers nearby and professionals offering them.
            parseResponses((JSONArray) action);
            if(users != null && services != null){
                updateMap(); // Updates the map
            }
        } else if(action instanceof String[]){ // User has changed service filter values.
            rayFilter = ((String[])action)[0];
            priceFilter = ((String[])action)[1];
        } else if(action instanceof Boolean){ // User has prompted a service.
            if(choosenOne != null){
                sendJobRequest(context);
            }
        }
    }

    /**
     * Dispatches a service request to the server, which will inform a professional of this user desire
     * to be a client.
     */
    private void sendJobRequest(AppContext context) {
        String[] query = findProfessionalAndServiceByEmail(choosenOne);
        HashMap<String,String> map = new HashMap<>();
        map.put("clientId", context.getUser().getId());
        map.put("professionalId", query[0]);
        map.put("serviceId", query[1]);
        map.put("startDate", "0");
        map.put("endDate", "0");
        map.put("jobStatus", "0");
        map.put("clientRating", "5");
        map.put("professionalRating", "5");
        context.getScreen().getHttpHandler().makePOSTRequestForObject(new String[]{"createJob"}, map);
    }

    /**
     * Verifies if the server sent the nearby services or nearby professionals, and stores them in memory,
     * to be represented in the map for the user to navigate.
     */
    private void parseResponses(JSONArray action) {
        try {
            if(action.length() > 0) {
                JSONObject obj = action.getJSONObject(0);
                if (obj.has("username")) {
                    users = action;
                } else if (obj.has("professionalId")){
                    services = action;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sets the map to send clicks to this class.
     */
    private void initMap(GoogleMap action) {
        map = action;
        map.setOnInfoWindowClickListener(this);
    }

    /**
     * Initializes this state.
     * For every context.getUpdateInterval(), queries the server for services and professionals nearby.
     * Changes the left menu content to show the options available for a Client.
     */
    private void init(final AppContext context) {
        context.getScreen().setMenuItems(new String[]{context.getUser().getFirstName() + " " + context.getUser().getLastName(), StringTable.CLIENT, " ", StringTable.FILTER_TITLE, StringTable.LOGOFF});
        this.context = context;
        timer = new Handler();
        services = users = null;
        priceFilter = rayFilter = null;
        markers = new ArrayList<>();
        timer.postDelayed(new Runnable() {
            @Override
            public void run() {
                context.getScreen().getHttpHandler().makeRequestForArray(new String[]{"services"});
                context.getScreen().getHttpHandler().makeRequestForArray(new String[]{"users"});
                if (context.getState() instanceof ClientLoggedState) {
                    timer.postDelayed(this, context.getUpdateInterval());
                }
            }
        }, context.getUpdateInterval());
    }

    /**
     * Draws into the Google Map all the services makers, and populate them with service and professional information.
     * Called for each map update.
     */
    private void updateMap() {
        cleanMarkers();
        try {
            for (int i = 0; i < services.length(); i++) {
                JSONObject service = services.getJSONObject(i);
                for (int j = 0; j < users.length(); j++) {
                    JSONObject user = users.getJSONObject(j);
                    if (service.getString("professionalId").equals(user.getString("_id")) && filterPrice(service.getInt("hourlyPrice"))
                            && filterRay(user.getString("latitude"), user.getString("longitude"))){
                        drawMarkerOnMap(Double.parseDouble(user.getString("latitude")),
                                Double.parseDouble(user.getString("longitude")),
                                service.getString("category"),
                                service.getString("serviceName"),
                                service.getString("description"),
                                service.getString("hourlyPrice"),
                                user.getString("name"),
                                user.getString("lastName"),
                                user.getString("mail"));
                        break;
                    }
                }
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
    }

    /**
     * Applies the ray filter for updateMap(), returning true if the service passes the maximum ray requirement.
     */
    private boolean filterRay(String latitude, String longitude) {
        try {
            double radius;
            double x0 = context.getUser().getCoordinates().latitude;
            double y0 = context.getUser().getCoordinates().longitude;
            double x1 = Double.parseDouble(latitude);
            double y1 = Double.parseDouble(longitude);
            radius = Double.parseDouble(this.rayFilter);

            //Ratio for earth surface - Harvarsine Formula
            double deglen = 110.25;
            double x = x1 - x0;
            double y = (y1 - y0) * Math.cos(x0);
            double distance = deglen * Math.sqrt(x*x + y*y);

            return ( distance <= radius);
        }catch (Exception e){
            return true;
        }
    }

    /**
     * Applies the price filter for updateMap(), returning true if the service passes the maximum price requirement.
     */
    private boolean filterPrice(int hourlyPrice) {
        double priceFilter;
        try {
            priceFilter = Double.parseDouble(this.priceFilter);
        }catch (Exception e){
            priceFilter = Double.MAX_VALUE;
        }
        return hourlyPrice <= priceFilter;
    }

    /**
     * Returns a String array with the professional id for a email into the position 0, and the service it offers into position 1
     */
    private String[] findProfessionalAndServiceByEmail(String email){
        String[] results = new String[2];
        results[0] = "";
        results[1] = "";
        try {
            for (int j = 0; j < users.length(); j++) {
                JSONObject user = users.getJSONObject(j);
                if (user.getString("mail").equals(email)) {
                    results[0] = user.getString("_id");
                    break;
                }
            }
            for (int j = 0; j < services.length(); j++) {
                JSONObject service = services.getJSONObject(j);
                if (service.getString("professionalId").equals(results[0])) {
                    results[1] = service.getString("_id");
                    break;
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return results;
    }

    private void cleanMarkers(){
        for(Marker e : markers){
            e.remove();
        }
    }

    /**
     * Draws a marker on the map and builds it's description window.
     */
    private void drawMarkerOnMap(double lat, double lng, String category, String name, String desc, String price, String username, String lastName, String email){
        if(map != null) {
            Marker marker = map.addMarker(new MarkerOptions().position(new LatLng(lat,lng)).
                    title(category + ": " + name).snippet(desc + "\nR$" + price + " (hora)\n" + username + " " + lastName + "\n" + email)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
            markers.add(marker);
        }
    }

    /**
     * Handles user map clicks.
     */
    @Override
    public void onInfoWindowClick(Marker marker) {
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(marker.getPosition(),15)); // Move to clicked service marker
        DialogDecorator dialogDecorator = new DialogDecorator();
        String[] snippetLines = marker.getSnippet().split("\n");
        choosenOne = snippetLines[snippetLines.length - 1];
        if(choosenOne.length() > 2) {
            // Prompts user for confirmation
            dialogDecorator.getDialog(DialogType.CONFIRM_JOB, context.getScreen(), context.getScreen().getHttpHandler()).show();
        }

    }
}
