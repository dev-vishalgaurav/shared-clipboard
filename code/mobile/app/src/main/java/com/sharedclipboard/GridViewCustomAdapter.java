package com.sharedclipboard;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by reshmi on 5/24/16.
 */
public class GridViewCustomAdapter extends ArrayAdapter
{
    Context context;

    final CharSequence[] items = {
            "Edit", "Delete", "Export"
    };

    public GridViewCustomAdapter(Context context)
    {
        super(context, 0);
        this.context=context;

    }

    public int getCount()
    {
        return 24;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        View row = convertView;

        if (row == null)
        {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(R.layout.grid_row, parent, false);


            TextView textViewTitle = (TextView) row.findViewById(R.id.textView);
            TextView textViewInfo = (TextView) row.findViewById(R.id.textViewInfo);
            ImageView imageViewIte = (ImageView) row.findViewById(R.id.imageView);
            imageViewIte.setImageResource(R.drawable.settings);
//            imageViewIte.setImageResource(R.mipmap.info_icon);
            textViewInfo.setText("Flight 804, carrying 66 an people en route to Cairo from Paris, disappeared shortly before it was due. An aging musical group");
//            imageViewIte.setImageResource(R.drawable.ic_launcher);

            imageViewIte.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    //                                  builder.setTitle("Options");
                    builder.setItems(items, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int item) {
                            // Do something with the selection

                        }
                    });
                    AlertDialog alert = builder.create();
                    alert.show();
                }
            });

            if(position%2==0)
            {
                textViewTitle.setText("3 mins ago");

            } else {
                textViewTitle.setText("12 mins ago");
//                imageViewIte.setImageResource(R.drawable.psm_alarm_clock2);
            }
        }



        return row;

    }

}