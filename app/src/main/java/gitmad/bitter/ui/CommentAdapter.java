package gitmad.bitter.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import gitmad.bitter.R;
import gitmad.bitter.model.Comment;
import gitmad.bitter.model.User;

import java.util.Map;

/**
 * Created by prabh on 10/19/2015.
 */
public class CommentAdapter extends ArrayAdapter<Comment> {

    private Comment[] comments;
    private Map<Comment, User> commentAuthors;

    public CommentAdapter(Context context, Comment[] comments, Map<Comment,
            User> commentAuthors) {
        super(context, 0, comments); // TODO is the zero here correct?

        this.comments = comments;
        this.commentAuthors = commentAuthors;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        Comment comment = comments[position];

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout
                    .view_comment, parent, false);
        }

        TextView userText = (TextView) convertView.findViewById(R.id.user_text);
        TextView commentText = (TextView) convertView.findViewById(R.id
                .comment_text);

        userText.setText(commentAuthors.get(comment).getName());
        commentText.setText(comment.getText());

        return convertView;
    }
}
