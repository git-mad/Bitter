package gitmad.bitter.fragment.sortedpost;

import android.os.Bundle;
import gitmad.bitter.fragment.SortedPostFragment;
import gitmad.bitter.model.Post;

import java.util.Comparator;

/**
 * Sorted Post Fragment that sorts posts by the number of downvotes the post
 * has.
 */
public class TopPostFragment extends SortedPostFragment {
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
