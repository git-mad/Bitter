package gitmad.bitter.data.firebase;


import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Provides synchronous method calls for Firebase data being updated by
 * asynchronous
 * callbacks.
 */
public class FirebaseSyncRequester<T> {

    private Firebase firebaseRef;
    private RequesterValueEventListener firebaseListener;

    private AtomicReference<DataSnapshot> currentDataSnapshot;

    private CountDownLatch countDownLatch;

    private Class<T> dataType;


    public FirebaseSyncRequester(Firebase pFirebaseRef, Class<T> genericType) {
        firebaseRef = pFirebaseRef;

        dataType = genericType;

        currentDataSnapshot = new AtomicReference<>();

        initializeLatch();

        registerFirebaseCallback();
    }

    /**
     * Waits for firebase, and then returns whether the data at that URL
     * exists or not.
     *
     * @return true if data exists at the URL, false otherwise.
     */
    public boolean exists() {
        whenReady();

        return currentDataSnapshot.get().exists();
    }

    /**
     * A synchronous get() for the firebase url contained by this
     * FirebaseSyncRequester.
     * Waits until data is present, and then returns
     *
     * @return the data contained by at the firebase url contained by this
     * FirebaseSyncRequester
     */
    public T get() {
        whenReady();

        return currentDataSnapshot.get().getValue(dataType);
    }

    /**
     * determines whether firebase has responded or not.
     *
     * @return true if data is ready for #get() and #exists(), false otherwise.
     */
    public boolean isReady() {
        return countDownLatch.getCount() < 1;
    }

    /**
     * Waits until this requester has data, and then returns a reference to this
     * requester
     *
     * @return a reference to this requester.
     */
    public FirebaseSyncRequester whenReady() {
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return this;
    }

    /**
     * initialize the latch so that CountDownLatch#await() blocks until
     * CountDownLatch#countDown() is called once.
     */
    private void initializeLatch() {
        countDownLatch = new CountDownLatch(1);
    }

    private void registerFirebaseCallback() {
        firebaseListener = new RequesterValueEventListener();

        firebaseRef.addValueEventListener(firebaseListener);
    }

    private class RequesterValueEventListener implements ValueEventListener {

        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            currentDataSnapshot.set(dataSnapshot);
            countDownLatch.countDown();
        }

        @Override
        public void onCancelled(FirebaseError firebaseError) {
            throw firebaseError.toException();
        }
    }
}
