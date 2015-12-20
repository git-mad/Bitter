package gitmad.bitter.model;

/**
 * Created by brian on 12/18/15.
 */
public class Comment {
    private int id;
    private int postId;
    private String authorId;
    private String text;

    public Comment(int id, int postId, String authorId, String text) {
        this.id = id;
        this.postId = postId;
        this.authorId = authorId;
        this.text = text;
    }

    public int getId() {
        return id;
    }

    public int getPostId() {
        return postId;
    }

    public String getAuthorId() {
        return authorId;
    }

    public String getText() {
        return text;
    }
}
