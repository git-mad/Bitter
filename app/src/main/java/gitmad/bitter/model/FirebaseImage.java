package gitmad.bitter.model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.ByteArrayOutputStream;
import java.util.Date;

/**
 * Created by brian on 2/15/16.
 */
public class FirebaseImage {

    private String uid;
    private String ownerUid;
    @JsonIgnore
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

        int quality = 100;

        getBitmap().compress(Bitmap.CompressFormat.PNG, quality, stream);

        byte[] byteArray = stream.toByteArray();

        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }


    public String getUid() {
        return uid;
    }

    public String getOwnerUid() {
        return ownerUid;
    }

    public String getImageData() {
        return imageData;
    }

    public long getTimestamp() {
        return timestamp;
    }


    public Bitmap getBitmap() {
        if (bitmap == null) {
            bitmap = getImageDataAsBitmap();
        }
        return bitmap;
    }

    private Bitmap getImageDataAsBitmap() {
        byte[] encodeByte = Base64.decode(imageData, Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
        return bitmap;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof FirebaseImage)) {
            return false;
        }

        FirebaseImage other = (FirebaseImage) o;

        return getUid().equals(other.getUid()) && getImageDataAsString().equals(other.getImageDataAsString());
    }

    @Override
    public int hashCode() {
        return uid.hashCode();
    }
}
