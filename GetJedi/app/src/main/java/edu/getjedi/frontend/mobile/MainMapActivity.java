package edu.getjedi.frontend.mobile;

import android.Manifest;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Marker;

import edu.getjedi.frontend.mobile.io.Memory;
import edu.getjedi.frontend.mobile.network.HTTPHandler;
import edu.getjedi.frontend.mobile.state.AppContext;

/**
 * Application entry point and main interface controller.
 */
public class MainMapActivity extends FragmentActivity implements OnMapReadyCallback {
    private UserLocationHandler locationHandler;
    private HTTPHandler httpHandler;
    private DrawerMenuHandler menuHandler;
    private DrawerLayout drawerLayout;
    private ListView listMenu;
    private AppContext appContext;
    private GoogleMap googleMap;
    private Memory memory;

    /**
     * Application entry point
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Forces vertical orientation always
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_main);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        // Set the list's click listener
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        listMenu = (ListView) findViewById(R.id.left_drawer);
        memory = Memory.getInstanceOf(this);
    }

    /**
     * Returns true if the user has granted this app all the required permissions for it's proper execution.
     */
    private boolean hasLocationAndInternetPermissions() {
        return ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.INTERNET) == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * Initializes the Location Handler, for the Android Location API, and HTTP Handler, which deals with
     * the Volley API (HTTP requests).
     */
    private void initializeSensorsAccess(){
        try {
            // Acquire a reference to the system Location Manager
            // Define a listener that responds to location updates
            locationHandler = UserLocationHandler.getInstanceOf(this);
            httpHandler = HTTPHandler.getInstanceOf(this);
        }catch (Exception e){
            Toast.makeText(getApplicationContext(),StringTable.SENSOR_EXCEPTION,Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Programmatically reveals the Drawer menu (used by the states objects).
     */
    public void revealDrawer(){
        drawerLayout.openDrawer(Gravity.LEFT);
    }

    /**
     * When the application starts or returns from background.
     */
    @Override
    protected void onStart(){
        super.onStart();
        appContext = AppContext.getInstanceOf(this);
        if(memory.hasFile()){
            appContext.setUser(memory.load());
        }
        appContext.performAction(null);
        if (!hasLocationAndInternetPermissions()) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.INTERNET}, 10);
        }else {
            initializeSensorsAccess();
        }
    }

    @Override
    protected void onPause(){
        super.onPause();
    }

    @Override
    protected void onResume(){
        super.onResume();
    }

    /**
     * Application sent to background or is closing.
     */
    @Override
    protected void onStop(){
        super.onStop();
        if(appContext.getUser() != null){
            memory.save(appContext.getUser());
        }else{
            memory.clean();
        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        final MainMapActivity thisActivity = this;
        googleMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

            @Override
            public View getInfoWindow(Marker arg0) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {

                LinearLayout info = new LinearLayout(thisActivity);
                info.setOrientation(LinearLayout.VERTICAL);

                TextView title = new TextView(thisActivity);
                title.setTextColor(Color.BLACK);
                title.setGravity(Gravity.CENTER);
                title.setTypeface(null, Typeface.BOLD);
                title.setText(marker.getTitle());

                TextView snippet = new TextView(thisActivity);
                snippet.setTextColor(Color.GRAY);
                snippet.setText(marker.getSnippet());

                info.addView(title);
                info.addView(snippet);

                return info;
            }
        });
        locationHandler.setGoogleMap(googleMap); /** Send the map to the location handler */
        this.getAppContext().performAction(googleMap); /** Flags to the state object that the map is ready to use */
    }

    /**
     * User has responded to the permission request dialog, either allowing or denying the necessary
     * permissions needed to the app's correct execution.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if( grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            initializeSensorsAccess();
        } else {
            Toast.makeText(getApplicationContext(), StringTable.SENSOR_DENIED,Toast.LENGTH_LONG).show();
        }
        return;
    }

    public void setMenuItems(String[] items) {
        menuHandler = new DrawerMenuHandler(this, listMenu, items);
    }

    /** Getters and setters */

    public UserLocationHandler getLocationHandler() {
        return locationHandler;
    }

    public HTTPHandler getHttpHandler() {
        return httpHandler;
    }

    public DrawerMenuHandler getMenuHandler() {
        return menuHandler;
    }

    public DrawerLayout getDrawerLayout() {
        return drawerLayout;
    }

    public AppContext getAppContext() {
        return appContext;
    }

    public ListView getListMenu() {
        return listMenu;
    }

    public GoogleMap getGoogleMap() {
        return googleMap;
    }

    public void setGoogleMap(GoogleMap googleMap) {
        this.googleMap = googleMap;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }
}
