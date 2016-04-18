package gitmad.bitter.model;

/**
 * Created by brian on 9/21/15.
 */
public class Post {
    private String id;
    private String text;
    private String category;
    private long timestamp;
    private int downvotes;
    private String authorId;

    public Post(String id, String text, long timestamp, int downvotes, String
            authorId, String category) {
        this.id = id;
        this.text = text;
        this.timestamp = timestamp;
        this.downvotes = downvotes;
        this.authorId = authorId;
        this.category = category;
    }

    public Post() {
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Post)) {
            return false;
        }

        Post other = (Post) o;
        return other.getId().equals(getId());
    }

    @Override
    public int hashCode() {
        return getId().hashCode();
    }

    @Override
    public String toString() {
        return "id:" + getId();
    }

    public String getAuthorId() {
        return authorId;
    }

    public String getCategory() {
        return category;
    }

    public int getDownvotes() {
        return downvotes;
    }

    public void downvotePost() {
        downvotes++;
    }

    public String getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public long getTimestamp() {
        return timestamp;
    }
}
