package com.example.gabriel.readerlish;
import com.example.gabriel.readerlish.Carte.Carte;
import com.example.gabriel.readerlish.ConnectionManager.ConnectionManager;
import com.example.gabriel.readerlish.Mesaj.Mesaj;
import com.example.gabriel.readerlish.Mesaj.RequestEnum;
import com.example.gabriel.readerlish.User.User;

import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Gabriel on 18.01.2017.
 */

public class YourProfile extends Fragment{
    public static final int SELECT_IMAGE = 0;
    private LayoutInflater myInflater;
    ProgressDialog m_pd;
    private Dialog m_dateDialog;
    private Dialog m_pozaDialog;
    EditUserTask m_editUserTask;
    EditText m_nume;
    EditText m_prenume;
    EditText m_parola1;
    EditText m_parola2;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.out.println("acum si acum");


    }

    View myView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.your_profile,container,false);
        System.out.println("again");

        myInflater=inflater;
        setProgressDialog();
        populateView();
        registerCallbacks();
        return myView;


    }
    public ImageButton getImageButton()
    {
        ImageButton buton=(ImageButton)myView.findViewById(R.id.notificationButton);
        return buton;
    }
    private  void registerCallbacks()
    {
        Button incarca=(Button)myView.findViewById(R.id.btn_incarca_poza_profil);
        Button editeaza=(Button)myView.findViewById(R.id.btn_editeaza_date_profil);
        incarca.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);//
                startActivityForResult(Intent.createChooser(intent, "Select Picture"),SELECT_IMAGE);

            }
        });

        editeaza.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                m_dateDialog=new Dialog(myView.getContext());
                m_dateDialog.setTitle("Editeaza date");
                m_dateDialog.setContentView(R.layout.dialog_layout_editeaza_date);

                m_nume=(EditText)m_dateDialog.findViewById(R.id.nume_editeaza_profil);
                m_prenume=(EditText)m_dateDialog.findViewById(R.id.prenume_editeaza_profil);
                m_parola1=(EditText)m_dateDialog.findViewById(R.id.parola_editeaza_profil);
                m_parola2=(EditText)m_dateDialog.findViewById(R.id.parola2_editeaza_profil);

                m_nume.setText(MainActivity.m_user.getNume());
                m_prenume.setText(MainActivity.m_user.getPrenume());
                m_parola1.setText(MainActivity.m_user.getParola());
                m_parola2.setText(MainActivity.m_user.getParola());
                m_dateDialog.show();

                Button editeaza=(Button)m_dateDialog.findViewById(R.id.btn_editeaza_profil);
                editeaza.setOnClickListener(new View.OnClickListener(){

                    @Override
                    public void onClick(View v) {
                        if(!TextUtils.isEmpty(m_nume.getText().toString()))
                        {
                            MainActivity.m_user.setNume(m_nume.getText().toString());
                        }
                        if(!TextUtils.isEmpty(m_prenume.getText().toString())) {
                            MainActivity.m_user.setPrenume(m_prenume.getText().toString());
                        }
                        if(!TextUtils.isEmpty(m_parola1.getText().toString()) && !TextUtils.isEmpty(m_parola2.getText().toString())) {
                            if( m_parola1.getText().toString().compareTo(m_parola2.getText().toString())==0) {
                                MainActivity.m_user.setParola(m_parola1.getText().toString());
                            }
                            else
                            {
                                Toast.makeText(getActivity(), "Parole incorecte", Toast.LENGTH_SHORT).show();
                            }
                        }
                        m_dateDialog.cancel();
                        m_pd.show();
                        m_editUserTask =new EditUserTask(MainActivity.m_user,RequestEnum.REQUEST_EDIT_PROFIL);
                        m_editUserTask.execute((Void) null);

                    }
                });

            }
        });
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SELECT_IMAGE)
        {
            if (resultCode == Activity.RESULT_OK)
            {
                if (data != null)
                {
                    try
                    {

                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), data.getData());

                        ImageView cover=(ImageView)myView.findViewById(R.id.imageView3);
                        cover.setImageBitmap(bitmap);
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                        byte[] byteArray = stream.toByteArray();
                        MainActivity.m_user.setImagineProfil(byteArray);
                        m_pd.show();
                        m_editUserTask =new EditUserTask(MainActivity.m_user,RequestEnum.REQUEST_EDIT_PHOTO_PROFILE);
                        m_editUserTask.execute((Void) null);

                    } catch (IOException e)
                    {
                        e.printStackTrace();
                    }

                }
            } else if (resultCode == Activity.RESULT_CANCELED)
            {
                Toast.makeText(getActivity(), "Cancelled", Toast.LENGTH_SHORT).show();
            }
        }
    }
    private void setProgressDialog()
    {
        m_pd= new ProgressDialog(myView.getContext());
        m_pd.setTitle("Incarcare!");

        m_pd.setMessage("Asteapta");
    }

    private void populateView()
    {

        EditText nume=(EditText)myView.findViewById(R.id.nume_profil);
        EditText prenume=(EditText)myView.findViewById(R.id.prenume_profil);
        EditText parola=(EditText)myView.findViewById(R.id.parola_profil);
        ImageView imagine=(ImageView)myView.findViewById(R.id.imageView3);

        if(MainActivity.m_user.getImagineProfil()!=null)
        {
            Bitmap bmp = BitmapFactory.decodeByteArray(MainActivity.m_user.getImagineProfil(), 0, MainActivity.m_user.getImagineProfil().length);
            imagine.setImageBitmap(bmp);
        }
        nume.setText(MainActivity.m_user.getNume());
        prenume.setText(MainActivity.m_user.getPrenume());
        parola.setText(MainActivity.m_user.getParola());

    }


    public class EditUserTask extends AsyncTask<Void, Void, Boolean> {
        private User user;
        private Mesaj m_mesaj;
        RequestEnum request;
        EditUserTask(User user,RequestEnum re) {
            this.user=user;
            request=re;
        }
        @Override
        protected Boolean doInBackground(Void... params) {
            Mesaj mesaj=new Mesaj();
            mesaj.setObiect(user);
            mesaj.setCmd(request);
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
            m_editUserTask = null;
            if(success)
            {
                user=(User)m_mesaj.getObiect();
                populateView();


            }
        }
        @Override
        protected void onCancelled() {
            m_editUserTask = null;
            m_pd.cancel();
        }
    }


}
