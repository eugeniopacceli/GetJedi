package edu.getjedi.frontend.mobile.state;

import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;

import edu.getjedi.frontend.mobile.DialogDecorator;
import edu.getjedi.frontend.mobile.DialogType;
import edu.getjedi.frontend.mobile.MainMapActivity;
import edu.getjedi.frontend.mobile.StringTable;
import edu.getjedi.frontend.mobile.network.RequestType;
import edu.getjedi.schema.Client;
import edu.getjedi.schema.Job;
import edu.getjedi.schema.User;

/**
 * State of the application when a professional.
 */
public class ProfessionalLoggedState implements AppState{
    private Handler timer;
    private User user;
    private GoogleMap map;
    private AppContext context;
    private Job currentJob;

    @Override
    public void performAction(final AppContext context, Object action) {
        if(action == null) { // Verifies if user is still logged, this is called when the application begins or is resumed
            if (context.getUser() == null) {
                context.setState(new BeginState());
            } else {
                init(context);
            }
        } else if (action instanceof GoogleMap){ // Received a working Google Map instance to be used.
            map = (GoogleMap) action;
            map.clear();
        } else if (action instanceof String[]){ // User has inputted a new service offer to be shown to the clients.
            String[] params = (String[])action;
            dispatchService(params[0], params[1], params[2], params[3]);
        } else if (action instanceof JSONArray){  // Job offer received
            JSONArray jobs = (JSONArray)action;
            DialogDecorator dialog = new DialogDecorator();
            try {
                if(jobs.length() > 0 && jobs.getJSONObject(0).has("jobStatus") && jobs.getJSONObject(0).has("clientId")){
                    //for (int j = 0; j < jobs.length(); j++) {
                        JSONObject job = jobs.getJSONObject(0);
                        dialog.getDialog(DialogType.CONFIRM_JOB, context.getScreen(), context.getScreen().getHttpHandler()).show();
                        currentJob = new Job();
                        currentJob.setId(job.getString("_id"));
                    //}
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        } else if (action instanceof Boolean){ // Job offer received and the application user (a professional) has confirmed or denied it.
            if(((Boolean)action).booleanValue()) {
                DialogDecorator dialog = new DialogDecorator();
                dialog.getDialog(DialogType.PROGRESS, context.getScreen(), context.getScreen().getHttpHandler()).show();
                context.getScreen().getHttpHandler().makeRequestForObject(new String[]{"updateJobStatus", currentJob.getId(), "1"});
            }else{
                context.getScreen().getHttpHandler().makeRequestForObject(new String[]{"updateJobStatus", currentJob.getId(), "2"});
            }
        }
    }

    /**
     * Initializes this state.
     * This state sends to the server the user location for every context.getUpdateInterval() miliseconds, and also
     * queries for job proposals made by clients. Responses received in performAction, as the action object.
     * Changes the left menu content to show the options available for a Professional.
     */
    private void init(final AppContext context) {
        context.getScreen().setMenuItems(new String[]{context.getUser().getFirstName() + " " + context.getUser().getLastName(), StringTable.PROFESSIONAL," ", StringTable.OFFER_TITLE, StringTable.LOGOFF});
        user = context.getUser();
        timer = new Handler();
        this.context = context;
        timer.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(user != null && user.getCoordinates() != null) {
                    context.getScreen().getHttpHandler().makeRequestForObject(new String[]{"updateLocation",
                            user.getId(),
                            Double.toString(user.getCoordinates().latitude),
                            Double.toString(user.getCoordinates().longitude)});
                }
                if(user != null){
                    context.getScreen().getHttpHandler().makeRequestForArray(new String[]{"getOpenedJobs",
                            user.getId()});
                }
                if (context.getState() instanceof ProfessionalLoggedState) {
                    timer.postDelayed(this, context.getUpdateInterval());
                }
            }
        }, context.getUpdateInterval());
    }

    /**
     * Dispatches a new service offer to the server.
     */
    public void dispatchService(String category, String name, String description, String price){
        HashMap<String, String> map = new HashMap<String, String>();
        if(context != null && context.getUser() != null) {
            map.put("category", category);
            map.put("serviceName", name);
            map.put("description", description);
            map.put("hourlyPrice", price);
            map.put("professionalId", context.getUser().getId());

            context.getScreen().getHttpHandler().makePOSTRequestForObject(new String[]{"createService"}, map);
        }
    }
}
