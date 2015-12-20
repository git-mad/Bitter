package gitmad.bitter;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

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
        Firebase.setAndroidContext(this); // Necessary for firebase to work in all Activities

        Firebase fbRef = new Firebase("https://bitter-gitmad.firebaseio.com");

        if (fbRef.getAuth() == null) {
            authenticateForFirstTime(fbRef);
        } else {
            Log.d("Bitter", "Logged in anonymously");
        }
    }

    private void authenticateForFirstTime(Firebase fbRef) {

        fbRef.authAnonymously(new Firebase.AuthResultHandler() {
            @Override
            public void onAuthenticated(AuthData authData) {
                storeInitialUserData(authData.getUid());
            }

            @Override
            public void onAuthenticationError(FirebaseError firebaseError) {
                Log.d("Bitter", "could not log in");
                Toast.makeText(getApplicationContext(), "Could not log in to server. Check internet.",
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    private String generateRandomUsername() {
        return "asdf"; // TODO
    }

    private void storeInitialUserData(String userId) {
        User me = new User();
        me.setName(generateRandomUsername());

        Firebase userRef = new Firebase("https://bitter-gitmad.firebaseio.com/users/" + userId);

        userRef.setValue(me);
    }
}
