package edu.getjedi.frontend.mobile.state;

import edu.getjedi.schema.Client;

/**
 * Created by Administrador on 11/06/2017.
 */

public class ClientLoggedState implements AppState{
    @Override
    public void performAction(AppContext context, Object action) {
        if(context.getUser() == null){
            context.setState(new BeginState());
        }
    }
}
