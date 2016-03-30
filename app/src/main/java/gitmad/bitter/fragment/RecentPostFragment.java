package gitmad.bitter.fragment;

import android.os.Bundle;
import gitmad.bitter.model.Post;

import java.util.Comparator;

public class RecentPostFragment extends SortedPostFragment {

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public RecentPostFragment() {
        super(new Comparator<Post>() {
            @Override
            public int compare(Post lhs, Post rhs) {
                return Long.compare(lhs.getTimestamp(), rhs.getTimestamp());
            }
        });
    }

    public static RecentPostFragment newInstance() {
        RecentPostFragment fragment = new RecentPostFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }
}
