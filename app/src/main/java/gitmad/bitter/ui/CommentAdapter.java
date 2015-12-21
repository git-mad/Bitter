package gitmad.bitter.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import gitmad.bitter.R;
import gitmad.bitter.model.Comment;

/**
 * Created by prabh on 10/19/2015.
 */
public class CommentAdapter extends ArrayAdapter<Comment> {

    private Comment[] comments;
    private Context context;

    public CommentAdapter(Context context, Comment[] comments) {
        super(context, 0, comments);

        this.context = context;
        this.comments = comments;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        Comment comment = comments[position];

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.view_comment, parent, false);
        }

        TextView userText = (TextView) convertView.findViewById(R.id.user_text);
        TextView commentText = (TextView) convertView.findViewById(R.id.comment_text);

        userText.setText(comment.getUser().getName());
        commentText.setText(comment.getText());

        return convertView;
    }
}
