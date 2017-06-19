package edu.getjedi.schema;

/**
 * The Professional class represents a person who offers services, in our application schema.
 */
public class Professional extends User {
    public Professional(String id, String email, String username) {
        super(id, email, username);
    }
}
