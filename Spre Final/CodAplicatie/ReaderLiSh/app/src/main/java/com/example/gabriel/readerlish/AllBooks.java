package com.example.gabriel.readerlish;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gabriel.readerlish.Carte.Carte;
import com.example.gabriel.readerlish.ConnectionManager.ConnectionManager;
import com.example.gabriel.readerlish.Mesaj.Mesaj;
import com.example.gabriel.readerlish.Mesaj.RequestEnum;
import com.example.gabriel.readerlish.Mesaj.RespondeEnum;
import com.example.gabriel.readerlish.R;
import com.example.gabriel.readerlish.User.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Gabriel on 18.01.2017.
 */

public class AllBooks extends Fragment{
    private List<Car> myCars = new ArrayList<Car>();
    private LayoutInflater myInflater;
    UserLoginTask mAuthTask;
    ProgressDialog m_pd;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    View myView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.all_books,container,false);
        myInflater=inflater;
        TabHost tabhost =(TabHost) myView.findViewById(R.id.tabhost);
        tabhost.setup();

        m_pd= new ProgressDialog(myView.getContext());
        m_pd.setTitle("Incarcare!");
        m_pd.setMessage("Asteapta");
        TabHost.TabSpec allBooks=tabhost.newTabSpec("Toate cartile");
        allBooks.setContent(R.id.tab1);
        allBooks.setIndicator("Toate cartile");

        TabHost.TabSpec yourBooks=tabhost.newTabSpec("Cartile tale");
        yourBooks.setContent(R.id.tab2);
        yourBooks.setIndicator("Cartile tale");

        tabhost.addTab(allBooks);
        tabhost.addTab(yourBooks);

       m_pd.show();
        mAuthTask=new UserLoginTask(0);
        mAuthTask.execute((Void) null);
        return myView;
    }

    private void populateCarList() {
        Mesaj mesaj=new Mesaj();
        mesaj.setId(0);
        mesaj.setCmd(RequestEnum.REQUEST_BOOKS);
        Mesaj mesajj= (Mesaj) ConnectionManager.getInstance().sendMessage(mesaj);
        Carte[] carti=(Carte[])mesajj.getObiect();
        for(int i=0;i<carti.length;i++)
        {
            if(carti[i]!=null) {
                Bitmap bmp = BitmapFactory.decodeByteArray(carti[i].getImagine(), 0, carti[i].getImagine().length);
                myCars.add(new Car(carti[i].getNume(), carti[i].getID(), R.drawable.ic_menu_send, carti[i].getAutor(),bmp));

            }
        }
    }

    private void populateListView() {
        ArrayAdapter<Car> adapter = new MyListAdapter();
        ListView list = (ListView) myView.findViewById(R.id.listView);
        list.setAdapter(adapter);
    }

    private void registerClickCallback() {
        ListView list = (ListView) myView.findViewById(R.id.listView);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View viewClicked,
                                    int position, long id) {

                Car clickedCar = myCars.get(position);
                String message = "You clicked position " + position
                        + " Which is car make " + clickedCar.getMake();
               System.out.println(message);
            }
        });
    }

    private class MyListAdapter extends ArrayAdapter<Car> {
        public MyListAdapter() {
            super(myView.getContext(), R.layout.item_view, myCars);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // Make sure we have a view to work with (may have been given null)
            View itemView = convertView;
            if (itemView == null) {
                itemView = myInflater.inflate(R.layout.item_view, parent, false);
            }

            // Find the car to work with.
            Car currentCar = myCars.get(position);

            // Fill the view
            ImageView imageView = (ImageView)itemView.findViewById(R.id.item_icon);
            imageView.setImageBitmap(currentCar.getImagine());

            // Make:
            TextView makeText = (TextView) itemView.findViewById(R.id.item_txtMake);
            makeText.setText(currentCar.getMake());

            // Year:
            TextView yearText = (TextView) itemView.findViewById(R.id.item_txtYear);
            yearText.setText("" + currentCar.getYear());

            // Condition:
            TextView condionText = (TextView) itemView.findViewById(R.id.item_txtCondition);
            condionText.setText(currentCar.getCondition());

            return itemView;
        }
    }
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

       private int nr;
        private Mesaj m_mesaj;

        UserLoginTask(int id) {
           this.nr=id;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            Mesaj mesaj=new Mesaj();
            mesaj.setId(0);
            mesaj.setCmd(RequestEnum.REQUEST_BOOKS);
            m_mesaj= (Mesaj) ConnectionManager.getInstance().sendMessage(mesaj);

            if(m_mesaj!=null) {

                return true;
            }
            else
            {
                return false;
            }

        }
        @Override
        protected void onPostExecute(final Boolean success) {
            m_pd.cancel();
            mAuthTask = null;
            if(success)
            {
                Carte[] carti=(Carte[])m_mesaj.getObiect();
                for(int i=0;i<carti.length;i++)
                {
                    if(carti[i]!=null) {
                        Bitmap bmp = BitmapFactory.decodeByteArray(carti[i].getImagine(), 0, carti[i].getImagine().length);
                        myCars.add(new Car(carti[i].getNume(), carti[i].getID(), R.drawable.ic_menu_send, carti[i].getAutor(),bmp));

                    }
                }
                populateListView();
                registerClickCallback();
            }
            else
            {

            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            m_pd.cancel();

        }



    }

}
