package com.sharedclipboard;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.sharedclipboard.storage.db.models.Clipping;

import java.util.List;

/**
 * Created by reshmi on 5/24/16.
 */
public class GridViewCustomAdapter extends ArrayAdapter
{
    Context context;
    List<Clipping> list;

    final CharSequence[] items = {
            "Edit", "Delete", "Export"
    };

    public GridViewCustomAdapter(Context context)
    {
        super(context, 0);
        this.context=context;

        //get clippings from database
        list = Clipping.getAllClippings(SharedClipperApp.getDb(context).getClippingAll(true));

    }

    public int getCount()
    {
        return list.size();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent)
    {
        View row = convertView;
        Log.d("TAGG", "Came here");
        if (row == null)
        {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(R.layout.grid_row, parent, false);
        }

        TextView textViewTitle = (TextView) row.findViewById(R.id.textView);
        TextView textViewInfo = (TextView) row.findViewById(R.id.textViewInfo);
        ImageView imageViewIte = (ImageView) row.findViewById(R.id.imageView);
        imageViewIte.setImageResource(R.drawable.settings);
//            imageViewIte.setImageResource(R.mipmap.info_icon);
        textViewInfo.setText("" + list.get(position).getClipping());
//            imageViewIte.setImageResource(R.drawable.ic_launcher);


        imageViewIte.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                //                                  builder.setTitle("Options");
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        // Do something with the selection
                        if(item==1)
                            SharedClipperApp.getDb(context).deleteClipping(list.get(position).getId());

                        MainActivity.gridViewCustomeAdapter = new GridViewCustomAdapter(context);
                        // Set the Adapter to GridView
                        MainActivity.gridView.setAdapter(MainActivity.gridViewCustomeAdapter);
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });

        textViewTitle.setText(""+list.get(position).getDate());
//            if(position%2==0)
//            {
//                textViewTitle.setText("3 mins ago");
//
//            } else {
//                textViewTitle.setText("12 mins ago");
////                imageViewIte.setImageResource(R.drawable.psm_alarm_clock2);
//            }
        return row;

    }

}