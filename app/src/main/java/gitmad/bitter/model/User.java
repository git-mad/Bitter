package gitmad.bitter.model;

import gitmad.bitter.data.PostProvider;
import gitmad.bitter.data.UserProvider;

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

    public User() {

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

    /**
     * Method returns the top 3 categories based on user's posts
     *
     * @return a list of the top 3 categories with 0 being the top category
     */
    public Post[][] topCategories(String userId, UserProvider userProvider,
                                  PostProvider postProvider) {
        Post[][] topPosts = new Post[2][3];
        Post[] userPosts = postProvider.getPostsByUser(userId);
        Map<String, Integer> categoryCount = userProvider
                .getPostCategoryCount(userId);

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

        //Now getting the top 2 posts from each category
        Post topPost1 = null;
        Post secondPost1 = null;
        if (!firstCategory.equals("")) {
            int k = 0;
            while (k < userPosts.length && !userPosts[k].getCategory().equals
                    (firstCategory)) {
                k++;
            }
            topPost1 = userPosts[k];
            for (int i = 0; i < userPosts.length; i++) {
                if (userPosts[i].getCategory().equals(firstCategory)
                        && userPosts[i].getDownvotes() > topPost1
                        .getDownvotes()) {
                    topPost1 = userPosts[i];
                }
            }
            for (int j = 0; j < userPosts.length; j++) {
                if (userPosts[j].getCategory().equals(firstCategory)
                        && userPosts[j].getDownvotes() < topPost1
                        .getDownvotes()) {
                    if (secondPost1 != null && userPosts[j].getDownvotes() >
                            secondPost1.getDownvotes()) {
                        secondPost1 = userPosts[j];
                    }
                }
            }
        }

        Post topPost2 = null;
        Post secondPost2 = null;
        if (!secondCategory.equals("")) {
            int k = 0;
            while (k < userPosts.length && !userPosts[k].getCategory().equals
                    (secondCategory)) {
                k++;
            }
            topPost2 = userPosts[k];
            for (int i = 0; i < userPosts.length; i++) {
                if (userPosts[i].getCategory().equals(secondCategory)
                        && userPosts[i].getDownvotes() > topPost2
                        .getDownvotes()) {
                    topPost2 = userPosts[i];
                }
            }
            for (int j = 0; j < userPosts.length; j++) {
                if (userPosts[j].getCategory().equals(secondCategory)
                        && userPosts[j].getDownvotes() < topPost2
                        .getDownvotes()) {
                    if (secondPost2 != null && userPosts[j].getDownvotes() >
                            secondPost2.getDownvotes()) {
                        secondPost2 = userPosts[j];
                    }
                }
            }
        }

        Post topPost3 = null;
        Post secondPost3 = null;
        if (!thirdCategory.equals("")) {
            int k = 0;
            while (k < userPosts.length && !userPosts[k].getCategory().equals
                    (thirdCategory)) {
                k++;
            }
            topPost3 = userPosts[k];
            for (int i = 0; i < userPosts.length; i++) {
                if (userPosts[i].getCategory().equals(thirdCategory)
                        && userPosts[i].getDownvotes() > topPost3
                        .getDownvotes()) {
                    topPost3 = userPosts[i];
                }
            }
            for (int j = 0; j < userPosts.length; j++) {
                if (userPosts[j].getCategory().equals(thirdCategory)
                        && userPosts[j].getDownvotes() < topPost3
                        .getDownvotes()) {
                    if (secondPost3 != null && userPosts[j].getDownvotes() >
                            secondPost3.getDownvotes()) {
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

    public void updateUserInfo() {
        // TODO automatically update all variables that must be set internally
    }
}