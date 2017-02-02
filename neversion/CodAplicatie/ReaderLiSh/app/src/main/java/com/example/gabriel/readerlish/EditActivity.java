package com.example.gabriel.readerlish;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gabriel.readerlish.Carte.Carte;
import com.example.gabriel.readerlish.ConnectionManager.ConnectionManager;
import com.example.gabriel.readerlish.Mesaj.Mesaj;
import com.example.gabriel.readerlish.Mesaj.RequestEnum;
import com.example.gabriel.readerlish.Reusable.Views;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class EditActivity extends Fragment {
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    private static final int SELECT_IMAGE = 2;
    View myView;
    Spinner sItems;
    EditText m_numeFisier;
    EditText m_autor;
    EditText m_descriere;
    ImageView m_Poza;
    Button m_btn_incarca;
    Button m_btn_incarca_poza;
    Map<String,Integer> m_genuri;
    ProgressDialog m_progresialog;
    Button m_buton_fisier;
    Bitmap bitmap;
    File pdf_file;
    UploadCarteTask uploadTaskCarte;
    GetBookTask m_getBookTask;
    MainActivity m_activity;
    int m_idCarte;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.upload_book,container,false);

        m_activity=(MainActivity)getActivity();
        setupProgressDialog();
        getItemFromView();
        getGen();
        getBook();
        populateSpiner();
        createCallbackButtons();

        return myView;
    }
    private void setupProgressDialog()
    {
        m_progresialog = new ProgressDialog(myView.getContext());
        m_progresialog.setTitle("Incarcare!");
        m_progresialog.setMessage("Asteapta");
    }
    private void getBook()
    {
        m_idCarte=(int)m_activity.m_obiect;
        m_progresialog.show();
        m_getBookTask =new GetBookTask(m_idCarte);
        m_getBookTask.execute((Void) null);
    }
    private void getItemFromView()
    {
        m_numeFisier=(EditText)myView.findViewById(R.id.m_numeFisier);
        m_autor=(EditText)myView.findViewById(R.id.m_autorFisier);
        m_descriere=(EditText)myView.findViewById(R.id.m_descriereFisier);
        m_Poza=(ImageView) myView.findViewById(R.id.m_imageFile);
        m_btn_incarca=(Button)myView.findViewById(R.id.btn_incarcaFisier);
        m_btn_incarca_poza=(Button)myView.findViewById(R.id.m_btn_incarcaPozaFisier);
    }
    private void getGen()
    {
        Mesaj mesaj=new Mesaj();
        mesaj.setCmd(RequestEnum.REQUEST_GEN);
        mesaj= (Mesaj) ConnectionManager.getInstance().sendMessage(mesaj);
        m_genuri=(Map<String,Integer>)mesaj.getObiect();

    }

    private void createCallbackButtons()
    {

        m_buton_fisier=(Button)myView.findViewById(R.id.open_file_manager);
        m_buton_fisier.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("application/pdf");
                startActivityForResult(intent,0);
            }
        });



        m_btn_incarca.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                boolean ok=true;
                View focus=null;
                if(m_numeFisier.getText().toString().isEmpty() && ok)
                {
                    ok=false;
                    Toast.makeText(getActivity(), "Incarcata Fisier", Toast.LENGTH_SHORT).show();
                }
                if(m_autor.getText().toString().isEmpty() && ok)
                {
                    ok=false;
                    Toast.makeText(getActivity(), "Completeaza autor", Toast.LENGTH_SHORT).show();
                }
                if(m_descriere.getText().toString().isEmpty() && ok)
                {
                    ok=false;
                    Toast.makeText(getActivity(), "Completeaza descrierea", Toast.LENGTH_SHORT).show();
                }
                if(bitmap==null && ok)
                {
                    ok=false;
                    Toast.makeText(getActivity(), "Introdu poza", Toast.LENGTH_SHORT).show();
                }

                if(ok)
                {
                    Toast.makeText(getActivity(), "Bravo", Toast.LENGTH_SHORT).show();
                    uploadCarte();
                }

            }
        });

        m_btn_incarca_poza.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);//
                startActivityForResult(Intent.createChooser(intent, "Select Picture"),SELECT_IMAGE);

            }
        });


    }
    private void uploadCarte()
    {
        if(verifyStoragePermissions((Activity) myView.getContext())) {
            Carte carte = new Carte();
            carte.setID(m_idCarte);
            carte.setNume(m_numeFisier.getText().toString());
            carte.setAutor(m_autor.getText().toString());
            carte.setDescriere(m_descriere.getText().toString());
            carte.setId_utilizator(MainActivity.m_user.getId());
            carte.setId_gen(m_genuri.get(sItems.getSelectedItem().toString()));
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] img_byteArray = stream.toByteArray();
            if(pdf_file!=null)
            {
                byte[] pdf_bytesArray = new byte[(int) pdf_file.length()];
                FileInputStream fis = null;
                try {
                    fis = new FileInputStream(pdf_file);
                    fis.read(pdf_bytesArray); //read file into bytes[]
                    fis.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                carte.setContinut(pdf_bytesArray);
            }
            carte.setImagine(img_byteArray);
            m_progresialog.show();
            uploadTaskCarte = new UploadCarteTask(carte);
            uploadTaskCarte.execute((Void) null);
        }


    }
    public static boolean verifyStoragePermissions(Activity activity) {

        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
        return true;
    }
    private void populateSpiner()
    {
        List<String> spinnerArray =  new ArrayList<String>();
        for(Map.Entry m:m_genuri.entrySet()){
            spinnerArray.add(m.getKey().toString());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(myView.getContext(), R.layout.support_simple_spinner_dropdown_item, spinnerArray);
        sItems = (Spinner)myView.findViewById(R.id.spinner);
        sItems.setAdapter(adapter);

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

                        bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), data.getData());
                        ImageView cover=(ImageView)myView.findViewById(R.id.m_imageFile);
                        cover.setImageBitmap(bitmap);

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
        if (requestCode == 0)
        {
            if (resultCode == Activity.RESULT_OK)
            {
                if (data != null)
                {

                    Uri uri = data.getData();

                    String path = null;
                    try {
                        path = getPath(myView.getContext(), uri);
                    } catch (URISyntaxException e) {
                        e.printStackTrace();
                    }
                    m_buton_fisier.setText("OK");
                    pdf_file = new File(path);
                    Toast.makeText(getActivity(), pdf_file.getName(), Toast.LENGTH_SHORT).show();


                }
            } else if (resultCode == Activity.RESULT_CANCELED)
            {
                Toast.makeText(getActivity(), "Cancelled", Toast.LENGTH_SHORT).show();
            }
        }
    }
    public static String getPath(Context context, Uri uri) throws URISyntaxException {
        if ("content".equalsIgnoreCase(uri.getScheme())) {
            String[] projection = { "_data" };
            Cursor cursor = null;

            try {
                cursor = context.getContentResolver().query(uri, projection, null, null, null);
                int column_index = cursor.getColumnIndexOrThrow("_data");
                if (cursor.moveToFirst()) {
                    return cursor.getString(column_index);
                }
            } catch (Exception e) {
                // Eat it
            }
        }
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }


    public class UploadCarteTask extends AsyncTask<Void, Void, Boolean> {

        private final Carte carte;


        UploadCarteTask(Carte carte) {
            this.carte=carte;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            Mesaj mesaj=new Mesaj();
            mesaj.setCmd(RequestEnum.REQUEST_EDIT_BOOK);
            mesaj.setObiect(carte);
            Mesaj mesajj= (Mesaj)ConnectionManager.getInstance().sendMessage(mesaj);
            m_progresialog.cancel();
            if(mesajj!=null)
            {

                return true;
            }
            else
            {
                return false;
            }

        }
        @Override
        protected void onPostExecute(final Boolean success) {
            m_progresialog.cancel();
            uploadTaskCarte = null;
            if(success)
            {
                Toast.makeText(getActivity(), "Editata cu succes", Toast.LENGTH_SHORT).show();
            }
            else
            {
                Toast.makeText(getActivity(), "Incarcarea a esuat", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected void onCancelled() {
            uploadTaskCarte = null;
            m_progresialog.cancel();

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
            m_progresialog.cancel();
            m_getBookTask = null;
            if(success)
            {
                carte=(Carte)m_mesaj.getObiect();
                m_numeFisier.setText(carte.getNume());
                m_autor.setText(carte.getAutor());
                m_descriere.setText(carte.getDescriere());
                bitmap = BitmapFactory.decodeByteArray(carte.getImagine(), 0, carte.getImagine().length);
                m_Poza.setImageBitmap(bitmap);
                sItems.setSelection(getNumberGen(carte.getGen()));
                m_buton_fisier.setText("OK");

            }
        }
        @Override
        protected void onCancelled() {
            m_getBookTask = null;
            m_progresialog.cancel();
        }
    }

    private int getNumberGen(String nume)
    {
        int nr=-1;
        for(Map.Entry m:m_genuri.entrySet()){
            nr++;
            String aux=(String)m.getKey();
            if((aux.compareTo(nume)==0))
            {
                break;
            }
        }
        return nr;
    }
}

