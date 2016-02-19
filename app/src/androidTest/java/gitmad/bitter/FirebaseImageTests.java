package gitmad.bitter;

import android.test.ApplicationTestCase;

import com.firebase.client.Firebase;

/**
 * Created by brian on 2/18/16.
 */
public class FirebaseImageTests extends ApplicationTestCase<BitterApplication> {



    public FirebaseImageTests(Class<BitterApplication> applicationClass) {
        super(applicationClass);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        createApplication();
    }
}
