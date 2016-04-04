package gitmad.bitter.model;


/**
 * Models a user
 */
public class User {
    private String id;
    private String imageId;
    private String name;
    private int numEnemies;
    private int posts;
    private int salt;
    private int totalComments;
    private int totalVotes;
    private String userSince;

    public User(String name, String imageId, String id, int numEnemies, int posts, int salt, int totalComments, int totalVotes, String userSince) {
        this.name = name;
        this.imageId = imageId;
        this.id = id;
        this.numEnemies = numEnemies;
        this.posts = posts;
        this.salt = salt;
        this.totalComments = totalComments;
        this.totalVotes = totalVotes;
        this.userSince = userSince;
    }

    public User() {
    }

    public User(String temp, String userUid) {
        this.name = temp;
        this.id = userUid;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }


    @Override
    public int hashCode() {
        return getId().hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof User)) {
            return false;
        }

        User other = (User) o;

        return other.getId().equals(getId());
    }

    public String getImageId() {
        return imageId;
    }

    public int getNumEnemies() {
        return numEnemies;
    }

    public int getPosts() {
        return posts;
    }

    public int getSalt() {
        return salt;
    }

    public int getTotalComments() {
        return totalComments;
    }

    public int getTotalVotes() {
        return totalVotes;
    }

    public String getUserSince() {
        return userSince;
    }
}
