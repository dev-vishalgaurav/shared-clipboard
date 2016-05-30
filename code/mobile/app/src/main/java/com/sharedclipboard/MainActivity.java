package com.sharedclipboard;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.sharedclipboard.network.ClippingRefreshAsyncTask;
import com.sharedclipboard.service.ClipListenerService;
import com.sharedclipboard.service.RegistrationIntentService;
import com.sharedclipboard.storage.db.models.Clipping;
import com.sharedclipboard.storage.preferences.PreferenceUtils;
import com.sharedclipboard.ui.activity.BaseActivity;
import com.sharedclipboard.ui.activity.SettingsActivity;


public class MainActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener {

    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

    public static GridView gridView;
    public static GridViewCustomAdapter gridViewCustomeAdapter;
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private boolean isReceiverRegistered;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gridView=(GridView)findViewById(R.id.gridViewCustom);
        gridView.setEmptyView(findViewById(R.id.txtEmpty));
        // Create the Custom Adapter Object
        // Handling touch/click Event on GridView Item
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View v, int position, long arg3) {
                if (gridViewCustomeAdapter != null) {
                    Clipping clip = gridViewCustomeAdapter.getClipping(position);
                    ClipListenerService.swapClipping(getBaseContext(), clip);
                }
            }
        });
        initGCM();
        //getSupportActionBar().setDisplayShowHomeEnabled(true);
        //getSupportActionBar().setIcon(R.drawable.sync_icon_burned);
        ClippingRefreshAsyncTask refreshAsyncTask = new ClippingRefreshAsyncTask(this);
        refreshAsyncTask.execute("");

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        mSwipeRefreshLayout.setOnRefreshListener(this);
    }

    @Override
    protected void onResume(){
        super.onResume();
        gridViewCustomeAdapter = new GridViewCustomAdapter(this);
        // Set the Adapter to GridView
        gridView.setAdapter(gridViewCustomeAdapter);
        registerReceiver();
    }

    @Override
    protected void onPause(){
        super.onPause();
        unRegisterReceiver();
    }
    private void initGCM(){
        Log.e("VVV", "initGCM");
        boolean isRegistrationNeeded = PreferenceUtils.getBoolean(getBaseContext(),PreferenceUtils.PREF_SENT_TOKEN_TO_SERVER,false);
        if(!isRegistrationNeeded) {
            mRegistrationBroadcastReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    boolean sentToken = PreferenceUtils.getBoolean(getBaseContext(), PreferenceUtils.PREF_SENT_TOKEN_TO_SERVER, false);
                    if (sentToken) {
                        showToast("GCM registration done");
                    } else {
                        showToast("GCM registration ERROR");
                    }
                }
            };
            // Registering BroadcastReceiver
            registerReceiver();

            if (checkPlayServices()) {
                // Start IntentService to register this application with GCM.
                Intent intent = new Intent(this, RegistrationIntentService.class);
                startService(intent);
            }
        }
    }

    /**
     * This method is called when swipe refresh is pulled down
     */
    @Override
    public void onRefresh() {
        //onResume();
        ClippingRefreshAsyncTask refreshAsyncTask = new ClippingRefreshAsyncTask(this);
        refreshAsyncTask.execute("");
        final Activity a = this;

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //gridViewCustomeAdapter.notifyDataSetChanged();
                gridViewCustomeAdapter = new GridViewCustomAdapter(a);
                // Set the Adapter to GridView
                gridView.setAdapter(gridViewCustomeAdapter);
                mSwipeRefreshLayout.setRefreshing(false);
            }
        }, 5000);
        Log.d("Refresh", "refresh");
    }

    public void refreshGridView() {
        gridViewCustomeAdapter = new GridViewCustomAdapter(this);
        // Set the Adapter to GridView
        gridView.setAdapter(gridViewCustomeAdapter);
    }


    private void registerReceiver(){
        if(!isReceiverRegistered) {
            LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver, new IntentFilter(PreferenceUtils.PREF_REGISTRATION_COMPLETE));
            isReceiverRegistered = true;
        }
    }
    private void unRegisterReceiver(){
        if(isReceiverRegistered) {
            LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
            isReceiverRegistered = false;
        }
    }
    /**
     * Check the device to make sure it has the Google Play Services APK. If
     * it doesn't, display a dialog that allows users to download the APK from
     * the Google Play Store or enable it in the device's system settings.
     */
    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();
            } else {
                Log.i("VVV", "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
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