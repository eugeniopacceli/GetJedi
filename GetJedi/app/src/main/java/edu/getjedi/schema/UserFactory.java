package edu.getjedi.schema;

/**
 * Created by Administrador on 10/06/2017.
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
