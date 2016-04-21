package gitmad.bitter.data.firebase;

import android.graphics.Bitmap;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;
import gitmad.bitter.data.ImageProvider;
import gitmad.bitter.model.FirebaseImage;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Created by brian on 2/15/16.
 */
public class FirebaseImageProvider implements ImageProvider {

    private Firebase imagesRef;

    public FirebaseImageProvider() {

        imagesRef = new Firebase("https://bitter-gitmad.firebaseio.com/images");
    }

    private static FirebaseImage[] parsePostsFromDataSnapshot(DataSnapshot
                                                                      dataSnapshot) {
        List<FirebaseImage> postsList = new LinkedList<>();

        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
            postsList.add(0, postSnapshot.getValue(FirebaseImage.class));
        }

        return postsList.toArray(new FirebaseImage[postsList.size()]);
    }

    @Override
    public FirebaseImage addImageAsync(Bitmap bitmap) {
        Firebase newImageRef = imagesRef.push();

        FirebaseImage image = new FirebaseImage(imagesRef.getKey(), bitmap,
                getLoggedInUserId());

        newImageRef.setValue(image);

        return image;
    }

    @Override
    public FirebaseImage addImageSync(Bitmap bitmap) {

        Firebase newImageRef = imagesRef.push();

        FirebaseImage image = new FirebaseImage(newImageRef.getKey(), bitmap,
                getLoggedInUserId());

        CountDownLatchListener countDownLatchOnComplete = new
                CountDownLatchListener();

        newImageRef.setValue(image, countDownLatchOnComplete);

        countDownLatchOnComplete.awaitLatch();

        return image;
    }

    @Override
    public FirebaseImage deleteImage(String imageUid) {
        Firebase imageFirebaseRef = imagesRef.child(imageUid);

        FirebaseImage pastImage = getImage(imageUid);

        CountDownLatchListener countDownLatchOnComplete = new
                CountDownLatchListener();

        imageFirebaseRef.removeValue(countDownLatchOnComplete);

        countDownLatchOnComplete.awaitLatch();

        return pastImage;
    }

    @Override
    public FirebaseImage getImage(String imageUid) {
        Firebase imageFirebaseRef = imagesRef.child(imageUid);

        final CountDownLatch latch = new CountDownLatch(1);
        final AtomicReference<FirebaseImage> imageAtomicRef = new
                AtomicReference<>();

        imageFirebaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                imageAtomicRef.set(dataSnapshot.getValue(FirebaseImage.class));
                latch.countDown();
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                throw firebaseError.toException();
            }
        });

        awaitLatch(latch);

        return imageAtomicRef.get();
    }

    @Override
    public FirebaseImage[] getImagesByUser(String ownerUid) {
        Query userImagesQuery = imagesRef
                .orderByChild("ownerUid")
                .equalTo(ownerUid);

        final AtomicReference<DataSnapshot> snapshotAtomicRef = new
                AtomicReference<>();
        final CountDownLatch latch = new CountDownLatch(1);

        userImagesQuery.addListenerForSingleValueEvent(new ValueEventListener
                () {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                snapshotAtomicRef.set(dataSnapshot);
                latch.countDown();
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                throw firebaseError.toException();
            }
        });

        awaitLatch(latch);
        return parsePostsFromDataSnapshot(snapshotAtomicRef.get());
    }

    private void awaitLatch(CountDownLatch latch) {
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private String getLoggedInUserId() {
        return imagesRef.getAuth().getUid();
    }
}
