package edu.getjedi.frontend.mobile;

import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import edu.getjedi.frontend.mobile.network.HTTPHandler;
import edu.getjedi.frontend.mobile.state.BeginState;

/**
 * The DrawerMenu controller and event handler.
 */
public class DrawerMenuHandler implements ListView.OnItemClickListener {

    private ListView drawerList;
    private MainMapActivity context;
    private String[] content;
    private DialogDecorator dialogDecorator;

    public DrawerMenuHandler(MainMapActivity context, ListView listView, String[] items){
        this.drawerList = listView;
        if(items == null){
            items = new String[]{ StringTable.APP_NAME , " " , StringTable.LOGIN, StringTable.REGISTER};
        }
        this.context = context;
        this.content = items;
        /** Draws the list to the Drawer Menu on screen with the items from the array
         *  and using the menu_list template. */
        drawerList.setAdapter(new ArrayAdapter<String>(context,
                R.layout.menu_list, content));
        // Set the list's click listener
        drawerList.setOnItemClickListener(this);
        dialogDecorator = new DialogDecorator();
    }

    /**
     * The onItemClick returns the position of the content array for the object which it's
     * representation was clicked on the interface by the user.
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (content[position]){
            case StringTable.LOGIN:
                dialogDecorator.getDialog(DialogType.LOGIN, context, HTTPHandler.getInstanceOf(context)).show();
                break;
            case StringTable.REGISTER:
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(HTTPHandler.SERVER_URL));
                context.startActivity(browserIntent);
                break;
            case StringTable.RAY_FILTER:

                break;
            case StringTable.FILTER_TITLE:
                dialogDecorator.getDialog(DialogType.FILTER, context, HTTPHandler.getInstanceOf(context)).show();
                break;
            case StringTable.SHOW_TITLE:

                break;
            case StringTable.OFFER_TITLE:
                dialogDecorator.getDialog(DialogType.SERVICES, context, HTTPHandler.getInstanceOf(context)).show();
                break;
            case StringTable.LOGOFF:
                context.getAppContext().setUser(null);
                context.getAppContext().setState(new BeginState());
                context.getAppContext().performAction(null);
        }
    }
}
