package com.sharedclipboard.service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.sharedclipboard.HomeActivity;
import com.sharedclipboard.R;
import com.sharedclipboard.SharedClipperApp;
import com.sharedclipboard.storage.db.models.Clipping;
import com.sharedclipboard.ui.widget.ClippingWidget;

public class ClipListenerService extends Service {

    public static final String EXTRA_ACTION_TYPE = "extra_action_type";
    public static final String EXTRA_CLIPPING_ID = "extra_clipping_id";
    public static final int ACTION_TYPE_CLICK = 1;



    private ClipboardManager mClipManager = null;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e("VVV","ClipListenerService :- onCreate");
        initClipper();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(intent.hasExtra(EXTRA_ACTION_TYPE)){
            doClippingAction(intent);
        }
        return START_NOT_STICKY;
    }

    private void doClippingAction(Intent intent){
        Log.e("VVV","doClippingAction");
        int actionType = intent.getIntExtra(EXTRA_ACTION_TYPE,-1);
        if(actionType == ACTION_TYPE_CLICK){
            Log.e("VVV","ACTION_TYPE_CLICK");
            long id = intent.getLongExtra(EXTRA_CLIPPING_ID,-1);
            Log.e("VVV", "clicked clipping id = " + id);
            if(id > 0){
                copyToClipboard(id);
            }
        }
    }

    private void copyToClipboard(long id) {
        Log.e("VVV", "ClipListenerService copyToClipboard() id = " + id);
        try {
            Clipping clipping = new Clipping(SharedClipperApp.getDb(getBaseContext()).getClipping(id));
            if(mClipManager != null){
                mClipManager.removePrimaryClipChangedListener(mClipListener);
                mClipManager.setPrimaryClip(clipping.toClipData());
                mClipManager.addPrimaryClipChangedListener(mClipListener);
                Toast.makeText(getBaseContext(),getString(R.string.copied_to_clipboard),Toast.LENGTH_SHORT).show();
                Log.e("VVV","copied to clipboard");
            }
        }catch (Exception ex){
            Log.e("VVV" , "Exception in copying clipping");
            ex.printStackTrace();
        }

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
            if(clip.getItemCount() > 0) {
                ClipData.Item item  = clip.getItemAt(0);
                if(item!=null && item.getText() != null) {
                    Clipping clipping = new Clipping(item.getText().toString(),1, System.currentTimeMillis());
                    long id = SharedClipperApp.getDb(getBaseContext()).insertClipping(clipping);
                    Log.e("VVV","Clipping insert ID = " + id );
                    sendNotification(item.getText().toString());
                    updateWidgets();
                }
            }
        }
    };

    private void updateWidgets() {
        Log.e("VVV","ClipListenerService :- updateWidgets");
        Intent intent = new Intent(this,ClippingWidget.class);
        intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        int ids[] = AppWidgetManager.getInstance(getApplication()).getAppWidgetIds(new ComponentName(getApplication(), ClippingWidget.class));
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS,ids);
        sendBroadcast(intent);
    }

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
