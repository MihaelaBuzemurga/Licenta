package com.example.gabriel.readerlish;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.gabriel.readerlish.ConnectionManager.ConnectionManager;
import com.example.gabriel.readerlish.Maps.MapAction;
import com.example.gabriel.readerlish.Mesaj.Mesaj;
import com.example.gabriel.readerlish.Mesaj.RequestEnum;
import com.example.gabriel.readerlish.Mesaj.RespondeEnum;
import com.example.gabriel.readerlish.Reusable.Views;
import com.example.gabriel.readerlish.User.User;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    public static User m_user;
    public Object m_obiect;
    Handler handler_time;
    int delay = 30000; //milliseconds
    CheckNotification notificationTask;
    ImageButton notificationButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Intent myIntent = getIntent();
        m_user=(User)myIntent.getSerializableExtra("user");



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
    public void setButton(ImageButton button)
    {
        notificationButton=button;
        checkNotification();
    }
    private void checkNotification()
    {
        notificationButton.setVisibility(View.INVISIBLE);
        notificationButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
//                Intent myIntent =new Intent(myView.getContext(),ReadActivity.class);
//                myIntent.putExtra("id_carte",carte.getID());
//                startActivity(myIntent);
            }
        });
        handler_time = new Handler();


        handler_time.postDelayed(new Runnable(){
            public void run(){
                    notificationTask = new CheckNotification();
                    notificationTask.execute((Void) null);
                    handler_time.postDelayed(this, delay);

            }
        }, delay);
    }

    private void updateNotificationButton(int nr)
    {
        if(nr>0)
        {
            notificationButton.setVisibility(View.VISIBLE);
        }
        else
        {
            notificationButton.setVisibility(View.INVISIBLE);
        }



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

        }else if (id == R.id.nav_find) {
            Intent myIntent=new Intent(this,MapAction.class);
            startActivity(myIntent);

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }



    public class CheckNotification extends AsyncTask<Void, Void, Boolean> {
        CheckNotification() {
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            Mesaj mesaj = new Mesaj();
            mesaj.setCmd(RequestEnum.REQUEST_CHECK_NOTIFICATION);
            mesaj.setObiect(m_user.getId());
            Mesaj mesajj= (Mesaj) ConnectionManager.getInstance().sendMessage(mesaj);
            if(mesajj!=null)
            {

                final int nr = (int) mesajj.getObiect();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        updateNotificationButton(nr);
                    }
                });

            }
            else
            {
                return false;
            }
            return true;

        }
        @Override
        protected void onCancelled() {
            notificationTask = null;


        }



    }
}
