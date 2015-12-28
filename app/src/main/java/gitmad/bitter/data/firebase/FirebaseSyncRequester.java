package gitmad.bitter.data.firebase;

import android.util.Log;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.GenericTypeIndicator;
import com.firebase.client.ValueEventListener;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReference;

/**
 *
 */
public class FirebaseSyncRequester<T> {

    private Firebase firebaseRef;
    private RequesterValueEventListener firebaseListener;

    private AtomicReference<DataSnapshot> currentDataSnapshot;

    private CountDownLatch countDownLatch;

    public FirebaseSyncRequester(String firebasePath) {
        firebaseRef = new Firebase(firebasePath);

        initializeLatch();

        registerFirebaseCallback();
    }

    public boolean exists() {
        throwExceptionIfNotReady();

        return currentDataSnapshot.get().exists();
    }

    public T get() {
        throwExceptionIfNotReady();

        return currentDataSnapshot.get().getValue(new GenericTypeIndicator<T>() {});
    }

    /**
     * Waits until this requester has data, and then returns a reference to this
     * requester
     * @return a reference to this requester.
     */
    public FirebaseSyncRequester<T> whenReady() {
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return this;
    }

    public boolean isReady() {
        return countDownLatch.getCount() < 1;
    }

    private void throwExceptionIfNotReady() {
        if (!isReady()) {
            throw new IllegalStateException("FirebaseSyncRequester has not yet received data from firebase.");
        }
    }

    private void registerFirebaseCallback() {
        if (firebaseListener != null) {
            throw new IllegalStateException("RequesterValueEventListener already registered.");
        }

        firebaseListener = new RequesterValueEventListener();

        firebaseRef.addValueEventListener(firebaseListener);
    }

    private void initializeLatch() {
        countDownLatch = new CountDownLatch(1);
    }

    private class RequesterValueEventListener implements ValueEventListener {

        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            currentDataSnapshot.set(dataSnapshot);
            countDownLatch.countDown();
        }

        @Override
        public void onCancelled(FirebaseError firebaseError) {
            Log.e("Bitter", "Connection problem with Firebase FirebaseSyncRequester");
        }
    }
}
