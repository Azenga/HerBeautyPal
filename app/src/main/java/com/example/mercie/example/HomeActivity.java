package com.example.mercie.example;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private ViewPager fragHolder;
    private MyFragStatePagerAdapter myFragStatePagerAdapter;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        fragHolder = (ViewPager) findViewById(R.id.frag_holder);
        myFragStatePagerAdapter = new MyFragStatePagerAdapter(getSupportFragmentManager());

        myFragStatePagerAdapter.addFragment(new HomePageFragment(), "Home Page");
        myFragStatePagerAdapter.addFragment(new ChangeProfileFragment(), "Change Profile");

        fragHolder.setAdapter(myFragStatePagerAdapter);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

    }

/*
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        animationDrawable.start();
    }
*/

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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_rateUs) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_changeprofile) {
            toolbar.setTitle("Profile Settings");
            fragHolder.setCurrentItem(1);
        } else if (id == R.id.nav_homepage) {
            toolbar.setTitle("Beauty Pal");
            fragHolder.setCurrentItem(0);
        } else if (id == R.id.nav_feedback) {
            toolbar.setTitle("Feedback");
            fragHolder.setCurrentItem(2);
        } else if (id == R.id.nav_about) {
            toolbar.setTitle("About");
            fragHolder.setCurrentItem(3);

        } else if (id == R.id.nav_history) {
            toolbar.setTitle("History");
            fragHolder.setCurrentItem(4);

        } else if (id == R.id.nav_checkDetails) {
            toolbar.setTitle("Reservation Details");
            fragHolder.setCurrentItem(5);

        } else if (id == R.id.nav_logout) {
            toolbar.setTitle("Log Out");
            fragHolder.setCurrentItem(6);

        } else if (id == R.id.nav_share) {
            toolbar.setTitle("Share");
            fragHolder.setCurrentItem(7);

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
