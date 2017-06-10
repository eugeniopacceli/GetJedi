package edu.getjedi.frontend.mobile;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

import edu.getjedi.frontend.mobile.R;

public class MainMapActivity extends FragmentActivity implements OnMapReadyCallback {
    private UserLocationHandler locationHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState){
        if(savedInstanceState.getBoolean("saved")){
            Log.i("DEBUG","Data retrieved!");
        }
        Log.i("DEBUG","onRestore!");
    }

    @Override
    protected void onStart(){
        super.onStart();
        // Acquire a reference to the system Location Manager
        // Define a listener that responds to location updates
        locationHandler = new UserLocationHandler(this);
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
        Log.i("DEBUG","onStop!");
    }

    @Override
    protected void onSaveInstanceState(Bundle state){
        state.putBoolean("saved",true);
        Log.i("DEBUG","Saving...");
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
}