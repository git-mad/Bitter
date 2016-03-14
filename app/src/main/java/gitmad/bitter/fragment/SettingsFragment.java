package gitmad.bitter.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.preference.EditTextPreference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.PreferenceManager;
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
                    EditTextPreference newUserName = (EditTextPreference)findPreference(key);
                    String nameChange = newUserName.getText();
                    //TODO: Add Firebase call to set the UserName change
                    Toast.makeText(getContext(), nameChange, Toast.LENGTH_SHORT).show();
                    //Clear after text is received
                    newUserName.setText("");
                }else if(key.equals("pref_change_password")){
                    EditTextPreference newPasswordPref = (EditTextPreference)findPreference(key);
                    String newPassword = newPasswordPref.getText();
                    //TODO: Add Firebase call to set the UserName change
                    Toast.makeText(getContext(),newPassword , Toast.LENGTH_SHORT).show();
                    //Clear after text is received
                    newPasswordPref.setText("");
                }


            }
        };
        sharedPreferences.registerOnSharedPreferenceChangeListener(spChanged);
    }
}
