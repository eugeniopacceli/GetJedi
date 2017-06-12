package edu.getjedi.frontend.mobile;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import edu.getjedi.frontend.mobile.network.HTTPHandler;

/**
 * Created by Administrador on 10/06/2017.
 */

public class DrawerMenuHandler implements ListView.OnItemClickListener {

    private ListView drawerList;
    private MainMapActivity context;
    private String[] content;

    public DrawerMenuHandler(MainMapActivity context, ListView listView, String[] items){
        this.drawerList = listView;
        if(items == null){
            items = new String[]{ StringTable.login, StringTable.register };
        }
        this.context = context;
        this.content = items;
        drawerList.setAdapter(new ArrayAdapter<String>(context,
                R.layout.menu_list, content));
        // Set the list's click listener
        drawerList.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (content[position]){
            case StringTable.login:
                context.showLoginForm();
                break;
            case StringTable.register:
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(HTTPHandler.SERVER_URL+"/register"));
                context.startActivity(browserIntent);
        }
    }
}
