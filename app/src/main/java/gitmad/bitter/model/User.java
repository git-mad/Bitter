package gitmad.bitter.model;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

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
    private Date userSince;
    private List<String> enemies;
    public User(String name, String userId, String imageId) {
        userProvider = new FirebaseUserProvider();

        this.id = userId;
        this.name = name;
        this.imageId = imageId;

        posts = 0;
        salt = 0;
        totalVotes = 0;
        totalComments = 0;
        userSince = new Date();
        enemies = new ArrayList<>();
    }

    public User(String name, String userId) {
        // FIXME set imageId to the id for the defualt picture
        this(name, userId, "default-image");
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

    public User removeEnemy(String userId, UserProvider userProvider) {
        if (enemies.remove(userId)) {
            User user = userProvider.getUser(userId);
            return user;
        } else {
            return null;
        }
    }

    /**
     * Method returns the top 3 categories based on user's posts
     * @return a list of the top 3 categories with 0 being the top category
     * */
    public List<String> topCategories(String userId, UserProvider userProvider) {
        List<String> topList = new ArrayList<>(3);
        Map<String, Integer> categoryCount = userProvider.getPostCategoryCount(userId);
        //Method will be stable in the case of categories occurring the same number of times
        String firstCategory = "";
        int max = 0;
        for (String category : categoryCount.keySet()) {
            if (categoryCount.get(category) > max) {
                max = categoryCount.get(category);
                firstCategory = category;
            }
        }
        String secondCategory = "";
        int secondMax = 0;
        for (String category : categoryCount.keySet()) {
            if (categoryCount.get(category) > secondMax && categoryCount.get(category) < max) {
                secondMax = categoryCount.get(category);
                secondCategory = category;
            }
        }
        String thirdCategory = "";
        int thirdMax = 0;
        for (String category : categoryCount.keySet()) {
            if (categoryCount.get(category) > thirdMax && categoryCount.get(category) < secondMax) {
                thirdMax = categoryCount.get(category);
                thirdCategory = category;
            }
        }
        topList.add(firstCategory);
        topList.add(secondCategory);
        topList.add(thirdCategory);
        return topList;
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
