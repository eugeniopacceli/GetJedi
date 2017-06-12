package edu.getjedi.frontend.mobile;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

import edu.getjedi.frontend.mobile.io.Memory;
import edu.getjedi.frontend.mobile.network.HTTPHandler;
import edu.getjedi.frontend.mobile.state.AppContext;

public class MainMapActivity extends FragmentActivity implements OnMapReadyCallback {
    private UserLocationHandler locationHandler;
    private HTTPHandler httpHandler;
    private DrawerMenuHandler menuHandler;
    private DrawerLayout drawerLayout;
    private ListView listMenu;
    private AppContext appContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
            Toast.makeText(getApplicationContext(),StringTable.sensorException,Toast.LENGTH_LONG).show();
        }
    }

    public void revealDrawer(){
        drawerLayout.openDrawer(Gravity.LEFT);
    }

    public void showLoginForm(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // Get the layout inflater
        LayoutInflater inflater = this.getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(inflater.inflate(R.layout.dialog_login, null))
                // Add action buttons
                .setPositiveButton(R.string.login, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // sign in the user ...
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });
        builder.create().show();
    }

    @Override
    protected void onStart(){
        super.onStart();
        appContext = AppContext.getInstanceOf(this);
        if(Memory.hasFile(this)){
            appContext.setUser(Memory.load(this));
        }
        appContext.performAction(null);
        menuHandler = new DrawerMenuHandler(this, listMenu, null);
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
        locationHandler.setGoogleMap(googleMap);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if( grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            initializeSensorsAccess();
        } else {
            Toast.makeText(getApplicationContext(), StringTable.sensorDenied,Toast.LENGTH_LONG).show();
        }
        return;
    }
}
