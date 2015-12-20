package gitmad.bitter.fragment;

import android.app.Activity;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.Date;

import gitmad.bitter.R;
import gitmad.bitter.data.MockPostProvider;
import gitmad.bitter.model.Post;
import gitmad.bitter.model.User;

public class AuthorPostDialogFragment extends DialogFragment {

    private OnPostCreatedListener mListener;

    public AuthorPostDialogFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_author_post_dialog, container, false);

        final EditText bitchEditText = (EditText) root.findViewById(R.id.postTextEditText);

        root.findViewById(R.id.bitch_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String postText = bitchEditText.getText().toString();

                Post post = new MockPostProvider(getContext()).addPost(postText);


                User temp = new User();

                //temporary//
                temp.setName("me");
                ArrayList<Post> posts = new ArrayList<>();
                posts.add(post);

                mListener.onPostCreated(post);
                AuthorPostDialogFragment.this.dismiss();
            }
        });

        return root;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnPostCreatedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
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
    public interface OnPostCreatedListener {
        void onPostCreated(Post post);
    }

}
