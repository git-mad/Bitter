package gitmad.bitter.data.firebase.auth;

import android.content.Context;
import android.content.SharedPreferences;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

import gitmad.bitter.data.firebase.FirebaseUserProvider;

/**
 * Created by brian on 2/18/16.
 */
public class FirebaseAuthManager {

    private static final String KEY_EMAIL = "email key";
    private static final String KEY_PASS = "password key";

    private static final String AUTH_PREFS = "authprefs";


    private SharedPreferences prefs;
    private Firebase firebaseUsersRef;


    public FirebaseAuthManager(Context context) {
        prefs = context.getSharedPreferences(AUTH_PREFS, Context.MODE_PRIVATE);
        firebaseUsersRef = new Firebase(FirebaseUserProvider.FIREBASE_USERS_URL);

        assureAccountCreated();
    }

    public void authenticate() {
        final CountDownLatch latch = new CountDownLatch(1);

        if (!isAuthed()) {
            firebaseUsersRef.authWithPassword(getEmail(), getPassword(), new Firebase.AuthResultHandler() {
                @Override
                public void onAuthenticated(AuthData authData) {
                    latch.countDown();
                }

                @Override
                public void onAuthenticationError(FirebaseError firebaseError) {
                    throw firebaseError.toException();
                }
            });
        } else {
            latch.countDown();
        }

        awaitLatch(latch);
    }

    public String getUid() {
        if (isAuthed()) {
            return firebaseUsersRef.getAuth().getUid();
        } else {
            return null;
        }
    }

    public boolean isAuthed() {
        AuthData authData = firebaseUsersRef.getAuth();
        return firebaseUsersRef.getAuth() != null;
    }

    public void unauth() {
        firebaseUsersRef.unauth();
    }

    private void assureAccountCreated() {
        getEmail();
        getPassword();
    }

    private String getEmail() {
        if (!prefs.contains(KEY_EMAIL)) {
            makeLogin();
        }

        return prefs.getString(KEY_EMAIL, "");
    }

    private String getPassword() {
        if (!prefs.contains(KEY_PASS)) {
            makeLogin();
        }

        return prefs.getString(KEY_PASS, "");
    }

    private void makeLogin() {
        generateRandomCredentials();

        registerWithFirebase();

        pushUserData();

        unauth();
    }

    private void pushUserData() {
        authenticate();

        final CountDownLatch latch = new CountDownLatch(1);

        firebaseUsersRef.updateChildren(initUserData(), new Firebase.CompletionListener() {
            @Override
            public void onComplete(FirebaseError firebaseError, Firebase firebase) {
                if (firebaseError != null) {
                    throw firebaseError.toException();
                }
                latch.countDown();
            }
        });

        awaitLatch(latch);
    }

    private void registerWithFirebase() {
        firebaseUsersRef = new Firebase(FirebaseUserProvider.FIREBASE_USERS_URL);

        final CountDownLatch latch = new CountDownLatch(1);

        firebaseUsersRef.createUser(getEmail(), getPassword(), new Firebase.ResultHandler() {
            @Override
            public void onSuccess() {
                latch.countDown();
            }

            @Override
            public void onError(FirebaseError firebaseError) {
                throw firebaseError.toException();
            }
        });

        awaitLatch(latch);
    }

    private void generateRandomCredentials() {
        String emailString = generateRandomString() + "@fake.bitter.com";
        String password = generateRandomString();

        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(KEY_EMAIL, emailString);
        editor.putString(KEY_PASS, password);
        editor.commit();
    }

    private String generateRandomString() {
        String alphabet = "abcdefghijklmnopqrstuvwxyz";
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < 10; i++) {
            int randomIndex = (int) (Math.random() * alphabet.length());
            builder.append(alphabet.charAt(randomIndex));
        }

        return builder.toString();
    }

    private Map<String, Object> initUserData() {
        Map<String, Object> updateMap = new HashMap<>();
        updateMap.put(getUid(), "test");

        return updateMap;
    }

    private void awaitLatch(CountDownLatch latch) {
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
