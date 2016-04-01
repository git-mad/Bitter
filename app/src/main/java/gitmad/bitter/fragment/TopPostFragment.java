package gitmad.bitter.fragment;

import android.os.Bundle;
import gitmad.bitter.model.Post;

import java.util.Comparator;

public class TopPostFragment extends SortedPostFragment {
    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public TopPostFragment() {
        super(new Comparator<Post>() {
            @Override
            public int compare(Post lhs, Post rhs) {
                return lhs.getDownvotes() - rhs.getDownvotes();
            }
        });
    }

    public static SortedPostFragment newInstance() {
        TopPostFragment fragment = new TopPostFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }
}
