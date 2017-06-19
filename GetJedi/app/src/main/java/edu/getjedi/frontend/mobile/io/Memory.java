package edu.getjedi.frontend.mobile.io;

import android.content.Context;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import edu.getjedi.frontend.mobile.network.HTTPHandler;
import edu.getjedi.schema.User;

/**
 * The Memory singleton. Used to read and write User files to the system. Needs a Context, to provide to the
 * Android file API. This class is used when the User exits and enters the application, to persist it's login
 * credentials.
 */
public class Memory {
    public static String FILENAME = "user.data";
    private static Memory memory;
    private Context context;

    private Memory(Context context){
        this.context = context;
    }

    public boolean hasFile(){
        return context.fileList().length > 0;
    }

    public User load(){
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

    public void clean(){
        if(hasFile()){
            context.deleteFile(FILENAME);
        }
    }

    public void save(User user){
        try {
            FileOutputStream outputFile = context.openFileOutput(FILENAME, Context.MODE_PRIVATE);
            ObjectOutputStream outputObject = new ObjectOutputStream(outputFile);
            outputObject.writeObject(user);
            outputObject.close();
        }catch (Exception e){
        }
    }

    public static Memory getInstanceOf(Context context){
        if(memory == null){
            memory = new Memory(context);
        }
        return memory;
    }
}
