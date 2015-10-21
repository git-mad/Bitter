package gitmad.bitter.fragment;

import android.app.Fragment;
import android.os.Bundle;

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
