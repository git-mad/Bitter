package gitmad.bitter.fragment.sortedpost;

import android.os.Bundle;
import gitmad.bitter.fragment.SortedPostFragment;
import gitmad.bitter.model.Post;

import java.util.Comparator;

/**
 * Sorted Post Fragment that sorts posts by the time the post was created.
 */
public class RecentPostFragment extends SortedPostFragment {
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
