package gitmad.bitter.model;


import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import gitmad.bitter.data.UserProvider;
import gitmad.bitter.data.firebase.FirebaseUserProvider;

/**
 * Models a user
 */
public class User {
    private String name;
    private String id;
    private int numPosts;
    private int numEnemies;
    private List<String> enemies;
    private int numDownVotes;
    private UserProvider database;

    public User(String pName, String pId) {
        name = pName;
        id = pId;
        numPosts = 0;
        numDownVotes = 0;
        enemies = new ArrayList<String>();
        numEnemies = enemies.size();
        database = new FirebaseUserProvider();
    }

    public User() {
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public int getNumEnemies() {return numEnemies;}

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
             User user = database.getUser(userId);
             return user;
         } else {
             throw new NoSuchElementException("No Such Enemy Exists");
         }
    }

    public int getNumPosts() { return numPosts; }

    public int getNumDownVotes() {return numDownVotes;}

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
