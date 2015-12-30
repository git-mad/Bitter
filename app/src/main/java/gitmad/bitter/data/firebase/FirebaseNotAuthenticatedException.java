package gitmad.bitter.data.firebase;

/**
 * Created by brian on 12/26/15.
 */
class FirebaseNotAuthenticatedException extends RuntimeException {
    public FirebaseNotAuthenticatedException() {
        super("Firebase user must be authenticated.");
    }

    public FirebaseNotAuthenticatedException(String message) {
        super(message);
    }
}
