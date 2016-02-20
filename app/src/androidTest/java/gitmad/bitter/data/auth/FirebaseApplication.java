package gitmad.bitter.data.auth;

import android.app.Application;

import com.firebase.client.Firebase;

/**
 * Created by brian on 2/19/16.
 */
public class FirebaseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        Firebase.setAndroidContext(getApplicationContext());
    }
}
