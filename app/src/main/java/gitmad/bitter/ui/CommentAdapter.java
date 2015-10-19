package gitmad.bitter.ui;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import gitmad.bitter.R;
import gitmad.bitter.model.Comment;

/**
 * Created by prabh on 10/19/2015.
 */
public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {

    private Comment[] comments;

    public CommentAdapter(Comment[] comments) {
        this.comments = comments;
    }

    @Override
    public CommentAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.view_comment, viewGroup, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(CommentAdapter.ViewHolder holder, int i) {
        holder.commentText.setText(comments[i].getText());
        holder.userText.setText(comments[i].getUser().getName());
    }

    @Override
    public int getItemCount() {
        return comments.length;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView userText;
        public TextView commentText;

        public ViewHolder(View postLayout) {
            super(postLayout);
            userText = (TextView) postLayout.findViewById(R.id.user_text);
            commentText = (TextView) postLayout.findViewById(R.id.comment_text);
        }
    }
}
