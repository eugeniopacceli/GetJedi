package edu.getjedi.frontend.mobile.state;

import edu.getjedi.frontend.mobile.MainMapActivity;
import edu.getjedi.schema.User;

/**
 * Created by Administrador on 11/06/2017.
 */

public class AppContext {
    private User user;
    private AppState state;
    private static AppContext appContext = null;
    private static final int UPDATE_INTERVAL_DEFAULT = 5000;
    private MainMapActivity screen;
    private int updateInterval;

    private AppContext(MainMapActivity screen){
        user = null;
        setState(new BeginState());
        this.screen = screen;
        this.updateInterval = UPDATE_INTERVAL_DEFAULT;
    }

    public void performAction(Object action){
        state.performAction(this, action);
    }

    public void setState(AppState state){
        this.state = state;
    }

    public AppState getState(){
        return this.state;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public static AppContext getInstanceOf(MainMapActivity screen){
        if(appContext == null){
            appContext = new AppContext(screen);
        }
        return appContext;
    }

    public MainMapActivity getScreen() {
        return screen;
    }

    public int getUpdateInterval() {
        return updateInterval;
    }

    public void setUpdateInterval(int updateInterval) {
        this.updateInterval = updateInterval;
    }
}
