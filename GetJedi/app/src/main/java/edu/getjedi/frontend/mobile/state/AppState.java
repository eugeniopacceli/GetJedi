package edu.getjedi.frontend.mobile.state;

/**
 * Defines the method through which the application will send inputs to each State pattern states.
 */
public interface AppState {
    public void performAction(AppContext context, Object action);
}
