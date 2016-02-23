package gitmad.bitter;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.util.concurrent.CountDownLatch;

import gitmad.bitter.data.firebase.auth.FirebaseAuthManager;
import gitmad.bitter.model.User;

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
