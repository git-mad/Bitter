package gitmad.bitter.fragment;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import gitmad.bitter.R;
import gitmad.bitter.data.CommentProvider;
import gitmad.bitter.data.PostProvider;
import gitmad.bitter.data.UserProvider;
import gitmad.bitter.model.Post;
import gitmad.bitter.ui.PostAdapter;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class FeedFragment extends Fragment implements AuthorPostDialogFragment
        .OnPostCreatedListener {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private String imagePath;

    private PostProvider postProvider;
    private CommentProvider commentProvider;
    private UserProvider userProvider;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public FeedFragment() {
    }

    public static FeedFragment newInstance() {
        FeedFragment fragment = new FeedFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Creates file for saving photo
     *
     * @return File for saving photo
     * @throws IOException File cannot be created
     */
    public File createFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new
                Date());
        String fileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                fileName,
                ".jpg",
                storageDir);
        imagePath = "file:" + image.getAbsolutePath();
        return image;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            final int resultOk = -1;
            if (resultCode == resultOk) {
                Toast.makeText(getActivity(), "Image saved.",
                        Toast.LENGTH_LONG).show();
                //image should be accessed here using filename imagePath
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        final View view = inflater.inflate(R.layout.fragment_feed, container,
                false);
        final FloatingActionButton createPost = (FloatingActionButton) view
                .findViewById(R.id.create_post_button);
        final FloatingActionButton takePic = (FloatingActionButton) view
                .findViewById(R.id.camera_fab);
        final FloatingActionButton picFromGallery = (FloatingActionButton)
                view.findViewById(R.id.gallery_fab);
        final FloatingActionButton textPost = (FloatingActionButton) view
                .findViewById(R.id.text_fab);

        takePic.hide();
        picFromGallery.hide();
        textPost.hide();
        createPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (takePic.isShown() && picFromGallery.isShown() && takePic
                        .isShown()) {
                    takePic.hide();
                    picFromGallery.hide();
                    textPost.hide();
                } else {
                    takePic.show();
                    picFromGallery.show();
                    textPost.show();
                    takePicHandler(takePic);
                }


            }
        });

        final Intent photoOp = new Intent();
        photoOp.setType("image/*");
        photoOp.setAction(Intent.ACTION_VIEW);
        picFromGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(photoOp);
            }
        });

        textPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCreatePostDialog();
            }
        });

        // TODO different SortedPostFragment for Feed Activity
        SortedPostFragment sortedPostsFragment = new TopPostFragment();
        FragmentTransaction transaction = getChildFragmentManager()
                .beginTransaction();
        transaction.add(R.id.fragment_feed_sorted_posts_frame,
                sortedPostsFragment).commit();

        return view;
    }

    @Override
    public void onPostCreated(String postText) {
        Post newPost = postProvider.addPostSync(postText);
        ((PostAdapter) adapter).add(newPost);
        recyclerView.swapAdapter(adapter, false);
    }

    private void showCreatePostDialog() {
        AuthorPostDialogFragment authorPostDialogFragment =
                AuthorPostDialogFragment.newInstance();
        authorPostDialogFragment.show(getActivity().getSupportFragmentManager
                (), AuthorPostDialogFragment.AUTHOR_POST_DIALOG_FRAG_TAG);
    }

    /**
     * Sets listeners for floating action buttons
     *
     * @param takePic floating action button to set listener to
     */
    private void takePicHandler(FloatingActionButton takePic) {

        takePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (intent.resolveActivity(getContext().getPackageManager())
                        != null) {
                    File file;
                    try {
                        file = createFile();
                    } catch (IOException e) {
                        file = null;
                    }
                    if (file != null) {
                        intent.putExtra(MediaStore.EXTRA_OUTPUT,
                                Uri.fromFile(file));
                        final int IMAGE_REQUEST_CODE = 1;
                        startActivityForResult(intent, IMAGE_REQUEST_CODE);
                    }
                }
            }
        });
    }
}
