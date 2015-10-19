package gitmad.bitter.ui;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

import gitmad.bitter.R;
import gitmad.bitter.activity.ViewPostActivity;
import gitmad.bitter.model.Post;

/**
 * Created by brian on 9/28/15.
 */
public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {

    private Post[] posts;

    public PostAdapter(Post[] posts) {
        this.posts = posts;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.view_post, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int i) {
        viewHolder.postText.setText(posts[i].getText());
        viewHolder.userText.setText(posts[i].getUser().getName());
        viewHolder.downvoteText.setText(Integer.toString(posts[i].getDownvotes()));
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ViewPostActivity.class);
                intent.putExtra("postContent", posts[i].getText());
                intent.putExtra("userName", posts[i].getUser().getName());
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return posts.length;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView userText;
        public TextView postText;
        public TextView timeText;
        public TextView repliesText;
        public TextView downvoteText;

        public ViewHolder(View postLayout) {
            super(postLayout);
            userText = (TextView) postLayout.findViewById(R.id.user_text);
            postText = (TextView) postLayout.findViewById(R.id.post_text);
            timeText = (TextView) postLayout.findViewById(R.id.time_posted);
            repliesText = (TextView) postLayout.findViewById(R.id.post_replies);
            downvoteText = (TextView) postLayout.findViewById(R.id.downvote_button);
        }
    }
}
