package gitmad.bitter.data.auth;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.test.ApplicationTestCase;
import android.test.suitebuilder.annotation.SmallTest;
import android.util.Log;

import com.firebase.client.Firebase;

import gitmad.bitter.data.firebase.FirebaseUserProvider;
import gitmad.bitter.data.firebase.auth.FirebaseAuthManager;

/**
 * Created by brian on 2/19/16.
 */
public class TestFirebaseAuthManager extends ApplicationTestCase<FirebaseApplication> {

    // these are the same keys that the FirebaseAuthManager uses //
    private static final String KEY_EMAIL = "email key";
    private static final String KEY_PASS = "password key";
    private static final String AUTH_PREFS = "authprefs";

    private Application application;
    private SharedPreferences prefs;
    private Firebase usersFirebaseRef;

    public TestFirebaseAuthManager() {
        super(FirebaseApplication.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        createApplication();

        application = getApplication();

        // this is the same as is used in FirebaseAuthManager
        prefs = application.getSharedPreferences(AUTH_PREFS, Context.MODE_PRIVATE);
//        Log.d("Prefs", prefs.getAll().keySet().toString());

        usersFirebaseRef = new Firebase(FirebaseUserProvider.FIREBASE_USERS_URL);

        usersFirebaseRef.unauth();

        Log.d("Bitter", "end of setUp()");
    }

    @SmallTest
    public void testIsAlreadyAuthed() {
        assertNull("auth should be null at this point in the tests", usersFirebaseRef.getAuth());
    }

    @SmallTest
    public void testAuthManagerInit() {
        Log.d("Bitter", "testAuthManagerInit");
        prefs.edit()
                .clear()
                .commit();

        FirebaseAuthManager authManager = new FirebaseAuthManager(application);

        authManager.authenticate();


        assertTrue("prefs should contain an email address", prefs.contains(KEY_EMAIL));

        assertTrue("prefs should contain a password", prefs.contains(KEY_PASS));
    }

    @SmallTest
    public void testAuthenticateConsistently() {
        Log.d("Bitter", "testAuthenticateConsistently");
        FirebaseAuthManager authManager = new FirebaseAuthManager(application);

        authManager.authenticate();
        String pastUid = authManager.getUid();
        authManager.unauth();

        for (int i = 0; i < 3; i++) {
            authManager.authenticate();
            assertEquals("auth uids should be consistent across logins", pastUid, authManager.getUid());
            authManager.unauth();
        }
    }
}
