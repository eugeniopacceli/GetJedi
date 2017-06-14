package edu.getjedi.frontend.mobile;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class UserLocationHandler implements LocationListener {
    private static GoogleMap googleMap;
    private static LocationManager locationManager;
    private static UserLocationHandler userLocationHandler;
    private Marker userMarker;

    private UserLocationHandler(FragmentActivity mainScreen){
        locationManager = (LocationManager) mainScreen.getSystemService(Context.LOCATION_SERVICE);
    }

    public void zoomToUser(GoogleMap map,LatLng user){
        // Zoom in, animating the camera.
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(user,15));
    }

    public void onLocationChanged(Location location) {
        // Called when a new location is found by the network location provider.
        LatLng user = new LatLng(location.getLatitude(), location.getLongitude());
        if(googleMap != null) {
            if(userMarker != null){
                userMarker.remove();
            }
            userMarker = googleMap.addMarker(new MarkerOptions().position(user).title(StringTable.USER_IN_MAP).snippet(StringTable.USER_IN_MAP_DESC).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
            zoomToUser(googleMap, user);
        }
    }

    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    public void onProviderEnabled(String provider) {
    }

    public void onProviderDisabled(String provider) {
    }

    public GoogleMap getGoogleMap() {
        return googleMap;
    }

    public void setGoogleMap(GoogleMap googleMap) {
        this.googleMap = googleMap;
    }

    public static UserLocationHandler getInstanceOf(FragmentActivity mainScreen) throws SecurityException{
        if(userLocationHandler == null){
            userLocationHandler = new UserLocationHandler(mainScreen);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, userLocationHandler);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, userLocationHandler);
        }
        return userLocationHandler;
    }
}
