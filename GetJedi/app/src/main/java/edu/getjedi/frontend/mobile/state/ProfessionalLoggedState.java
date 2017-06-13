package edu.getjedi.frontend.mobile.state;

import android.os.CountDownTimer;
import android.os.Handler;
import android.widget.Toast;

import edu.getjedi.frontend.mobile.StringTable;
import edu.getjedi.frontend.mobile.network.RequestType;
import edu.getjedi.schema.Client;

/**
 * Created by Administrador on 11/06/2017.
 */

public class ProfessionalLoggedState implements AppState{
    private Handler timer;

    @Override
    public void performAction(final AppContext context, Object action) {
        if(action == null) {
            if (context.getUser() == null) {
                context.setState(new BeginState());
            } else {
                context.getScreen().setMenuItems(new String[]{context.getUser().getFirstName() + " " + context.getUser().getLastName(), StringTable.PROFESSIONAL," ", StringTable.OFFER_TITLE, StringTable.LOGOFF});
                timer = new Handler();
                timer.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        context.getScreen().getHttpHandler().makeRequest(new String[]{}, RequestType.GET);
                        if(context.getState() instanceof ProfessionalLoggedState){
                            timer.postDelayed(this, context.getUpdateInterval());
                        }
                    }
                }, context.getUpdateInterval());
            }
        }else{

        }
    }
}
