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
import edu.getjedi.frontend.mobile.state.ClientLoggedState;

public class MainMapActivity extends FragmentActivity implements OnMapReadyCallback {
    private UserLocationHandler locationHandler;
    private HTTPHandler httpHandler;
    private DrawerMenuHandler menuHandler;
    private DrawerLayout drawerLayout;
    private ListView listMenu;
    private AppContext appContext;
    private GoogleMap googleMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_main);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        // Set the list's click listener
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        listMenu = (ListView) findViewById(R.id.left_drawer);
    }

    private boolean hasLocationAndInternetPermissions() {
        return ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.INTERNET) == PackageManager.PERMISSION_GRANTED;
    }

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

    public void revealDrawer(){
        drawerLayout.openDrawer(Gravity.LEFT);
    }

    @Override
    protected void onStart(){
        super.onStart();
        appContext = AppContext.getInstanceOf(this);
        if(Memory.hasFile(this)){
            appContext.setUser(Memory.load(this));
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

    @Override
    protected void onStop(){
        super.onStop();
        if(appContext.getUser() != null){
            Memory.save(this, appContext.getUser());
        }else{
            Memory.clean(this);
        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
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
        locationHandler.setGoogleMap(googleMap);
        this.getAppContext().performAction(googleMap);
    }

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
        //here you can handle orientation change
    }
}
