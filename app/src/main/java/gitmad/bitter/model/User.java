package gitmad.bitter.model;

import gitmad.bitter.data.UserProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Models a user
 */
public class User {
    private String id;
    private String name;
    private String imageId;

    private String userSince;
    private int posts;
    private int salt;
    private int totalVotes;
    private int totalComments;
    private int numEnemies;

    public User(String name, String userId, String imageId) {
        this.id = userId;
        this.name = name;
        this.imageId = imageId;

        // FIXME these should be automatically initialized
        userSince = "03/28/16";
        this.posts = 0;
        this.salt = 0;
        this.totalVotes = 0;
        this.totalComments = 0;
        this.numEnemies = 0;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof User)) {
            return false;
        }

        User other = (User) o;

        return other.id.equals(id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    public String getId() {
        return id;
    }

    public String getImageId() {
        return imageId;
    }

    public String getName() {
        return name;
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

    public void updateUserInfo() {
        // TODO automatically update all variables that must be set internally
    }

    /**
     * Method returns the top 3 categories based on user's posts
     *
     * @return a list of the top 3 categories with 0 being the top category
     */
    public List<String> topCategories(String userId, UserProvider
            userProvider) {
        // FIXME efficency
        // FIXME need to figure out how to organize this data

        List<String> topList = new ArrayList<>(3);
        Map<String, Integer> categoryCount = userProvider
                .getPostCategoryCount(userId);

        //Method will be stable in the case of categories occurring the same
        // number of times
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
            if (categoryCount.get(category) > secondMax && categoryCount.get
                    (category) < max) {
                secondMax = categoryCount.get(category);
                secondCategory = category;
            }
        }

        String thirdCategory = "";
        int thirdMax = 0;
        for (String category : categoryCount.keySet()) {
            if (categoryCount.get(category) > thirdMax && categoryCount.get
                    (category) < secondMax) {
                thirdMax = categoryCount.get(category);
                thirdCategory = category;
            }
        }
        topList.add(firstCategory);
        topList.add(secondCategory);
        topList.add(thirdCategory);
        return topList;
    }
}
