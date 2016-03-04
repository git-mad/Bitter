package gitmad.bitter.data.firebase;

import gitmad.bitter.data.CategoryProvider;

/**
 * Created by brian on 2/22/16.
 */
public class FirebaseCategoryProvider implements CategoryProvider {

    public static final String FIREBASE_CATEGORIES_URL = "https://bitter-gitmad.firebaseio.com/comments";

    @Override
    public String[] getCategories() {
        return new String[0];
    }
}
