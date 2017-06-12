package edu.getjedi.frontend.mobile.state;

/**
 * Created by Administrador on 11/06/2017.
 */

public interface AppState {
    public void performAction(AppContext context, Object action);
}
