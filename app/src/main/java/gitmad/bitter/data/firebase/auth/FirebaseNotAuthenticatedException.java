package gitmad.bitter.data.firebase.auth;

/**
 * Created by brian on 12/26/15.
 */
public class FirebaseNotAuthenticatedException extends RuntimeException {
    public FirebaseNotAuthenticatedException() {
        super("Firebase user must be authenticated.");
    }

    public FirebaseNotAuthenticatedException(String message) {
        super(message);
    }
}
