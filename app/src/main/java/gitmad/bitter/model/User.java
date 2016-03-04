package gitmad.bitter.model;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

import gitmad.bitter.data.UserProvider;
import gitmad.bitter.data.firebase.FirebaseUserProvider;

/**
 * Models a user
 */
public class User {
    private UserProvider userProvider;

    private String name;
    private String id;

    private int posts;
    private int salt;
    private int totalVotes;
    private int totalComments;
    private Date userSince;
    private List<String> enemies;

    public User(String pName, String pId) {
        userProvider = new FirebaseUserProvider();

        name = pName;
        id = pId;

        posts = 0;
        salt = 0;
        totalVotes = 0;
        totalComments = 0;
        userSince = new Date();
        enemies = new ArrayList<>();
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
        SimpleDateFormat formater = new SimpleDateFormat("MMMM d, yyyy");
        String formatedDate = formater.format(userSince);
        return formatedDate;
    }

    public int getNumEnemies() {
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
            throw new NoSuchElementException("No Such Enemy Exists");
        }
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
}
