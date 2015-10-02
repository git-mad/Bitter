package gitmad.bitter.ui;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import gitmad.bitter.R;
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
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        viewHolder.postText.setText(posts[i].getText());
        viewHolder.userText.setText(posts[i].getUser().getName());
    }

    @Override
    public int getItemCount() {
        return posts.length;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView userText;
        public TextView postText;

        public ViewHolder(View postLayout) {
            super(postLayout);
            userText = (TextView) postLayout.findViewById(R.id.user_text);
            postText = (TextView) postLayout.findViewById(R.id.post_text);
        }
    }
}
