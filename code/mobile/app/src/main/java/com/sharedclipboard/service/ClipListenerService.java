package com.sharedclipboard.service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.sharedclipboard.HomeActivity;
import com.sharedclipboard.R;
import com.sharedclipboard.SharedClipperApp;
import com.sharedclipboard.storage.db.models.Clipping;

public class ClipListenerService extends Service {
    private ClipboardManager mClipManager = null;
    @Override
    public void onCreate() {
        super.onCreate();
        Log.e("VVV","ClipListenerService :- onCreate");
        initClipper();
    }

    private void initClipper(){
        Log.e("VVV","ClipListenerService :- initClipper");
        mClipManager = (ClipboardManager)getSystemService(Context.CLIPBOARD_SERVICE);
        mClipManager.addPrimaryClipChangedListener(mClipListener);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onDestroy(){
        Log.e("VVV","onDestroy");
        ClipListenerService.start(getBaseContext());
    }

    public static void start(Context context){
        Intent intent = new Intent(context,ClipListenerService.class);
        context.startService(intent);
    }

    private ClipboardManager.OnPrimaryClipChangedListener mClipListener = new ClipboardManager.OnPrimaryClipChangedListener() {
        @Override
        public void onPrimaryClipChanged() {
            Log.e("VVV","onPrimaryClipChanged");
            ClipData clip = mClipManager.getPrimaryClip();
            int total = clip.getItemCount();
            Log.e("VVV","Total clip items = " + total);
            if(total > 0) {
                ClipData.Item item  = clip.getItemAt(0);
                if(item!=null) {
                    Clipping clipping = new Clipping(item.getText().toString(),1, System.currentTimeMillis());
                    long id = SharedClipperApp.getDb(getBaseContext()).insertClipping(clipping);
                    Log.e("VVV","Cliiping insert ID = " + id );
                    sendNotification(item.getText().toString());
                }
            }
        }
    };
    private void sendNotification(String message) {
        Intent intent = new Intent(this, HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.psm_bar)
                .setContentTitle("New Clipping")
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }
}
