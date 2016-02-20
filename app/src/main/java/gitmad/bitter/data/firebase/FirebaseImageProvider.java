package gitmad.bitter.data.firebase;

import com.firebase.client.Firebase;

import gitmad.bitter.data.ImageProvider;
import gitmad.bitter.model.FirebaseImage;

/**
 * Created by brian on 2/15/16.
 */
public class FirebaseImageProvider implements ImageProvider {

    private Firebase imagesRef;

    public FirebaseImageProvider() {

    }

    @Override
    public void addImageSync(FirebaseImage image) {
        Firebase newImageRef = imagesRef.push();

        imagesRef.setValue(image);
    }

    @Override
    public FirebaseImage getImage(String imageUid) {
//        Firebase imageRef = images;
        return null;
    }

    @Override
    public FirebaseImage deleteImage(String imageUid) {
        return null;
    }
}
