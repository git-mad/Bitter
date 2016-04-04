package gitmad.bitter.fragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Map;

import gitmad.bitter.R;
import gitmad.bitter.data.PostProvider;
import gitmad.bitter.data.UserProvider;
import gitmad.bitter.data.firebase.FirebaseCommentProvider;
import gitmad.bitter.data.firebase.FirebasePostProvider;
import gitmad.bitter.data.firebase.FirebaseUserProvider;
import gitmad.bitter.data.firebase.auth.FirebaseAuthManager;
import gitmad.bitter.data.mock.MockPostProvider;
import gitmad.bitter.data.mock.MockUserProvider;
import gitmad.bitter.model.Comment;
import gitmad.bitter.model.Post;
import gitmad.bitter.model.User;
import gitmad.bitter.ui.CommentAdapter;

public class ViewPostFragment extends Fragment {

    private static final String KEY_POST_ID = "postIdKey";
    private ImageButton imageButton;
    private boolean clicked = false;
    private Post post;
    private String postId;
    private View.OnClickListener imgButtonHandler = new View.OnClickListener() {
        public void onClick(View v) {
            if (!clicked) {
                imageButton.setBackgroundResource(R.drawable.arrow_down_float_downvote);
            } else {
                imageButton.setBackgroundResource(R.drawable.arrow_down_float);
            }

            clicked = !clicked;
            //implement some way to store downvote
        }
    };
    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ViewPostFragment() {
    }

    public static ViewPostFragment newInstance(String postId) {
        Bundle args = new Bundle();
        args.putString(KEY_POST_ID, postId);
        ViewPostFragment fragment = new ViewPostFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null && getArguments().containsKey(KEY_POST_ID)) {
            postId = getArguments().getString(KEY_POST_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view_post, container, false);

        getPostFirebaseTask postFirebaseTask = new getPostFirebaseTask(view);
        postFirebaseTask.execute(postId);

        FirebaseAuthManager authManager = new FirebaseAuthManager(getActivity());
        getCommentFirebaseTask asyncTask = new getCommentFirebaseTask(view);
        asyncTask.execute(postId);

        return view;
    }

    private Post getPostFromMockProvider(String postId) {
        PostProvider postProvider = new MockPostProvider(getActivity());
        return postProvider.getPost(postId);
    }

    private User getUserFromMockProvider() {
        UserProvider mockUserProvider = new MockUserProvider();
        return mockUserProvider.getAuthorOfPost(post);
    }

    private class getPostFirebaseTask extends AsyncTask<String, String, Post> {
        private View view;
        private Post post;

        public getPostFirebaseTask(View view) {
            this.view = view;
        }

        protected Post doInBackground(String... params) {
            FirebasePostProvider firebasePostProvider = new FirebasePostProvider();
            post = firebasePostProvider.getPost(params[0]);
            return post;
        }

        protected void onPostExecute(Post post) {
            imageButton = (ImageButton) view.findViewById(R.id.imageButton);
            imageButton.setOnClickListener(imgButtonHandler);
            TextView postBodyTextView = (TextView) view.findViewById(R.id.postContent);
            TextView userTextView = (TextView) view.findViewById(R.id.posterUsername);

            postBodyTextView.setText(post.getText());
            userTextView.setText(getUserFromMockProvider().getName());

            // FIXME temp profile pic, get pic from variable instead of hardcoded id
            ImageView pic = (ImageView) view.findViewById(R.id.posterPic);
            Bitmap bm = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
            Bitmap conv_bm = UserProfileFragment.getRoundedRectBitmap(bm, 500);
            pic.setImageBitmap(conv_bm);

            final TextInputLayout commentWrapper = (TextInputLayout) view.findViewById(R.id.comment_text_wrapper);
            commentWrapper.setHint("Bitch about it!");

        }
    }

    private class getCommentFirebaseTask extends AsyncTask<String, String, Comment[]> {
        private View view;
        private Comment[] comments;
        private Map<Comment, User> authorsMap;

        public getCommentFirebaseTask(View view) {
            this.view = view;
        }

        protected Comment[] doInBackground(String... params) {
            FirebaseCommentProvider firebaseCommentProvider = new FirebaseCommentProvider();
            comments = firebaseCommentProvider.getCommentsOnPost(params[0]);

            FirebaseUserProvider firebaseUserProvider = new FirebaseUserProvider();
            authorsMap = firebaseUserProvider.getAuthorsOfComments(comments);

            return comments;
        }

        protected void onPostExecute(Comment[] comments) {
            CommentAdapter adapter = new CommentAdapter(view.getContext(), comments, authorsMap);
            ListView listView = (ListView) view.findViewById(R.id.comments_list_view);
            listView.setAdapter(adapter);

        }
    }

}
