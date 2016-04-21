package gitmad.bitter.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.preference.EditTextPreference;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.PreferenceManager;
import gitmad.bitter.R;

public class SettingsFragment extends PreferenceFragmentCompat {

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    SharedPreferences.OnSharedPreferenceChangeListener spChanged;

    public SettingsFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static SettingsFragment newInstance() {
        SettingsFragment fragment = new SettingsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreatePreferences(Bundle bundle, String s) {
        //add xml
        addPreferencesFromResource(R.xml.preferences);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences
                (getActivity());
        editor = sharedPreferences.edit();

        spChanged = new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences
                                                          sharedPreferences,
                                                  String key) {
                if (key.equals("pref_change_username")) {
                    EditTextPreference newUserName = (EditTextPreference)
                            findPreference(key);
                    String nameChange = newUserName.getText();
                    //TODO: Add Firebase call to set the UserName change
                    //Clear after text is received
                    newUserName.setText("");
                } else if (key.equals("pref_text_size")) {
                    ListPreference textSize = (ListPreference) findPreference
                            (key);
                    String choice = textSize.getValue();
                    //Returns Small, Medium, Large

                }
            }
        };
        sharedPreferences.registerOnSharedPreferenceChangeListener(spChanged);
    }

    @Override
    public void onResume() {
        super.onResume();
        getPreferenceScreen().getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(spChanged);
    }

    @Override
    public void onPause() {
        super.onPause();
        getPreferenceScreen().getSharedPreferences()
                .unregisterOnSharedPreferenceChangeListener(spChanged);
    }
}
