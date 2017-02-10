package com.example.gabriel.readerlish;

import android.app.Dialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.gabriel.readerlish.Carte.Carte;
import com.example.gabriel.readerlish.ConnectionManager.ConnectionManager;
import com.example.gabriel.readerlish.Mesaj.Mesaj;
import com.example.gabriel.readerlish.Mesaj.RequestEnum;
import com.example.gabriel.readerlish.Reusable.Views;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Gabriel on 18.01.2017.
 */

public class AllBooksActivity extends Fragment{
    private List<CartiRepository> myCartiRepositories_allBooks = new ArrayList<CartiRepository>();
    private List<CartiRepository> myCartiRepositories_yourBooks = new ArrayList<CartiRepository>();
    ArrayAdapter<CartiRepository> adapter;
    ArrayAdapter<CartiRepository> adapterYourBooks;
    private LayoutInflater myInflater;
    private Dialog m_dialogCarte;
    private static RadioGroup radio_g;
    private  RadioButton radio_yourBooks;
    private  RadioButton radio_allBooks;

    private RequestEnum m_requestBooks;
    ProgressDialog m_pd;
    GetAllBooksTask m_allBooksTask;
    GetBookTask m_getBookTask;
    ListView m_listAllBooks;
    int m_paginaAllBooks=0;
    boolean m_finishAllBooks;
    float notaUser;
    RatingBar ratingBar;

