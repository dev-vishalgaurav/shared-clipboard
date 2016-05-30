package com.sharedclipboard.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;

import com.sharedclipboard.R;
import com.sharedclipboard.storage.preferences.PreferenceUtils;
import com.sharedclipboard.ui.activity.ScreenSlideActivity;

/**
 * Created by Girouard23 on 5/20/16.
 */
public class PreferencesFragment extends PreferenceFragment {

    public PreferencesFragment(){

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the preferences from the preferences xml file
        addPreferencesFromResource(R.xml.preferences);
        findPreference("passcode").setTitle(PreferenceUtils.getString(getActivity(),"passcode","Unknown"));
        findPreference(PreferenceUtils.PREF_EMAIL).setTitle(PreferenceUtils.getString(getActivity(),PreferenceUtils.PREF_EMAIL,"sample@sample.com"));
        findPreference("help").setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Intent intent = new Intent(getActivity(), ScreenSlideActivity.class);
                intent.putExtra(ScreenSlideActivity.EXTRA_IS_SETTINGS_LAUNCH,true);
                //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                return true;
            }
        });
    }
}
