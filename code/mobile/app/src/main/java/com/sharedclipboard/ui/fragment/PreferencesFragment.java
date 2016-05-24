package com.sharedclipboard.ui.fragment;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import com.sharedclipboard.R;
import com.sharedclipboard.storage.preferences.PreferenceUtils;

/**
 * Created by Girouard23 on 5/20/16.
 */
public class PreferencesFragment extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the preferences from the preferences xml file
        addPreferencesFromResource(R.xml.preferences);
        findPreference("passcode").setTitle(PreferenceUtils.getString(getActivity(),"passcode","Unknown"));
    }
}
