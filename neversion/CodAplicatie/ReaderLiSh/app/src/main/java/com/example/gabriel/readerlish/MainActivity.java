package com.example.gabriel.readerlish;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.example.gabriel.readerlish.Reusable.Views;
import com.example.gabriel.readerlish.User.User;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    public static User m_user;
    public Object m_obiect;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Intent myIntent = getIntent();
        m_user=(User)myIntent.getSerializableExtra("user");

        System.out.println("ceva");
//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        AllBooksActivity allbooks = new AllBooksActivity();
        FragmentManager fragmentManager=getFragmentManager();
        fragmentManager.beginTransaction().replace(
                R.id.content_main,
                allbooks,
                allbooks.getTag()
        ).commit();

    }

    @Override
    public void onBackPressed() {
        FragmentManager fragmentManager=getFragmentManager();
        if(fragmentManager.getBackStackEntryCount() != 0) {
            fragmentManager.popBackStack();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void changeView(Views views)
    {
        if(views == Views.EDIT_BOOKS)
        {
            FragmentManager fragmentManager=getFragmentManager();
            FragmentTransaction ft = fragmentManager.beginTransaction();
            ft.replace(R.id.content_main,new EditActivity());
            ft.addToBackStack(null);
            ft.commit();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

    }
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        FragmentManager fragmentManager=getFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();
       // FloatingActionButton floatButton=(FloatingActionButton)findViewById(R.id.fab);
        if (id == R.id.nav_allbooks) {
           // floatButton.setVisibility(View.VISIBLE);

            ft.replace(R.id.content_main,new AllBooksActivity());
            ft.addToBackStack(null);

            ft.commit();
            // Handle the camera action
        } else if (id == R.id.nav_uploadbook) {
            //floatButton.setVisibility(View.INVISIBLE);
            ft.replace(R.id.content_main,new UploadBook());
            ft.addToBackStack(null);

            ft.commit();

        } else if (id == R.id.nav_yourprofile) {
           // floatButton.setVisibility(View.INVISIBLE);
            ft.replace(R.id.content_main,new YourProfile());
            ft.addToBackStack(null);

            ft.commit();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
