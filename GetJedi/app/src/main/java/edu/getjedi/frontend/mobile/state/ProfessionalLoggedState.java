package edu.getjedi.frontend.mobile.state;

import edu.getjedi.frontend.mobile.StringTable;
import edu.getjedi.schema.Client;

/**
 * Created by Administrador on 11/06/2017.
 */

public class ProfessionalLoggedState implements AppState{
    @Override
    public void performAction(AppContext context, Object action) {
        if(action == null) {
            if (context.getUser() == null) {
                context.setState(new BeginState());
            } else {
                context.getScreen().setMenuItems(new String[]{context.getUser().getFirstName() + " " + context.getUser().getLastName(), " ", StringTable.LOGOFF});
            }
        }else{

        }
    }
}
