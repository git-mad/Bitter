package gitmad.bitter.data.firebase;

import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.util.concurrent.CountDownLatch;

/**
 * Created by brian on 2/20/16.
 */
public class CountDownLatchListener implements Firebase.CompletionListener {

    private CountDownLatch latch;

    public CountDownLatchListener(CountDownLatch pLatch) {
        latch = pLatch;
    }

    public CountDownLatchListener() {
        latch = new CountDownLatch(1);
    }

    public void awaitLatch() {
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onComplete(FirebaseError firebaseError, Firebase firebase) {
        if (firebaseError != null) {
            throw firebaseError.toException();
        }

        latch.countDown();
    }
}
