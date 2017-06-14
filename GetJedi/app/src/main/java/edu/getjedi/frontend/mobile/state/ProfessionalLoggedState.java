package edu.getjedi.frontend.mobile.state;

import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;

import edu.getjedi.frontend.mobile.StringTable;
import edu.getjedi.frontend.mobile.network.RequestType;
import edu.getjedi.schema.Client;
import edu.getjedi.schema.User;

/**
 * Created by Administrador on 11/06/2017.
 */

public class ProfessionalLoggedState implements AppState{
    private Handler timer;
    private User user;
    private GoogleMap map;

    @Override
    public void performAction(final AppContext context, Object action) {
        if(action == null) {
            if (context.getUser() == null) {
                context.setState(new BeginState());
            } else {
                context.getScreen().setMenuItems(new String[]{context.getUser().getFirstName() + " " + context.getUser().getLastName(), StringTable.PROFESSIONAL," ", StringTable.OFFER_TITLE, StringTable.LOGOFF});
                user = context.getUser();
                timer = new Handler();
                timer.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if(user != null && user.getCoordinates() != null) {
                            context.getScreen().getHttpHandler().makeRequestForObject(new String[]{"updateLocation",
                                    user.getId(),
                                    Double.toString(user.getCoordinates().latitude),
                                    Double.toString(user.getCoordinates().longitude)}, RequestType.GET);
                        }
                        if (context.getState() instanceof ProfessionalLoggedState) {
                            timer.postDelayed(this, context.getUpdateInterval());
                        }
                    }
                }, context.getUpdateInterval());
            }
        }else if (action instanceof GoogleMap){
            map.clear();
        }
    }
}
