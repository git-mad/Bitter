package gitmad.bitter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.test.ApplicationTestCase;
import android.test.suitebuilder.annotation.MediumTest;
import android.test.suitebuilder.annotation.SmallTest;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReference;

import gitmad.bitter.data.ImageProvider;
import gitmad.bitter.data.firebase.FirebaseImageProvider;
import gitmad.bitter.model.FirebaseImage;

/**
 * Created by brian on 2/18/16.
 */
public class TestFirebaseImageProvider extends ApplicationTestCase<BitterApplication> {

    ImageProvider imageProvider;

    public TestFirebaseImageProvider(Class<BitterApplication> applicationClass) {
        super(applicationClass);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        createApplication();

        imageProvider = new FirebaseImageProvider();
    }

    @MediumTest
    public void testFirebaseImageSerialization() {
        final Firebase tempFirebaseLocation = new Firebase("https://bitter-gitmad.firebaseio.com/testFirebaseImage");

        FirebaseImage originalFirebaseImage = makeAFirebaseImage();


        final CountDownLatch pushLatch = new CountDownLatch(1);
        tempFirebaseLocation.setValue(originalFirebaseImage, new Firebase.CompletionListener() {
            @Override
            public void onComplete(FirebaseError firebaseError, Firebase firebase) {
                if (firebaseError != null) {
                    throw firebaseError.toException();
                }
                pushLatch.countDown();
            }
        });
        awaitLatch(pushLatch);

        final CountDownLatch readLatch = new CountDownLatch(1);
        final AtomicReference<FirebaseImage> imageAtomicReference = new AtomicReference<>();

        tempFirebaseLocation.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                tempFirebaseLocation.removeValue();
                imageAtomicReference.set(dataSnapshot.getValue(FirebaseImage.class));
                readLatch.countDown();
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                throw firebaseError.toException();
            }
        });
        awaitLatch(readLatch);


        assertEquals("FirebaseImages should be equal before and after push to database.",
                originalFirebaseImage, imageAtomicReference.get());
    }

    @SmallTest
    public void testAddingAndGettingImage() {

        FirebaseImage originalImage = makeAFirebaseImage();


        imageProvider.addImageSync(originalImage);

        FirebaseImage imageFromFirebase = imageProvider.getImage(originalImage.getUid());


        assertEquals("Image from firebase should equal the one pushed", originalImage, imageFromFirebase);
    }

    @SmallTest
    public void testDeleteImage() {
        FirebaseImage image = makeAFirebaseImage();

        Firebase imageRef = new Firebase("https://bitter-gitmad.firebaseio.com/images/" + image.getUid());


        imageProvider.addImageSync(image);

        imageProvider.deleteImage(image.getUid());

        final CountDownLatch latch = new CountDownLatch(1);
        imageRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                assertFalse("should not contain data", dataSnapshot.exists());
                latch.countDown();
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                throw firebaseError.toException();
            }
        });

        awaitLatch(latch);
    }

    @SmallTest
    public void testGetImagesByUser() {
        // delete past mock images, relying on ImageProvider#getImagesByUser() //
        removePastMockImages();

        FirebaseImage[] originalImages = addImagesToFirebase();

        String mockPostOwnerId = originalImages[0].getOwnerUid();

        FirebaseImage[] imagesFromFirebase = imageProvider.getImagesByUser(mockPostOwnerId);

        for (int i = 0; i < originalImages.length; i++) {
            assertEquals("Images should be equal", originalImages[i], imagesFromFirebase[i]);
        }
    }

    @NonNull
    private FirebaseImage[] addImagesToFirebase() {
        final int NUM_IMAGES = 4;

        FirebaseImage[] originalImages = new FirebaseImage[NUM_IMAGES];

        for (int i = 0; i < originalImages.length; i++) {
            originalImages[i] = makeAFirebaseImage();
            imageProvider.addImageSync(originalImages[i]);
        }
        return originalImages;
    }

    private void removePastMockImages() {
        String mockOwnerUid = makeAFirebaseImage().getOwnerUid();
        for (FirebaseImage image : imageProvider.getImagesByUser(mockOwnerUid)) {
            imageProvider.deleteImage(image.getUid());
        }
    }

    private FirebaseImage makeAFirebaseImage() {
        Bitmap originalBitmap = BitmapFactory.decodeResource(getApplication().getResources(), R.drawable.ic_menu_camera);

        final String IMAGE_UID = "imageUID";
        final String AUTHOR_UID = "authoruid";

        return new FirebaseImage(IMAGE_UID, originalBitmap, AUTHOR_UID);
    }

    private void awaitLatch(CountDownLatch latch) {
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
