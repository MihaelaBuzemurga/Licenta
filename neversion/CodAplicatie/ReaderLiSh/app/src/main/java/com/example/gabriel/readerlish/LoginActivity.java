package com.example.gabriel.readerlish;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.example.gabriel.readerlish.ConnectionManager.ConnectionManager;
import com.example.gabriel.readerlish.Mesaj.Mesaj;
import com.example.gabriel.readerlish.Mesaj.RequestEnum;
import com.example.gabriel.readerlish.Mesaj.RespondeEnum;
import com.example.gabriel.readerlish.User.User;

public class LoginActivity extends AppCompatActivity {
    EditText m_nume;
    EditText m_parola;
    UserLoginTask mAuthTask;
    User m_user;
    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
    ProgressDialog m_progresialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        StrictMode.setThreadPolicy(policy);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ConnectionManager.getInstance().setContext(this);
        ConnectionManager.getInstance().connectToServer();

        m_progresialog = new ProgressDialog(this);
        m_progresialog.setTitle("Logare!");
        m_progresialog.setMessage("Asteapta");


        Intent myIntent = getIntent();
        User m_user =(User)myIntent.getSerializableExtra("user");
        if(m_user != null){
            m_nume = (EditText) findViewById(R.id.m_nume);
            m_parola = (EditText) findViewById(R.id.m_parola);
            m_nume.setText(m_user.getNume_utilizator());
            m_parola.setText(m_user.getParola());
        }

        m_nume=(EditText) findViewById(R.id.m_nume);
        m_parola=(EditText) findViewById(R.id.m_parola);
    }

    public void pressLogin(View view) {
        View focus=null;
        boolean isOk=true;

        if(TextUtils.isEmpty(m_nume.getText().toString()) || TextUtils.isEmpty(m_parola.getText().toString()))
        {
            focus=m_nume;
            isOk=false;
        }
        if(!isOk)
        {
            focus.requestFocus();
        }
        else
        {
            m_progresialog.show();
            mAuthTask=new UserLoginTask(m_nume.getText().toString(),m_parola.getText().toString());
            mAuthTask.execute((Void) null);
        }
    }

    public void pressRegister(View view) {
        Intent myIntent=new Intent(LoginActivity.this,RegisterActivity.class);
        startActivity(myIntent);
    }


    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mUsername;
        private final String mPassword;
        private  User m_user;

        UserLoginTask(String email, String password) {
            mUsername = email;
            mPassword = password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            User user = new User();
            Mesaj mesaj = new Mesaj();
            user.setNume_utilizator(mUsername);
            user.setParola(mPassword);
            user.setId(0);
            mesaj.setObiect(user);
            mesaj.setCmd(RequestEnum.REQUEST_LOGIN);
            Mesaj mesajj= (Mesaj)ConnectionManager.getInstance().sendMessage(mesaj);
            if(mesajj!=null)
            {

                m_user = (User) mesajj.getObiect();
                if (mesajj.getM_raspunsServer() == RespondeEnum.LOGIN_FAIL)
                {
                    return false;
                } else
                {
                    return true;
                }
            }
            else
            {
                return false;
            }

        }
        @Override
        protected void onPostExecute(final Boolean success) {
            m_progresialog.cancel();
            mAuthTask = null;
            if(success)
            {
                finish();
                Intent myIntent =new Intent(LoginActivity.this,MainActivity.class);
                myIntent.putExtra("user",m_user);
                startActivity(myIntent);
            }
            else
            {
                EditText parola=(EditText) findViewById(R.id.m_parola);
                parola.setError("Parola Incorecta");
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            m_progresialog.cancel();

        }



    }
}
