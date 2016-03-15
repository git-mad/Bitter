package gitmad.bitter.ui;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

import gitmad.bitter.R;
import gitmad.bitter.data.PostProvider;
import gitmad.bitter.model.Post;
import gitmad.bitter.model.User;

/**
 * Adapter for displaying cardviews that present posts in a RecyclerView.
 */
public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {

    private List<Post> posts;
    private Map<Post, User> postAuthors;
    private PostProvider provider;
    private int oddEven = 0;

    private FeedInteractionListener listener;

    public PostAdapter(List<Post> posts, Map<Post, User> postAuthors, FeedInteractionListener pListener, PostProvider provider) {
        this.posts = posts;
        this.postAuthors = postAuthors;
        listener = pListener;
        this.provider = provider;
    }

    public void add(Post p) {
        posts.add(0, p);
        this.notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.view_post, viewGroup, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int i) {
        viewHolder.postText.setText(posts.get(i).getText());
        viewHolder.userText.setText(postAuthors.get(posts.get(i)).getName());
        viewHolder.downvoteText.setText(Integer.toString(posts.get(i).getDownvotes()));
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onPostClicked(posts.get(i), i);
            }
        });


        viewHolder.downvoteImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onDownvoteClicked(posts.get(i), i);
                Post p  = provider.getPost(posts.get(i).getId());
                posts.set(i, p);
                viewHolder.downvoteText.setText(Integer.toString(posts.get(i).getDownvotes()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView userText;
        public TextView postText;
        public TextView timeText;
        public TextView repliesText;
        public ImageView downvoteImage;
        public TextView downvoteText;

        public ViewHolder(View postLayout) {
            super(postLayout);
            userText = (TextView) postLayout.findViewById(R.id.user_text);
            postText = (TextView) postLayout.findViewById(R.id.post_text);
            timeText = (TextView) postLayout.findViewById(R.id.time_posted);
            repliesText = (TextView) postLayout.findViewById(R.id.post_replies);

            downvoteImage = (ImageView) postLayout.findViewById(R.id.downvote_button);
            downvoteText = (TextView) postLayout.findViewById(R.id.downvote_counter);
        }
    }

    public interface FeedInteractionListener {
        void onPostClicked(Post p, int index);
        void onDownvoteClicked(Post p, int index);
    }
}
