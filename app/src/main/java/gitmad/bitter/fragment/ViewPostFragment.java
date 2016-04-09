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
import gitmad.bitter.data.PostProvider;
import gitmad.bitter.data.UserProvider;
import gitmad.bitter.data.mock.MockCommentProvider;
import gitmad.bitter.data.mock.MockPostProvider;
import gitmad.bitter.data.mock.MockUserProvider;
import gitmad.bitter.model.Comment;
import gitmad.bitter.model.Post;
import gitmad.bitter.model.User;
import gitmad.bitter.ui.CommentAdapter;

import java.util.Map;

public class ViewPostFragment extends Fragment {

    private static final String KEY_POST_ID = "postIdKey";
    private Post post;

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
            String postId = getArguments().getString(KEY_POST_ID);
            post = getPostFromMockProvider(postId);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view_post, container,
                false);

        TextView postBodyTextView = (TextView) view.findViewById(R.id
                .postContent);
        TextView userTextView = (TextView) view.findViewById(R.id
                .posterUsername);

        postBodyTextView.setText(post.getText());
        userTextView.setText(getUserFromMockProvider().getName());

        // FIXME temp profile pic, get pic from variable instead of hardcoded id
        ImageView pic = (ImageView) view.findViewById(R.id.posterPic);
        Bitmap bm = BitmapFactory.decodeResource(getResources(), R.mipmap
                .ic_launcher);
        Bitmap conv_bm = UserProfileFragment.getRoundedRectBitmap(bm, 500);
        pic.setImageBitmap(conv_bm);


        Comment[] comments = new MockCommentProvider(getContext())
                .getCommentsOnPost(post.getId());

        Map<Comment, User> authorsMap = new MockUserProvider()
                .getAuthorsOfComments(comments);

        CommentAdapter adapter = new CommentAdapter(this.getActivity(),
                comments, authorsMap);
        ListView listView = (ListView) view.findViewById(R.id
                .comments_list_view);
        listView.setAdapter(adapter);

        final TextInputLayout commentWrapper = (TextInputLayout) view
                .findViewById(R.id.comment_text_wrapper);
        commentWrapper.setHint("Bitch about it!");

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
}
