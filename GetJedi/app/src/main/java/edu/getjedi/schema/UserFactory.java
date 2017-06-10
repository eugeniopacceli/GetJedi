package edu.getjedi.schema;

/**
 * Created by Administrador on 10/06/2017.
 */

public class UserFactory {
    public static User create(String id, String name, UserType type){
        if(type == UserType.CLIENT) {
            return new Client(id, name);
        }else{
            return new Professional(id, name);
        }
    }
}
