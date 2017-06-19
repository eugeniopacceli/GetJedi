package edu.getjedi.schema;

/**
 * A factory to provide a instance of User according to it's type.
 */
public class UserFactory {
    public User getUser(String id, String email, String username, UserType type){
        if(type == UserType.CLIENT) {
            return new Client(id, email, username);
        }else{
            return new Professional(id, email, username);
        }
    }
}
