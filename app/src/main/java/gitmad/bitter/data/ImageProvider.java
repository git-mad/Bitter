package gitmad.bitter.data;

import android.graphics.Bitmap;
import gitmad.bitter.model.FirebaseImage;

/**
 * Created by brian on 2/15/16.
 */
public interface ImageProvider {

    FirebaseImage addImageAsync(Bitmap image);

    FirebaseImage addImageSync(Bitmap image);

    FirebaseImage deleteImage(String imageUid);

    FirebaseImage getImage(String imageUid);

    FirebaseImage[] getImagesByUser(String ownerUid);
}
