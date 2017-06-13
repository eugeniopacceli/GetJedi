package edu.getjedi.frontend.mobile.state;

import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import edu.getjedi.frontend.mobile.DialogFactory;
import edu.getjedi.frontend.mobile.DialogType;
import edu.getjedi.frontend.mobile.StringTable;
import edu.getjedi.frontend.mobile.network.HTTPHandler;
import edu.getjedi.schema.Client;
import edu.getjedi.schema.User;
import edu.getjedi.schema.UserFactory;
import edu.getjedi.schema.UserType;

/**
 * Created by Administrador on 11/06/2017.
 */

public class BeginState implements AppState{
    @Override
    public void performAction(AppContext context, Object action) {
        if(action == null) {
            if (context.getUser() == null) {
                context.getScreen().setMenuItems(null);
                context.getScreen().revealDrawer();
            } else {
                if (context.getUser() instanceof Client) {
                    context.setState(new ClientLoggedState());
                    context.performAction(null);
                } else {
                    context.setState(new ProfessionalLoggedState());
                    context.performAction(null);
                }
            }
        }else{
            try {
                JSONArray array = (JSONArray)action;
                if(array.length() == 0){
                    new DialogFactory().getDialog(DialogType.LOGIN, context.getScreen(), HTTPHandler.getInstanceOf(context.getScreen())).show();
                    Toast.makeText(context.getScreen().getApplicationContext(), StringTable.USER_NOT_FOUND,Toast.LENGTH_LONG).show();
                }else {
                    JSONObject obj = array.getJSONObject(0);
                    UserFactory factory = new UserFactory();
                    User user = factory.getUser(obj.getString("_id"),
                            obj.getString("mail"),
                            obj.getString("username"),
                            UserType.values()[obj.getInt("userType")]);
                    user.setFirstName(obj.getString("name"));
                    user.setLastName(obj.getString("lastName"));
                    context.setUser(user);
                    context.setState(user instanceof Client ? new ClientLoggedState() : new ProfessionalLoggedState());
                    context.performAction(null);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
