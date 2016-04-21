package gitmad.bitter.data.firebase;

import com.firebase.client.Firebase;
import gitmad.bitter.data.CategoryProvider;

import java.util.Map;

/**
 * Created by brian on 2/22/16.
 */
public class FirebaseCategoryProvider implements CategoryProvider {

    public static final String FIREBASE_CATEGORIES_URL =
            "https://bitter-gitmad.firebaseio.com/postCategories";

    @Override
    public String[] getCategories() {
        Firebase fb = new Firebase(FIREBASE_CATEGORIES_URL);

        FirebaseSyncRequester<Map> categoriesRequester = new
                FirebaseSyncRequester<>(fb, Map.class);

        Map<String, Boolean> categoryMap = categoriesRequester.get();

        return categoryMap.keySet().toArray(new String[categoryMap.size()]);
    }
}
