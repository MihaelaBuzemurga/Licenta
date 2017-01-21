package com.example.gabriel.readerlish;

import android.app.Dialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;

import com.example.gabriel.readerlish.Carte.Carte;
import com.example.gabriel.readerlish.ConnectionManager.ConnectionManager;
import com.example.gabriel.readerlish.Mesaj.Mesaj;
import com.example.gabriel.readerlish.Mesaj.RequestEnum;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Gabriel on 18.01.2017.
 */

public class AllBooks extends Fragment{
    private List<CartiRepository> myCartiRepositories_allBooks = new ArrayList<CartiRepository>();
    private List<CartiRepository> myCartiRepositories_yourBooks = new ArrayList<CartiRepository>();
    ArrayAdapter<CartiRepository> adapter;
    ArrayAdapter<CartiRepository> adapterYourBooks;
    private LayoutInflater myInflater;

    ProgressDialog m_pd;

    GetAllBooksTask m_allBooksTask;
    ListView m_listAllBooks;
    int m_paginaAllBooks=0;
    boolean m_finishAllBooks;


    GetYourBooksTask m_yourBooksTask;
    ListView m_listYourBooks;
    int m_paginaYourBooks=0;
    boolean m_finishYourBooks;
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
        m_finishAllBooks =false;
        m_paginaAllBooks =0;

        createTabHost();
        setProgressDialog();

        populateListViewAllBooks();
        registerClickCallback();
        startGetAllBooks();

