package com.sharedclipboard;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.sharedclipboard.storage.db.models.Clipping;
import com.sharedclipboard.ui.activity.BaseActivity;
import com.sharedclipboard.ui.activity.SettingsActivity;


public class MainActivity extends BaseActivity {
    public static GridView gridView;
    public static GridViewCustomAdapter gridViewCustomeAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Clipping clipping = new Clipping("Clipping 4",1, System.currentTimeMillis());
        long id = SharedClipperApp.getDb(getBaseContext()).insertClipping(clipping);
        clipping = new Clipping("Clipping 2",1, System.currentTimeMillis());
        long id2 = SharedClipperApp.getDb(getBaseContext()).insertClipping(clipping);

        gridView=(GridView)findViewById(R.id.gridViewCustom);
        // Create the Custom Adapter Object
        gridViewCustomeAdapter = new GridViewCustomAdapter(this);
        // Set the Adapter to GridView
        gridView.setAdapter(gridViewCustomeAdapter);

        // Handling touch/click Event on GridView Item
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View v, int position, long arg3) {
                String selectedItem;
                if(position%2==0)
                    selectedItem="Facebook";
                else
                    selectedItem="Twitter";
                Toast.makeText(getApplicationContext(), "Selected Item: " + selectedItem, Toast.LENGTH_SHORT).show();

            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}