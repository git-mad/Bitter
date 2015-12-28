package gitmad.bitter.model;

/**
 * Encapsulates info about a comment on a post
 */
public class Comment {
    private String id;
    private String postId;
    private String authorId;
    private String text;
    private long timestamp;
    private int downvotes;

    public Comment(String id, String postId, String authorId, String text, long timestamp, int downvotes) {
        this.id = id;
        this.postId = postId;
        this.authorId = authorId;
        this.text = text;
        this.timestamp = timestamp;
        this.downvotes = downvotes;
    }

    public String getId() {
        return id;
    }

    public String getPostId() {
        return postId;
    }

    public String getAuthorId() {
        return authorId;

    }

    public String getText() {
        return text;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public int getDownvotes() {
        return downvotes;
    }

    @Override
    public int hashCode() {
        return getId().hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Comment)) {
            return false;
        }

        Comment other = (Comment) o;

        return other.getId().equals(getId());
    }
}
