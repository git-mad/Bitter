package gitmad.bitter.model;

import android.graphics.Bitmap;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.util.Date;

/**
 * Created by brian on 2/15/16.
 */
public class FirebaseImage {

    private String uid;
    private String ownerUid;
    private Bitmap bitmap;
    private String imageData;
    private long timestamp;

    public FirebaseImage() {
        // default constructor necessary for firebase //
    }

    public FirebaseImage(String pUid, Bitmap bmp, String pOwnerUid) {
        uid = pUid;

        bitmap = bmp;
        imageData = getImageDataAsString();

        ownerUid = pOwnerUid;
        timestamp = new Date().getTime();
    }

    private String getImageDataAsString() {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();

        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        bitmap.recycle();

        byte[] byteArray = stream.toByteArray();

        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }


    public String getUid() {
        return uid;
    }

    public String getOwnerUid() {
        return ownerUid;
    }

    private String getImageData() {
        return imageData;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof FirebaseImage)) {
            return false;
        }

        FirebaseImage other = (FirebaseImage) o;

        return uid.equals(other.uid) && getImageDataAsString().equals(other.getImageDataAsString());
    }

    @Override
    public int hashCode() {
        return uid.hashCode();
    }
}
