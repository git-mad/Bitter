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
import gitmad.bitter.data.ImageProvider;
import gitmad.bitter.data.firebase.FirebaseImageProvider;
import gitmad.bitter.data.firebase.auth.FirebaseAuthManager;
import gitmad.bitter.model.FirebaseImage;

import java.util.Stack;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Created by brian on 2/18/16.
 */
public class TestFirebaseImageProvider extends
        ApplicationTestCase<BitterApplication> {

    ImageProvider imageProvider;
    private FirebaseAuthManager authManager;

    public TestFirebaseImageProvider() {
        super(BitterApplication.class);
    }

    @SmallTest
    public void testAddingAndGettingImage() {

        Bitmap bitmap = makeABitmap();


        FirebaseImage originalImage = imageProvider.addImageSync(bitmap);

        FirebaseImage imageFromFirebase = imageProvider.getImage
                (originalImage.getUid());


        assertEquals("Image from firebase should equal the one pushed",
                originalImage, imageFromFirebase);
    }

    @SmallTest
    public void testDeleteImage() {
        Bitmap bitmap = makeABitmap();

        FirebaseImage image = imageProvider.addImageSync(bitmap);

        Firebase imageRef = new Firebase("https://bitter-gitmad.firebaseio" +
                ".com/images/" + image.getUid());

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

    @MediumTest
    public void testFirebaseImageSerialization() {
        final Firebase imageFirebaseRef = new Firebase("https://bitter-gitmad" +
                ".firebaseio.com/images")
                .push();

        FirebaseImage originalFirebaseImage = new FirebaseImage
                (imageFirebaseRef.getKey(),
                        makeABitmap(), authManager.getUid());


        final CountDownLatch pushLatch = new CountDownLatch(1);
        imageFirebaseRef.setValue(originalFirebaseImage, new Firebase
                .CompletionListener() {
            @Override
            public void onComplete(FirebaseError firebaseError, Firebase
                    firebase) {
                if (firebaseError != null) {
                    throw firebaseError.toException();
                }
                pushLatch.countDown();
            }
        });
        awaitLatch(pushLatch);

        final CountDownLatch readLatch = new CountDownLatch(1);
        final AtomicReference<FirebaseImage> imageAtomicReference = new
                AtomicReference<>();

        imageFirebaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                imageFirebaseRef.removeValue();
                imageAtomicReference.set(dataSnapshot.getValue(FirebaseImage
                        .class));
                readLatch.countDown();
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                throw firebaseError.toException();
            }
        });
        awaitLatch(readLatch);


        assertEquals("FirebaseImages should be equal before and after push to" +
                        " database.",
                originalFirebaseImage, imageAtomicReference.get());
    }

    @SmallTest
    public void testGetImagesByUser() {
        // delete past mock images, relying on ImageProvider#getImagesByUser
        // () //
        removePastMockImages();

        Stack<FirebaseImage> originalImages = addImagesToFirebase();

        String mockPostOwnerId = originalImages.peek().getOwnerUid();

        FirebaseImage[] imagesFromFirebase = imageProvider.getImagesByUser
                (mockPostOwnerId);

        int imagesPushed = originalImages.size();

        for (int i = 0; i < imagesPushed; i++) {
            assertEquals("Images should be equal", originalImages.pop(),
                    imagesFromFirebase[i]);
        }
    }

    @NonNull
    private Stack<FirebaseImage> addImagesToFirebase() {
        final int NUM_IMAGES = 4;

        Stack<FirebaseImage> originalImages = new Stack<>();

        for (int i = 0; i < NUM_IMAGES; i++) {
            originalImages.push(imageProvider.addImageSync(makeABitmap()));
        }
        return originalImages;
    }

    private void authenticate() {
        authManager = new FirebaseAuthManager(getApplication());
        if (!authManager.isAuthed()) {
            authManager.authenticate();
        }
    }

    private void awaitLatch(CountDownLatch latch) {
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private Bitmap makeABitmap() {
        return BitmapFactory.decodeResource(getApplication().getResources(),
                R.drawable.ic_menu_camera);
    }

    private void removePastMockImages() {
        String mockOwnerUid = authManager.getUid();
        for (FirebaseImage image : imageProvider.getImagesByUser
                (mockOwnerUid)) {
            imageProvider.deleteImage(image.getUid());
        }
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        createApplication();

        authenticate();

        imageProvider = new FirebaseImageProvider();
    }

}
