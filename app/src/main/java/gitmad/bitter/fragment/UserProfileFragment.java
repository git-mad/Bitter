package gitmad.bitter.fragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import gitmad.bitter.R;
import gitmad.bitter.data.firebase.FirebaseUserProvider;
import gitmad.bitter.data.mock.MockUserProvider;
import gitmad.bitter.model.User;

// TODO # of followers + following

/**
 * A placeholder fragment containing a simple view.
 */
public class UserProfileFragment extends Fragment {

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public UserProfileFragment() {
    }

    public static UserProfileFragment newInstance() {
        UserProfileFragment fragment = new UserProfileFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public static Bitmap getRoundedRectBitmap(Bitmap bmp, int radius) {
        Bitmap sbmp;
        if (bmp.getWidth() != radius || bmp.getHeight() != radius)
            sbmp = Bitmap.createScaledBitmap(bmp, radius, radius, false);
        else
            sbmp = bmp;
        Bitmap output = Bitmap.createBitmap(sbmp.getWidth(),
                sbmp.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, sbmp.getWidth(), sbmp.getHeight());

        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        paint.setDither(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(Color.parseColor("#BAB399"));
        canvas.drawCircle(sbmp.getWidth() / 2 + 0.7f, sbmp.getHeight() / 2 + 0.7f,
                sbmp.getWidth() / 2 + 0.1f, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(sbmp, rect, rect, paint);

        return output;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_profile, container, false);

        ImageView pic = (ImageView) view.findViewById(R.id.user_profile_pic);
        Bitmap bm = BitmapFactory.decodeResource(getResources(), R.mipmap.tejunareddy); //FIXME how do I get the image from a variable id?
        Bitmap conv_bm = getRoundedRectBitmap(bm, 500);
        pic.setImageBitmap(conv_bm);

        MockUserProvider dataSrc = new MockUserProvider();

        User myUser = dataSrc.getUser("me123");

        TextView userName = (TextView) view.findViewById(R.id.user_profile_username);
        userName.setText(myUser.getName());

        TextView userSalt = (TextView) view.findViewById(R.id.user_profile_salt);
        userSalt.setText(myUser.getSalt());

        TextView countPosts = (TextView) view.findViewById(R.id.user_profile_posts);
        userSalt.setText(myUser.getPosts());

        TextView totalVotes = (TextView) view.findViewById(R.id.user_profile_votes);
        totalVotes.setText(myUser.getTotalVotes());


        return view;
    }


}
