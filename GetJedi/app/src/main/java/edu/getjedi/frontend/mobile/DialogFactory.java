package edu.getjedi.frontend.mobile;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import java.util.ArrayList;

import edu.getjedi.frontend.mobile.network.HTTPHandler;
import edu.getjedi.frontend.mobile.network.RequestType;

/**
 * Created by Administrador on 12/06/2017.
 */

public class DialogFactory {

    public DialogFactory(){
    }

    public AlertDialog getDialog(DialogType type,Activity context, HTTPHandler connection){
        switch (type){
            case LOGIN: return getLoginDialog(context, connection);
            case FILTER: return getRayFilterDialog(context, connection);
            case SERVICES: return getServicesFilterDialog(context, connection);
        }
        return null;
    }

    private AlertDialog getLoginDialog(Activity context, final HTTPHandler connection){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        // Get the layout inflater
        LayoutInflater inflater = context.getLayoutInflater();
        View layout = inflater.inflate(R.layout.dialog_login, null);
        final EditText login = (EditText)layout.findViewById(R.id.username);
        final EditText password = (EditText)layout.findViewById(R.id.password);

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(layout)
                // Add action buttons
                .setPositiveButton(StringTable.OK, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        connection.makeRequest(new String[]{"maillogin",login.getText().toString(),password.getText().toString()}, RequestType.GET);
                    }
                })
                .setNegativeButton(StringTable.CANCEL, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });
        return builder.create();
    }

    private AlertDialog getRayFilterDialog(Activity context, HTTPHandler connection){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        // Get the layout inflater
        LayoutInflater inflater = context.getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(inflater.inflate(R.layout.ray_form, null))
                // Add action buttons
                .setPositiveButton(StringTable.OK, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                    }
                })
                .setNegativeButton(StringTable.CANCEL, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });
        return builder.create();
    }

    private AlertDialog getServicesFilterDialog(Activity context, HTTPHandler connection){
        final ArrayList<String> mSelectedItems = new ArrayList();  // Where we track the selected items
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        StringTable.services.add("ok");
        StringTable.services.add("ok1");
        StringTable.services.add("ok2");
        // Set the dialog title
        builder.setTitle(StringTable.FILTER_TITLE)
                // Specify the list array, the items to be selected by default (null for none),
                // and the listener through which to receive callbacks when items are selected
                .setMultiChoiceItems(StringTable.services.toArray(new String[]{}), null,
                        new DialogInterface.OnMultiChoiceClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which,
                                                boolean isChecked) {
                                if (isChecked) {
                                    // If the user checked the item, add it to the selected items
                                    mSelectedItems.add(StringTable.services.get(which));
                                } else if (mSelectedItems.contains(which)) {
                                    // Else, if the item is already in the array, remove it
                                    mSelectedItems.remove(Integer.valueOf(which));
                                }
                            }
                        })
                // Set the action buttons
                .setPositiveButton(StringTable.FILTER_CONFIRM, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // User clicked OK, so save the mSelectedItems results somewhere
                        // or return them to the component that opened the dialog
                    }
                })
                .setNegativeButton(StringTable.CANCEL, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });
        return builder.create();
    }

}
