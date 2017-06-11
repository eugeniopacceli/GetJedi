package edu.getjedi.frontend.mobile;

import android.app.Fragment;
import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**
 * Created by Administrador on 10/06/2017.
 */

public class DrawerMenuHandler implements ListView.OnItemClickListener {

    private ListView drawerList;
    private String[] userMenu = new String[]{"abc","def","ghi"};

    public DrawerMenuHandler(Context context, ListView listView){
        this.drawerList = listView;
        drawerList.setAdapter(new ArrayAdapter<String>(context,
                R.layout.menu_list, userMenu));
        // Set the list's click listener
        drawerList.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }
}
