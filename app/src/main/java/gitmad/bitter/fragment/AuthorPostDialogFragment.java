package gitmad.bitter.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import gitmad.bitter.R;

public class AuthorPostDialogFragment extends DialogFragment {

    public static final String AUTHOR_POST_DIALOG_FRAG_TAG = "APDFT";

    private OnPostCreatedListener mListener;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public AuthorPostDialogFragment() {
    }

    public static AuthorPostDialogFragment newInstance() {
        AuthorPostDialogFragment fragment = new AuthorPostDialogFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnPostCreatedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnPostCreatedListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_author_post_dialog,
                container, false);

        final EditText bitchEditText = (EditText) root.findViewById(R.id
                .postTextEditText);

        root.findViewById(R.id.bitch_button).setOnClickListener(new View
                .OnClickListener() {
            @Override
            public void onClick(View v) {
                String postText = bitchEditText.getText().toString();

                mListener.onPostCreated(postText);
                AuthorPostDialogFragment.this.dismiss();
            }
        });

        return root;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating
     * .html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnPostCreatedListener {
        void onPostCreated(String postText);
    }

}
