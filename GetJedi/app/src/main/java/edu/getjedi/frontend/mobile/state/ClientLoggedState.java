package edu.getjedi.frontend.mobile.state;

import android.os.CountDownTimer;
import android.os.Handler;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

import org.json.JSONArray;

import edu.getjedi.frontend.mobile.StringTable;
import edu.getjedi.frontend.mobile.network.RequestType;
import edu.getjedi.schema.Client;

/**
 * Created by Administrador on 11/06/2017.
 */

public class ClientLoggedState implements AppState, GoogleMap.OnInfoWindowClickListener{
    private Handler timer;
    private GoogleMap map;
    private AppContext context;

    @Override
    public void performAction(final AppContext context, Object action) {
        if (action == null) {
            if (context.getUser() == null) {
                context.setState(new BeginState());
            } else {
                context.getScreen().setMenuItems(new String[]{context.getUser().getFirstName() + " " + context.getUser().getLastName(), StringTable.CLIENT, " ", StringTable.FILTER_TITLE, StringTable.LOGOFF});
                this.context = context;
                timer = new Handler();
                timer.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        context.getScreen().getHttpHandler().makeRequest(new String[]{"services"}, RequestType.GET);
                        if (context.getState() instanceof ClientLoggedState) {
                            timer.postDelayed(this, context.getUpdateInterval());
                        }
                    }
                }, context.getUpdateInterval());
            }
        } else if (action instanceof GoogleMap){
            map = (GoogleMap)action;
            map.setOnInfoWindowClickListener(this);
        } else if(action instanceof JSONArray){
            Toast.makeText(context.getScreen(),"infoReceived",Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(marker.getPosition(),15));
    }
}
