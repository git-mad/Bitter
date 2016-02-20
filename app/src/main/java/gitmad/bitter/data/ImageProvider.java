package gitmad.bitter.data;

import gitmad.bitter.model.FirebaseImage;

/**
 * Created by brian on 2/15/16.
 */
public interface ImageProvider {

    void addImage(FirebaseImage image);

    FirebaseImage getImage(String imageUid);

    FirebaseImage deleteImage(String imageUid);
}
