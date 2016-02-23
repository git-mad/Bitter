package gitmad.bitter.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import gitmad.bitter.R;

public class SettingsFragment extends PreferenceFragmentCompat {

    SharedPreferences sharedPreferences;
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
    }
}
