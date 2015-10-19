package gitmad.bitter.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import gitmad.bitter.R;

/**
 * Created by Clayton on 10/7/2015.
 */
public class RecentPostFragment extends android.support.v4.app.Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstancestate) {
        View v = inflater.inflate(R.layout.fragment_recent_posts, container, false);

        TextView textView = (TextView) v.findViewById(R.id.recent_post_textview);

        return v;
    }
}
