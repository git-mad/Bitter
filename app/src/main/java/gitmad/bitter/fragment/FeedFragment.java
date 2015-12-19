package gitmad.bitter.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import gitmad.bitter.R;
import gitmad.bitter.data.MockPostProvider;
import gitmad.bitter.model.Post;
import gitmad.bitter.ui.PostAdapter;


public class FeedFragment extends Fragment implements AuthorPostDialogFragment.OnPostCreatedListener {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    private MockPostProvider postProvider;

    private OnFragmentInteractionListener mListener;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public FeedFragment() {
    }

    public static FeedFragment newInstance() {
        FeedFragment fragment = new FeedFragment();
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
        View view = inflater.inflate(R.layout.fragment_feed, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.feed_recycler_view);
        layoutManager = new LinearLayoutManager(this.getContext());
        recyclerView.setLayoutManager(layoutManager);
        postProvider = new MockPostProvider(this.getContext());

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

    @Override
    public void onAttach(Context c) {
        super.onAttach(c);
        try {
            mListener = (OnFragmentInteractionListener) c;
        } catch (ClassCastException e) {
            throw new ClassCastException(c.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    private Post[] getMockPosts() {
        return postProvider.getPosts();
    }

    @Override
    public void onPostCreated(Post post) {

        //TODO re-read from provider
        postProvider.addPost(post);
        ((PostAdapter) adapter).add(post);
        recyclerView.swapAdapter(adapter, false);
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(String id);
    }
}
