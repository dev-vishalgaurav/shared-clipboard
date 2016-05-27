package com.sharedclipboard.ui.fragment;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import com.sharedclipboard.R;
import com.sharedclipboard.storage.preferences.PreferenceUtils;

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
    }
}
