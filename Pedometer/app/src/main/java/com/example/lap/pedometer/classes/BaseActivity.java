package com.example.lap.pedometer.classes;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.lap.pedometer.R;
import com.example.lap.pedometer.ui.HistoryActivity;
import com.example.lap.pedometer.ui.MainActivity;
import com.example.lap.pedometer.ui.SettingsActivity;
import com.example.lap.pedometer.ui.SplashActivity;

/**
 * Created by lap on 3/30/2018.
 */

public class BaseActivity extends AppCompatActivity implements MenuItem.OnMenuItemClickListener {

    protected FrameLayout main_layout; //This is the framelayout to keep your content view
    private NavigationView navigation_view; // The new navigation view from Android Design Library. Can inflate menu resources. Easy
    private DrawerLayout DrawerLayout;
    private ActionBarDrawerToggle DrawerToggle;
    private Menu drawerMenu;
    private User currentUser;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_base);
        main_layout = (FrameLayout) findViewById(R.id.view_stub);
        navigation_view = (NavigationView) findViewById(R.id.navigation_view);
        DrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        DrawerToggle = new ActionBarDrawerToggle(this, DrawerLayout,R.string.open, R.string.close);
        DrawerLayout.addDrawerListener(DrawerToggle);
        DrawerToggle.syncState();

        currentUser = SplashActivity.getCurrentUser();
        drawerMenu = navigation_view.getMenu();
        for(int i = 0; i < drawerMenu.size(); i++) {
            drawerMenu.getItem(i).setOnMenuItemClickListener(this);
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    @Override
    public void setContentView(int layoutResID) {
        if (main_layout != null) {
            LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
            ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);
            View stubView = inflater.inflate(layoutResID, main_layout, false);
            main_layout.addView(stubView, lp);
        }
    }

    @Override
    public void setContentView(View view) {
        if (main_layout != null) {
            ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);
            main_layout.addView(view, lp);
        }
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        if (main_layout != null) {
            main_layout.addView(view, params);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (DrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);

    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        if (currentUser.getWeight()!=0 && currentUser.getHeight() !=0 && currentUser.getStepLegth() !=0)
        {
            switch (menuItem.getItemId()) {
                case R.id.nav_newrun:
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    finish();
                    break;
                case R.id.nav_settings:
                    startActivity(new Intent(getApplicationContext(), SettingsActivity.class));
                    finish();
                    break;
                case R.id.nav_history:
                    startActivity(new Intent(getApplicationContext(), HistoryActivity.class));
                    finish();
                    break;
                case R.id.nav_logout:
                    // handle it
                    break;
                case R.id.nav_profile:
                    startActivity(new Intent(getBaseContext(), MainActivity.class));
                    finish();
                    break;
            }
            DrawerLayout.closeDrawer(GravityCompat.START);
        }

        else {
            Toast.makeText(getApplicationContext(),"Please Enter your Information",Toast.LENGTH_LONG).show();
        }

        return false;
    }
}
