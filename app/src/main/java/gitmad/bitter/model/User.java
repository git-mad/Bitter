package gitmad.bitter.model;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import gitmad.bitter.data.UserProvider;
import gitmad.bitter.data.firebase.FirebaseUserProvider;

/**
 * Models a user
 */
public class User {
    private final SimpleDateFormat formater = new SimpleDateFormat("MMMM d, yyyy");
    private UserProvider userProvider;

    private String id;
    private String name;
    private String imageId;
    private int posts;
    private int salt;
    private int totalVotes;
    private int totalComments;
    private String userSince;
     private int numEnemies;
    public User(String name, String userId, String imageId, int posts, int salt, int totalVotes, int totalComments, String userSince, int numEnemies) {
        userProvider = new FirebaseUserProvider();

        this.id = userId;
        this.name = name;
        this.imageId = imageId;

        this.posts = posts;
        this.salt = salt;
        this.totalVotes = totalVotes;
        this.totalComments = totalComments;
        this.userSince = userSince;
        this.numEnemies = numEnemies;


    }

    public User(){

    }

    public User(String name, String userId) {
        // FIXME set imageId to the id for the defualt picture
        this.name = name;
        this.id = userId;
    }

    public String getImageId() {
        return imageId;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public void setTotalPosts(int numPosts) {
        posts = numPosts;
    }

    public int getPosts() {
        return posts;
    }

    public int getSalt() {
        return salt;
    }

    public void setSalt(int numSalt) {
        salt = numSalt;
    }

    public int getTotalVotes() {
        return totalVotes;
    }

    public void setTotalVotes(int numVotes) {
        totalVotes = numVotes;
    }

    public int getTotalComments() {
        return totalComments;
    }

    public String getUserSince() {
        return userSince;
        //String formatedDate = formater.format(userSince);
        //return formatedDate;
    }

/*    public int getNumEnemies() {
        return enemies.size();
    }

    public boolean addEnemy(String userId) {
        if (!enemies.contains(userId)) {
            enemies.add(userId);
            return true;
        } else {
            return false;
        }
    }

    public User removeEnemy(String userId) {
        if (enemies.remove(userId)) {
            User user = userProvider.getUser(userId);
            return user;
        } else {
            return null;
        }
    }
*/
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

    public int getNumEnemies() {
        return numEnemies;
    }
}
