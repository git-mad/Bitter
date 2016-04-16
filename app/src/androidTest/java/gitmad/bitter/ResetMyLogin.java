package gitmad.bitter;

import android.test.ApplicationTestCase;
import gitmad.bitter.data.firebase.auth.FirebaseAuthManager;

/**
 * Created by brian on 3/6/16.
 */
public class ResetMyLogin extends ApplicationTestCase<BitterApplication> {

    public ResetMyLogin() {
        super(BitterApplication.class);
    }

    public void testButNotReally() {

        FirebaseAuthManager authManager = new FirebaseAuthManager
                (getApplication());

        authManager.resetAuth("I know what I'm doing");
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        createApplication();
    }
}
