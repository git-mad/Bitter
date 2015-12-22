package gitmad.bitter.activity;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import gitmad.bitter.R;
import gitmad.bitter.fragment.AuthorPostDialogFragment;
import gitmad.bitter.fragment.FeedFragment;
import gitmad.bitter.fragment.UserFragment;
import gitmad.bitter.model.Post;

public class NavigationActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener,
        AuthorPostDialogFragment.OnPostCreatedListener {

    private DrawerLayout drawer;
    private Toolbar toolbar;
    private MenuItem previousMI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // FIXME doesn't highlight feed fragment menu item in nav drawer
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.mainFrameLayout, FeedFragment.newInstance()).commit();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        Class fragmentClass = null;
        if (id == R.id.nav_feed) {
            fragmentClass = FeedFragment.class;
        } else if (id == R.id.nav_user) {
            fragmentClass = UserFragment.class;
        } else if (id == R.id.nav_settings) {

        }

        Fragment fragment;
        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            System.out.println("FRAGMENT NOT SET or NOT A PROPER FRAGMENT");
            Toast.makeText(this, "Fragment not set or not a proper fragment", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
            return false;
        }

        if (previousMI != null) {
            previousMI.setChecked(false);
        }
        previousMI = item;

        // Insert the fragment by replacing any existing fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.mainFrameLayout, fragment).commit();

        // Update the title, and close the drawer
        item.setChecked(true);
        setTitle(item.getTitle());
        drawer.closeDrawers();


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    // TODO
    @Override
    public void onPostCreated(Post post) {
        Log.d("Bitter", "NavigationView#onPostCreated(" + post.toString() + ")");
    }
}