        return myView;
    }
    private void startGetAllBooks()
    {
        m_pd.show();
        m_allBooksTask =new GetAllBooksTask(m_paginaAllBooks);
        m_allBooksTask.execute((Void) null);
    }
    private void startGetYourBooks()
    {
        m_pd.show();
        m_yourBooksTask =new GetYourBooksTask(m_paginaYourBooks);
        m_yourBooksTask.execute((Void) null);
    }
    private void setProgressDialog()
    {
        m_pd= new ProgressDialog(myView.getContext());
        m_pd.setTitle("Incarcare!");
        m_pd.setMessage("Asteapta");
    }
    private void createTabHost()
    {
        TabHost tabhost =(TabHost) myView.findViewById(R.id.tabhost);
        tabhost.setup();
        TabHost.TabSpec allBooks=tabhost.newTabSpec("Toate cartile");
        allBooks.setContent(R.id.tab1);
        allBooks.setIndicator("Toate cartile");

        TabHost.TabSpec yourBooks=tabhost.newTabSpec("Cartile tale");
        yourBooks.setContent(R.id.tab2);
        yourBooks.setIndicator("Cartile tale");

        tabhost.addTab(allBooks);
        tabhost.addTab(yourBooks);
    }

    private void populateListViewAllBooks() {
        adapter = new MyListAdapterAllBooks();
        adapterYourBooks= new MyListAdapterAllBooks();

        m_listAllBooks = (ListView) myView.findViewById(R.id.listView);
        m_listAllBooks.setAdapter(adapter);
        m_listAllBooks.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE
                        && (m_listAllBooks.getLastVisiblePosition() - m_listAllBooks.getHeaderViewsCount() -
                        m_listAllBooks.getFooterViewsCount()) >= (m_listAllBooks.getAdapter().getCount() - 1)) {
                    if (!m_finishAllBooks) {
                        m_paginaAllBooks++;
                        startGetAllBooks();

                    }
                }
            }
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });

        m_listYourBooks = (ListView) myView.findViewById(R.id.listView2);
        m_listYourBooks.setAdapter(adapterYourBooks);
        m_listYourBooks.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE
                        && (m_listYourBooks.getLastVisiblePosition() - m_listYourBooks.getHeaderViewsCount() -
                        m_listYourBooks.getFooterViewsCount()) >= (m_listYourBooks.getAdapter().getCount() - 1)) {
                    if (!m_finishYourBooks) {
                        m_paginaYourBooks++;
                        startGetAllBooks();

                    }
                }
            }
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });


    }

    private void registerClickCallback() {
        ListView list = (ListView) myView.findViewById(R.id.listView);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View viewClicked,
                                    int position, long id) {

                CartiRepository clickedCartiRepository = myCartiRepositories_allBooks.get(position);

                Dialog dialog=new Dialog(myView.getContext());
                dialog.setTitle("Carte");
                dialog.setContentView(R.layout.book_layout);
                dialog.show();

            }
        });
        ListView list2 = (ListView) myView.findViewById(R.id.listView2);
        list2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View viewClicked,
                                    int position, long id) {

                CartiRepository clickedCartiRepository = myCartiRepositories_yourBooks.get(position);

            }
        });
    }

    private class MyListAdapterAllBooks extends ArrayAdapter<CartiRepository> {
        public MyListAdapterAllBooks() {
            super(myView.getContext(), R.layout.item_view, myCartiRepositories_allBooks);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // Make sure we have a view to work with (may have been given null)
            View itemView = convertView;
            if (itemView == null) {
                itemView = myInflater.inflate(R.layout.item_view, parent, false);
            }
            CartiRepository currentCartiRepository = myCartiRepositories_allBooks.get(position);

            ImageView imageView = (ImageView)itemView.findViewById(R.id.imagine_carte);
            imageView.setImageBitmap(currentCartiRepository.getImagine());

            TextView gen_carte = (TextView) itemView.findViewById(R.id.gen_carte);
            gen_carte.setText("" + currentCartiRepository.getM_gen());

            TextView nota_carte = (TextView) itemView.findViewById(R.id.nota_carte);
            nota_carte.setText("" + currentCartiRepository.getNota());

            TextView autor = (TextView) itemView.findViewById(R.id.autor_carte);
            autor.setText("" + currentCartiRepository.getM_autor());

            TextView titlu_carte = (TextView) itemView.findViewById(R.id.titlu_carte);
            titlu_carte.setText(currentCartiRepository.getM_nume());

            return itemView;
        }
    }
    private class MyListAdapterYourBooks extends ArrayAdapter<CartiRepository> {
        public MyListAdapterYourBooks() {
            super(myView.getContext(), R.layout.item_view, myCartiRepositories_yourBooks);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // Make sure we have a view to work with (may have been given null)
            View itemView = convertView;
            if (itemView == null) {
                itemView = myInflater.inflate(R.layout.item_view, parent, false);
            }
            CartiRepository currentCartiRepository = myCartiRepositories_yourBooks.get(position);

            ImageView imageView = (ImageView)itemView.findViewById(R.id.imagine_carte);
            imageView.setImageBitmap(currentCartiRepository.getImagine());

            TextView gen_carte = (TextView) itemView.findViewById(R.id.gen_carte);
            gen_carte.setText("" + currentCartiRepository.getM_gen());

            TextView nota_carte = (TextView) itemView.findViewById(R.id.nota_carte);
            nota_carte.setText("" + currentCartiRepository.getNota());

            TextView autor = (TextView) itemView.findViewById(R.id.autor_carte);
            autor.setText("" + currentCartiRepository.getM_autor());

            TextView titlu_carte = (TextView) itemView.findViewById(R.id.titlu_carte);
            titlu_carte.setText(currentCartiRepository.getM_nume());

            return itemView;
        }
    }
    public class GetAllBooksTask extends AsyncTask<Void, Void, Boolean> {
        private int nr;
        private Mesaj m_mesaj;

        GetAllBooksTask(int nr) {
            this.nr=nr;
        }
        @Override
        protected Boolean doInBackground(Void... params) {
            Mesaj mesaj=new Mesaj();
            mesaj.setId(nr);
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
            m_allBooksTask = null;
            if(success)
            {
                Carte[] carti=(Carte[])m_mesaj.getObiect();
                for(int i=0;i<carti.length;i++)
                {
                    if(carti[i]!=null) {
                        Bitmap bmp = BitmapFactory.decodeByteArray(carti[i].getImagine(), 0, carti[i].getImagine().length);
                        myCartiRepositories_allBooks.add(new CartiRepository(carti[i].getNume(),carti[i].getGen(),String.valueOf(carti[i].getNota()),carti[i].getAutor(),bmp));
                    }
                    else
                    {
                        m_finishAllBooks = true;
                    }
                }
                adapter.notifyDataSetChanged();
            }
        }
        @Override
        protected void onCancelled() {
            m_allBooksTask = null;
            m_pd.cancel();
        }
    }

    public class GetYourBooksTask extends AsyncTask<Void, Void, Boolean> {
        private int nr;
        private Mesaj m_mesaj;

        GetYourBooksTask(int nr) {
            this.nr=nr;
        }
        @Override
        protected Boolean doInBackground(Void... params) {
            Mesaj mesaj=new Mesaj();
            mesaj.setId(nr);
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
            m_yourBooksTask = null;
            if(success)
            {
                Carte[] carti=(Carte[])m_mesaj.getObiect();
                for(int i=0;i<carti.length;i++)
                {
                    if(carti[i]!=null) {
                        Bitmap bmp = BitmapFactory.decodeByteArray(carti[i].getImagine(), 0, carti[i].getImagine().length);
                        myCartiRepositories_yourBooks.add(new CartiRepository(carti[i].getNume(),carti[i].getGen(),String.valueOf(carti[i].getNota()),carti[i].getAutor(),bmp));
                    }
                    else
                    {
                        m_finishYourBooks = true;
                    }
                }
                adapterYourBooks.notifyDataSetChanged();
            }
        }
        @Override
        protected void onCancelled() {
            m_yourBooksTask = null;
            m_pd.cancel();
        }
    }


}
