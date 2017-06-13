package edu.getjedi.frontend.mobile.io;

import android.content.Context;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import edu.getjedi.schema.User;

/**
 * Created by Administrador on 11/06/2017.
 */

public class Memory {
    public static String FILENAME = "user.data";

    public static boolean hasFile(Context context){
        return context.fileList().length > 0;
    }

    public static User load(Context context){
        User user = null;
        try {
            FileInputStream inputFile = context.openFileInput(FILENAME);
            ObjectInputStream inputObject = new ObjectInputStream(inputFile);
            user = (User) inputObject.readObject();
            inputObject.close();
        }catch (Exception e){
        }
        return user;
    }

    public static void clean(Context context){
        if(hasFile(context)){
            context.deleteFile(FILENAME);
        }
    }

    public static void save(Context context,User user){
        try {
            FileOutputStream outputFile = context.openFileOutput(FILENAME, Context.MODE_PRIVATE);
            ObjectOutputStream outputObject = new ObjectOutputStream(outputFile);
            outputObject.writeObject(user);
            outputObject.close();
        }catch (Exception e){
        }
    }
}