    int state;
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            // Restore last state for checked position.
            state = savedInstanceState.getInt("MyState", 0);
        }
        else
        {
            state=0;
        }
    }
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
        notaUser=0;

       // createTabHost();
        setProgressDialog();

        populateListViewAllBooks();
        registerClickCallback();
        onClickListenerButton();
        if(state==0)
        {
            radio_allBooks.setChecked(true);
            m_requestBooks=RequestEnum.REQUEST_BOOKS;
        }
        else
        {
            radio_yourBooks.setChecked(true);
            m_requestBooks=RequestEnum.REQUEST_MY_BOOKS;
        }
        sendButtonInMainAcitvity();
        return myView;
    }
    private void sendButtonInMainAcitvity()
    {
        MainActivity activity = (MainActivity) getActivity();
        ImageButton buton=(ImageButton)myView.findViewById(R.id.notificationButton);
        activity.setButton(buton);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

        savedInstanceState.putInt("MyState", state);
        // etc.
    }

    private void startGetAllBooks()
    {
        m_pd.show();
        m_allBooksTask =new GetAllBooksTask(m_paginaAllBooks);
        m_allBooksTask.execute((Void) null);
    }
    public void onClickListenerButton() {
        radio_g = (RadioGroup)myView.findViewById(R.id.radio_grup);
        radio_yourBooks = (RadioButton) myView.findViewById(R.id.radioButton3);
        radio_allBooks = (RadioButton)myView.findViewById(R.id.radioButton2);

        radio_g.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // checkedId is the RadioButton selected
                if(radio_yourBooks.getId()==checkedId)
                {
                    radio_yourBooks.setTypeface(Typeface.DEFAULT_BOLD);
                    radio_allBooks.setTypeface(Typeface.DEFAULT);
                    radio_yourBooks.setTextSize(18);
                    radio_allBooks.setTextSize(14);
                    m_requestBooks=RequestEnum.REQUEST_MY_BOOKS;
                    m_paginaAllBooks=0;
                    myCartiRepositories_allBooks.clear();
                    m_finishAllBooks=false;
                    state=1;
                    startGetAllBooks();

                }
                else
                {
                    radio_yourBooks.setTypeface(Typeface.DEFAULT);
                    radio_allBooks.setTypeface(Typeface.DEFAULT_BOLD);
                    radio_yourBooks.setTextSize(14);
                    radio_allBooks.setTextSize(18);
                    m_requestBooks=RequestEnum.REQUEST_BOOKS;
                    myCartiRepositories_allBooks.clear();
                    m_paginaAllBooks=0;
                    m_finishAllBooks=false;
                    state=0;
                    startGetAllBooks();
                }

            }
        });
    }
    private void setProgressDialog()
    {
        m_pd= new ProgressDialog(myView.getContext());
        m_pd.setTitle("Incarcare!");
        m_pd.setMessage("Asteapta");
    }


    private void populateListViewAllBooks() {
        adapter = new MyListAdapterAllBooks();

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
    }




    private void registerClickCallback() {
        ListView list = (ListView) myView.findViewById(R.id.listView);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View viewClicked,
                                    int position, long id) {


                CartiRepository clickedCartiRepository = myCartiRepositories_allBooks.get(position);
                m_pd.show();
                m_getBookTask =new GetBookTask(clickedCartiRepository.getId());
                m_getBookTask.execute((Void) null);

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
            mesaj.setObiect(MainActivity.m_user.getId());
            mesaj.setCmd(m_requestBooks);
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
                        myCartiRepositories_allBooks.add(new CartiRepository(carti[i].getID(),carti[i].getNume(),carti[i].getGen(),String.valueOf(carti[i].getNota()),carti[i].getAutor(),bmp));
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




    public class GetBookTask extends AsyncTask<Void, Void, Boolean> {
        private Carte carte;
        private Mesaj m_mesaj;
        GetBookTask(int nr) {
            carte=new Carte();
            carte.setID(nr);
        }
        @Override
        protected Boolean doInBackground(Void... params) {
            Mesaj mesaj=new Mesaj();
            mesaj.setObiect(carte);
            mesaj.setCmd(RequestEnum.REQUEST_FILE);
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
            m_getBookTask = null;
            if(success)
            {
                carte=(Carte)m_mesaj.getObiect();

                m_dialogCarte=new Dialog(myView.getContext());
                m_dialogCarte.setTitle("Carte");
                m_dialogCarte.setContentView(R.layout.book_layout);



                Bitmap bmp = BitmapFactory.decodeByteArray(carte.getImagine(), 0, carte.getImagine().length);

                Button btn_citeste=(Button)m_dialogCarte.findViewById(R.id.m_btn_citeste);
                Button btn_vreau=(Button)m_dialogCarte.findViewById(R.id.m_btn_vreau);
                Button btn_recomanda=(Button)m_dialogCarte.findViewById(R.id.m_btn_recomanda);

                ImageView cover=(ImageView)m_dialogCarte.findViewById(R.id.m_imagine_carte_view);


                cover.setImageBitmap(bmp);
                TextView text_autor=(TextView)m_dialogCarte.findViewById(R.id.m_autor_carte);
                TextView text_nume_carte=(TextView)m_dialogCarte.findViewById(R.id.m_nume_carte);
                TextView text_gen=(TextView)m_dialogCarte.findViewById(R.id.m_gen_carte);
                TextView text_descriere=(TextView)m_dialogCarte.findViewById(R.id.m_descriere_carte);
                FloatingActionButton editButton=(FloatingActionButton)m_dialogCarte.findViewById(R.id.floatingActionButton);
                FloatingActionButton addNota=(FloatingActionButton)m_dialogCarte.findViewById(R.id.floatingActionButton_setNota);
                ratingBar=(RatingBar)m_dialogCarte.findViewById(R.id.ratingBar3);
                ratingBar.setNumStars(3);
                ratingBar.setRating(carte.getNota());
                ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                    public void onRatingChanged(RatingBar ratingBar, float rating,
                                                boolean fromUser) {
                        notaUser=(carte.getNota()*carte.getNr_votanti()+rating)/(carte.getNr_votanti()+1);
                    }
                });
                if(carte.getId_utilizator()==MainActivity.m_user.getId())
                {
                    editButton.setVisibility(View.VISIBLE);
                    addNota.setVisibility(View.INVISIBLE);

                    editButton.setOnClickListener(new View.OnClickListener(){

                        @Override
                        public void onClick(View v) {
                            MainActivity activity = (MainActivity) getActivity();
                            m_dialogCarte.cancel();
                            activity.m_obiect=carte.getID();
                            activity.changeView(Views.EDIT_BOOKS);
                        }
                    });

                }
                else
                {
                    editButton.setVisibility(View.INVISIBLE);
                    addNota.setVisibility(View.VISIBLE);

                    addNota.setOnClickListener(new View.OnClickListener(){

                        @Override
                        public void onClick(View v) {
                            ratingBar.setRating(notaUser);
                            Carte newCarte=new Carte();
                            carte.setNr_votanti(carte.getNr_votanti()+1);
                            carte.setNota(notaUser);

                            newCarte.setID(carte.getID());
                            newCarte.setNr_votanti(carte.getNr_votanti());
                            newCarte.setNota(carte.getNota());
                            Mesaj mesaj=new Mesaj();
                            mesaj.setObiect(newCarte);
                            mesaj.setCmd(RequestEnum.REQUEST_ADD_NOTA);
                            mesaj= (Mesaj) ConnectionManager.getInstance().sendMessage(mesaj);
                        }
                    });
                }
                text_autor.setText(carte.getAutor());
                text_nume_carte.setText(carte.getNume());
                text_descriere.setText(carte.getDescriere());
                text_gen.setText(carte.getGen());


                btn_citeste.setOnClickListener(new View.OnClickListener(){

                    @Override
                    public void onClick(View v) {
                        Intent myIntent =new Intent(myView.getContext(),ReadActivity.class);
                        myIntent.putExtra("id_carte",carte.getID());
                        startActivity(myIntent);
                    }
                });

                m_dialogCarte.show();
            }
        }
        @Override
        protected void onCancelled() {
            m_getBookTask = null;
            m_pd.cancel();
        }
    }



}
