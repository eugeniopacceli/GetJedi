package edu.getjedi.schema;

/**
 * Created by Administrador on 10/06/2017.
 */

public class UserFactory {
    public static User create(String id, String name, String token, UserType type){
        if(type == UserType.CLIENT) {
            return new Client(id, name, token);
        }else{
            return new Professional(id, name, token);
        }
    }
}
