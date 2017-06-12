package edu.getjedi.frontend.mobile.state;

import edu.getjedi.schema.Client;

/**
 * Created by Administrador on 11/06/2017.
 */

public class BeginState implements AppState{
    @Override
    public void performAction(AppContext context, Object action) {
        if(context.getUser() == null){
            context.getScreen().revealDrawer();
        }else{
            if(context.getUser() instanceof Client) {
                context.setState(new ClientLoggedState());
            }else{
                context.setState(new ProfessionalLoggedState());
            }
        }
    }
}
