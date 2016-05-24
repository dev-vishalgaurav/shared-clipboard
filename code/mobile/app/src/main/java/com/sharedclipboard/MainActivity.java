package com.sharedclipboard;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

public class MainActivity extends Activity
{
    GridView gridView;
    GridViewCustomAdapter gridViewCustomeAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


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

}