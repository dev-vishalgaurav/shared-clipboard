package com.sharedclipboard.ui.fragment;

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

import com.sharedclipboard.R;

/**
 * Created by Girouard23 on 5/19/16.
 */
public class TextClippingArrayAdapter extends ArrayAdapter<String> {

    Activity context;
    final CharSequence[] items = {
            "Edit", "Delete", "Upload"
    };


    public TextClippingArrayAdapter(Context context, String s[]) {

        super(context, R.layout.text_clipping_list_item, s);
        this.context = (Activity) context;
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView= inflater.inflate(R.layout.text_clipping_list_item, null, true);
        //View rowView = super.getView(position, view, parent);
        TextView topTextView = (TextView) rowView.findViewById(R.id.top_text);
        TextView bottomTextView = (TextView) rowView.findViewById(R.id.bottom_text);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.img);

        if(position == 0) {
            topTextView.setText("20 minutes ago");
            bottomTextView.setText("An aging musical group calls it quits, but demonstrates that its music is timeless.");
        } else if(position == 1) {
            topTextView.setText("22 minutes ago");
            bottomTextView.setText("Flight 804, carrying 66 people en route to Cairo from Paris, disappeared shortly before it was due to land. The reason was unclear, but the developments touched off fears about terrorism.");
        } else {
            topTextView.setText("23 minutes ago");
            bottomTextView.setText("An overwhelming majority of Republican voters say their party’s leaders should get behind Donald J. Trump, according to the latest Times/CBS News poll.");
        }

        imageView.setImageResource(R.mipmap.info_icon);

        imageView.setOnClickListener(new View.OnClickListener() {

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

        return rowView;
    }
}