package gitmad.bitter.model;

/**
 * Encapsulates info about a comment on a post
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
