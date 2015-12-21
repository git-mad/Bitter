package gitmad.bitter.fragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import gitmad.bitter.R;
import gitmad.bitter.data.MockPostProvider;
import gitmad.bitter.data.PostProvider;
import gitmad.bitter.model.Comment;
import gitmad.bitter.model.Post;
import gitmad.bitter.model.User;
import gitmad.bitter.ui.CommentAdapter;

public class ViewPostFragment extends Fragment {

    private static final String KEY_POST_ID = "postIdKey";
    private int postId;
    private Post post;
    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ViewPostFragment() {
    }

    public static ViewPostFragment newInstance(int postId) {
        Bundle args = new Bundle();
        args.putInt(KEY_POST_ID, postId);
        ViewPostFragment fragment = new ViewPostFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null && getArguments().containsKey(KEY_POST_ID)) {
            int postId = getArguments().getInt(KEY_POST_ID);
            post = getPostFromMockProvider(postId);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view_post, container, false);

        TextView postBodyTextView = (TextView) view.findViewById(R.id.postContent);
        TextView userTextView = (TextView) view.findViewById(R.id.posterUsername);

        // FIXME temp profile pic, get pic from variable instead of hardcoded id
        ImageView pic = (ImageView) view.findViewById(R.id.posterPic);
        Bitmap bm = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
        Bitmap conv_bm = UserProfileFragment.getRoundedRectBitmap(bm, 500);
        pic.setImageBitmap(conv_bm);

        if (post != null) {
            postBodyTextView.setText(post.getText());
        }
        // TODO
        userTextView.setText("Username To Set");

        Comment[] comments = getMockComments();

        CommentAdapter adapter = new CommentAdapter(this.getActivity(), comments);
        ListView listView = (ListView) view.findViewById(R.id.comments_list_view);
        listView.setAdapter(adapter);

        final TextInputLayout commentWrapper = (TextInputLayout) view.findViewById(R.id.comment_text_wrapper);
        commentWrapper.setHint("Bitch about it!");

        return view;
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

    private Post getPostFromMockProvider(int postId) {
        PostProvider postProvider = new MockPostProvider(getActivity());
        return postProvider.getPost(postId);
    }
}
