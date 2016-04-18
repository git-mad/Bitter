package gitmad.bitter;

import android.app.Application;
import com.firebase.client.Firebase;

/**
 * Application singleton that initializes the Firebase backend,
 * and authenticates. We use anonymous authentication, which
 * only needs to be done once, when the application is first started.
 * After that, any Firebase#getAuth() should always be defined automatically.
 */
public class BitterApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        // Necessary for firebase to work in all Activities //
        Firebase.setAndroidContext(this);

        // store data locally until it can be pushed //
//            Firebase.getDefaultConfig().setPersistenceEnabled(true);

    }
}
