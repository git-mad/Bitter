package gitmad.bitter.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import gitmad.bitter.R;
import gitmad.bitter.model.Comment;
import gitmad.bitter.model.User;
import gitmad.bitter.ui.CommentAdapter;

public class ViewPostFragment extends Fragment {
    private OnFragmentInteractionListener mListener;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ViewPostFragment() {
    }

    public static ViewPostFragment newInstance() {
        ViewPostFragment fragment = new ViewPostFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.fragment_view_post);
//
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view_post, container, false);

        //FIXME intents don't work for fragments
//        Intent intent = getIntent();
//        String postContent = intent.getStringExtra("postContent");
//        String userName = intent.getStringExtra("userName");
        String postContent = "FIX ME";
        String userName = "FIX ME";

        TextView postBody = (TextView) view.findViewById(R.id.postContent);
        TextView user = (TextView) view.findViewById(R.id.posterUsername);

        postBody.setText(postContent);
        user.setText(userName);

        Comment[] comments = getMockComments();

        CommentAdapter adapter = new CommentAdapter(this.getActivity(), comments);
        ListView listView = (ListView) view.findViewById(R.id.comments_list_view);
        listView.setAdapter(adapter);

        final TextInputLayout commentWrapper = (TextInputLayout) view.findViewById(R.id.comment_text_wrapper);
        commentWrapper.setHint("Bitch about it!");

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

    private Comment[] getMockComments() {
        String[] commentsText = getResources().getStringArray(R.array.mock_comments);
        User user = new User();
        user.setName("NOTgBurdell");
        Comment[] comments = new Comment[commentsText.length];
        for (int i = 0; i < commentsText.length; i++) {
            comments[i] = new Comment();
            comments[i].setText(commentsText[i]);
            comments[i].setUser(user);
        }
        return comments;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
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
