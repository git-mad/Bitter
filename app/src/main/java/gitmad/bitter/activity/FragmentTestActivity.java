package gitmad.bitter.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import gitmad.bitter.R;
import gitmad.bitter.fragment.UserProfileFragment;

public class FragmentTestActivity extends ActionBarActivity implements UserProfileFragment.OnFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_test);
    }

    @Override
    public void onFragmentInteraction(String id) {

    }
}
