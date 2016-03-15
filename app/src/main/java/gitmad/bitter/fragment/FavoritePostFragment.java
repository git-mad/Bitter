package gitmad.bitter.fragment;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import java.util.Comparator;

import gitmad.bitter.data.mock.MockPostProvider;
import gitmad.bitter.model.Post;

public class FavoritePostFragment extends SortedPostFragment {
    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public FavoritePostFragment() {
        super(new Comparator<Post>() {
            @Override
            public int compare(Post lhs, Post rhs) {
                // TODO favorite post implementation
                return Long.compare(lhs.getTimestamp(), rhs.getTimestamp());
            }
        }, "FavoritePostFragment");
    }

    public static FavoritePostFragment newInstance() {
        FavoritePostFragment fragment = new FavoritePostFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }
}