package gitmad.bitter.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import gitmad.bitter.R;
import gitmad.bitter.data.mock.MockPostProvider;
import gitmad.bitter.model.Post;
import gitmad.bitter.ui.PostAdapter;

public class TopPostFragment extends Fragment {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    private MockPostProvider postProvider;

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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_top_posts, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.top_posts_recycler_view);
        layoutManager = new LinearLayoutManager(this.getActivity());
        recyclerView.setLayoutManager(layoutManager);

        postProvider = new MockPostProvider(this.getActivity());

        // specify an adapter (see also next example)

        Post[] posts = getMockPosts();

        ArrayList<Post> postList = new ArrayList<>(posts.length);

        for (Post p : posts) {
            postList.add(p);
        }

        adapter = new PostAdapter(postList);
        recyclerView.setAdapter(adapter);

        return view;
    }

    private Post[] getMockPosts() {
        return postProvider.getPosts(Integer.MAX_VALUE);
    }
}
