package gitmad.bitter.model;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import gitmad.bitter.data.PostProvider;
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
    public Post[][] topCategories(String userId, UserProvider userProvider, PostProvider postProvider) {
        Post[][] topPosts = new Post[2][3];
        Post[] userPosts = postProvider.getPostsByUser(userId);
        Map<String, Integer> categoryCount = userProvider.getPostCategoryCount(userId);

        //Finds the top 3 categories based on a User's posts
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

        //Now getting the top 2 posts from each category
        Post topPost1 = null;
        Post secondPost1 = null;
        if (!firstCategory.equals("")) {
            int k = 0;
            while (k < userPosts.length && !userPosts[k].getCategory().equals(firstCategory)){
                k++;
            }
            topPost1 = userPosts[k];
            for (int i = 0; i < userPosts.length; i++) {
                if (userPosts[i].getCategory().equals(firstCategory)
                        && userPosts[i].getDownvotes() > topPost1.getDownvotes()){
                    topPost1 = userPosts[i];
                }
            }
            for (int j = 0; j < userPosts.length; j++) {
                if (userPosts[j].getCategory().equals(firstCategory)
                        && userPosts[j].getDownvotes() < topPost1.getDownvotes()){
                    if (secondPost1 != null && userPosts[j].getDownvotes() > secondPost1.getDownvotes()) {
                        secondPost1 = userPosts[j];
                    }
                }
            }
        }

        Post topPost2 = null;
        Post secondPost2 = null;
        if (!secondCategory.equals("")) {
            int k = 0;
            while (k < userPosts.length && !userPosts[k].getCategory().equals(secondCategory)){
                k++;
            }
            topPost2 = userPosts[k];
            for (int i = 0; i < userPosts.length; i++) {
                if (userPosts[i].getCategory().equals(secondCategory)
                        && userPosts[i].getDownvotes() > topPost2.getDownvotes()){
                    topPost2 = userPosts[i];
                }
            }
            for (int j = 0; j < userPosts.length; j++) {
                if (userPosts[j].getCategory().equals(secondCategory)
                        && userPosts[j].getDownvotes() < topPost2.getDownvotes()){
                    if (secondPost2 != null && userPosts[j].getDownvotes() > secondPost2.getDownvotes()) {
                        secondPost2 = userPosts[j];
                    }
                }
            }
        }

        Post topPost3 = null;
        Post secondPost3 = null;
        if (!thirdCategory.equals("")) {
            int k = 0;
            while (k < userPosts.length && !userPosts[k].getCategory().equals(thirdCategory)){
                k++;
            }
            topPost3 = userPosts[k];
            for (int i = 0; i < userPosts.length; i++) {
                if (userPosts[i].getCategory().equals(thirdCategory)
                        && userPosts[i].getDownvotes() > topPost3.getDownvotes()){
                    topPost3 = userPosts[i];
                }
            }
            for (int j = 0; j < userPosts.length; j++) {
                if (userPosts[j].getCategory().equals(thirdCategory)
                        && userPosts[j].getDownvotes() < topPost3.getDownvotes()){
                    if (secondPost3 != null && userPosts[j].getDownvotes() > secondPost3.getDownvotes()) {
                        secondPost3 = userPosts[j];
                    }
                }
            }
        }

        //Finally, fills the 2-D array to return
        int r = 0;
        int c = 0;
        if (topPost1 != null) {
            topPosts[r][c] = topPost1;
            r++;
        }
        if (secondPost1 != null) {
            topPosts[r][c] = secondPost1;
            r--;
            c++;
        }
        if (topPost2 != null) {
            topPosts[r][c] = topPost2;
            r++;
        }
        if (secondPost2 != null) {
            topPosts[r][c] = secondPost2;
            r--;
            c++;
        }
        if (topPost3 != null) {
            topPosts[r][c] = topPost3;
            r++;
        }
        if (secondPost3 != null) {
            topPosts[r][c] = secondPost3;
        }

        return topPosts;
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
