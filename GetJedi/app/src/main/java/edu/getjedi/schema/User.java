package edu.getjedi.schema;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;

/**
 * The User class represents a user, which can be a professional or a client. An object of this
 * class is saved to the Android's internal file system memory when the user is logged and exiting
 * the application.
 */
public abstract class User implements Serializable{
    protected String id;
    protected String firstName;
    protected String lastName;
    protected String username;
    protected String email;
    protected LatLng coordinates;

    public User(String id, String email, String username) {
        this.id = id;
        this.email = email;
        this.username = username;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String name) {
        this.firstName = name;
    }

    public LatLng getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(LatLng coordinates) {
        this.coordinates = coordinates;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
