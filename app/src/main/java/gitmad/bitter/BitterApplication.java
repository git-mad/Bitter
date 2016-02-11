package gitmad.bitter;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.util.concurrent.CountDownLatch;

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
            Log.d("Bitter", "logging in for first time");
            authenticateForFirstTime(fbRef);
        } else {
            Log.d("Bitter", "Logged in anonymously");
        }
    }

    private void authenticateForFirstTime(Firebase fbRef) {
        final CountDownLatch latch = new CountDownLatch(1);

        fbRef.authAnonymously(new Firebase.AuthResultHandler() {
            @Override
            public void onAuthenticated(AuthData authData) {
                storeInitialUserData(authData.getUid());
                latch.countDown();
            }

            @Override
            public void onAuthenticationError(FirebaseError firebaseError) {
                Log.d("Bitter", "could not log in");
                Log.e("Bitter", firebaseError.toString());
                Toast.makeText(getApplicationContext(), "Could not log in to server. Check internet.",
                        Toast.LENGTH_LONG).show();
                latch.countDown();
            }
        });

        awaitLatch(latch);
    }

    private static void awaitLatch(CountDownLatch latch) {
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private String generateRandomUsername() {
        String alphabet = "abcdefghijklmnopqrstuvwxyz";
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < 10; i++) {
            int randomIndex = (int) (Math.random() * alphabet.length());
            builder.append(alphabet.charAt(randomIndex));
        }

        return builder.toString();
    }

    private void storeInitialUserData(String userId) {
        User me = new User(generateRandomUsername(), userId);

        Firebase userRef = new Firebase("https://bitter-gitmad.firebaseio.com/users/" + userId);

        userRef.setValue(me);
    }
}
