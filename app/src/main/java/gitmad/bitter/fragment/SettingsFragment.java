package gitmad.bitter.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.support.v4.app.Fragment;
import android.support.v7.preference.CheckBoxPreference;
import android.support.v7.preference.EditTextPreference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.preference.SwitchPreferenceCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import gitmad.bitter.R;

public class SettingsFragment extends PreferenceFragmentCompat {

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    public SettingsFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static SettingsFragment newInstance() {
        return new SettingsFragment();
    }

    @Override
    public void onCreatePreferences(Bundle bundle, String s) {
        //add xml
        addPreferencesFromResource(R.xml.preferences);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        editor = sharedPreferences.edit();

        SharedPreferences.OnSharedPreferenceChangeListener spChanged = new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                if (key.equals("pref_change_username")) {
                    EditTextPreference newUsername = (EditTextPreference) findPreference(key);
                    String nameChange = newUsername.getText();
                    Toast.makeText(getContext(), nameChange, Toast.LENGTH_SHORT).show();
                    //Clear after text is viewed
                    newUsername.setText("");
                } else if (key.equals("pref_change_password")) {
                    EditTextPreference newPasswordPref = (EditTextPreference) findPreference(key);
                    String passwordChange = newPasswordPref.getText();
                    Toast.makeText(getContext(), passwordChange, Toast.LENGTH_SHORT).show();
                    //Clear after text is viewed
                    newPasswordPref.setText("");
                }

            }
        };
        sharedPreferences.registerOnSharedPreferenceChangeListener(spChanged);
    }
}
