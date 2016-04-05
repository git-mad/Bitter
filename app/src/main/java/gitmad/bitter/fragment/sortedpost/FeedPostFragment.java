package gitmad.bitter.fragment.sortedpost;

import android.os.Bundle;
import gitmad.bitter.fragment.SortedPostFragment;
import gitmad.bitter.model.Post;

import java.util.Comparator;

/**
 * Sorted Post Fragment that sorts posts by ____ algorithm.
 */
public class FeedPostFragment extends SortedPostFragment {
    public FeedPostFragment() {
        super(new Comparator<Post>() {
            @Override
            public int compare(Post lhs, Post rhs) {
                // TODO feed post algorithm implementation
                return lhs.getDownvotes() - rhs.getDownvotes();
            }
        });
    }

    public static FavoritePostFragment newInstance() {
        FavoritePostFragment fragment = new FavoritePostFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }
}
