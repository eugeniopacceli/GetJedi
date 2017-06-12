package edu.getjedi.schema;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;

public abstract class User implements Serializable{
    protected String id;
    protected String name;
    protected LatLng coordinates;
    protected String token;

    public User(String id, String name, String token) {
        this.id = id;
        this.name = name;
        this.token = token;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LatLng getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(LatLng coordinates) {
        this.coordinates = coordinates;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
