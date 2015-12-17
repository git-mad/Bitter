package gitmad.bitter.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;

/**
 * Created by Clayton on 10/7/2015.
 */
public class TopPostFragment extends Fragment {
    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public TopPostFragment() {
    }

    public static TopPostFragment newInstance() {
        TopPostFragment fragment = new TopPostFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }
}
