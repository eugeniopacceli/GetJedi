package edu.getjedi.frontend.mobile;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import edu.getjedi.frontend.mobile.network.HTTPHandler;

/**
 * A Decorator for the Android Dialogs API. With  a value, returns a fully built AlertDialog ready to
 * be used through show().
 *
 * Dialogs's results are set to be passed to this application state machine classes.
 *
 * HTTPHandler is needed for possible HTTP communcations.
 */
public class DialogDecorator {

    public DialogDecorator(){
    }

    public AlertDialog getDialog(DialogType type,MainMapActivity context, HTTPHandler connection){
        switch (type){
            case LOGIN: return getLoginDialog(context, connection);
            case FILTER: return getFilterDialog(context, connection);
            case SERVICES: return getServiceCreateDialog(context, connection);
            case CONFIRM_JOB: return getConfirmJobDialog(context, connection);
            case PROGRESS: return getJobProgressDialog(context,connection);
        }
        return null;
    }

    private AlertDialog getJobProgressDialog(final MainMapActivity context, HTTPHandler connection) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        // Get the layout inflater
        LayoutInflater inflater = context.getLayoutInflater();
        View layout = inflater.inflate(R.layout.working_form, null);

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(layout)
                .setPositiveButton(StringTable.OK, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        context.getAppContext().performAction(new Boolean(false));
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }

    private AlertDialog getConfirmJobDialog(final MainMapActivity context, HTTPHandler connection) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        // Get the layout inflater
        LayoutInflater inflater = context.getLayoutInflater();
        View layout = inflater.inflate(R.layout.confirmation, null);

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(layout)
                .setPositiveButton(StringTable.OK, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        context.getAppContext().performAction(new Boolean(true));
                    }
                })
                .setNegativeButton(StringTable.CANCEL, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }

    private AlertDialog getLoginDialog(MainMapActivity context, final HTTPHandler connection){
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
                        connection.makeRequestForArray(new String[]{"maillogin",login.getText().toString(),password.getText().toString()});
                    }
                })
                .setNegativeButton(StringTable.CANCEL, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });
        return builder.create();
    }

    private AlertDialog getFilterDialog(final MainMapActivity context, final HTTPHandler connection){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        // Get the layout inflater
        LayoutInflater inflater = context.getLayoutInflater();
        View layout = inflater.inflate(R.layout.filter_form, null);
        final EditText ray = (EditText)layout.findViewById(R.id.maxRayText);
        final EditText price = (EditText) layout.findViewById(R.id.maxPriceText);

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(layout)
                // Add action buttons
                .setPositiveButton(StringTable.OK, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        context.getAppContext().performAction(new String[]{ray.getText().toString(), price.getText().toString()});
                    }
                })
                .setNegativeButton(StringTable.CANCEL, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });
        return builder.create();
    }

    private AlertDialog getServiceCreateDialog(final MainMapActivity context, final HTTPHandler connection){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        // Get the layout inflater
        LayoutInflater inflater = context.getLayoutInflater();
        View layout = inflater.inflate(R.layout.service_form, null);
        final EditText category = (EditText)layout.findViewById(R.id.category);
        final EditText name = (EditText) layout.findViewById(R.id.serviceName);
        final EditText description = (EditText)layout.findViewById(R.id.description);
        final EditText price = (EditText) layout.findViewById(R.id.hourlyPrice);

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(layout)
                // Add action buttons
                .setPositiveButton(StringTable.OK, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        context.getAppContext().performAction(new String[]{category.getText().toString(), name.getText().toString(), description.getText().toString(), price.getText().toString()});
                    }
                })
                .setNegativeButton(StringTable.CANCEL, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });
        return builder.create();
    }

}
