package gitmad.bitter.model;


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

    public User() {
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
}
