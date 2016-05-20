package com.sharedclipboard;

import android.app.Application;
import android.util.Log;

import com.sharedclipboard.service.ClipListenerService;

/**
 * Created by Vishal Gaurav on 5/20/16.
 */
public class SharedClipperApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Log.e("VVV","SharedClipperApp :- onCreate");
        ClipListenerService.start(getBaseContext());
    }
}
