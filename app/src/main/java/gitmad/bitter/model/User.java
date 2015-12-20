package gitmad.bitter.model;

import java.util.Collection;

/**
 * Models a user
 */
public class User {
    private String name;
    private String id;

    public User(String pName, String pId) {
        name = pName;
        id = pId;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }
}
