package com.example.mihaelabuzemurga.licenta;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.example.mihaelabuzemurga.licenta.ConnectionManager.ConnectionManager;
import com.example.mihaelabuzemurga.licenta.Mesaj.Mesaj;
import com.example.mihaelabuzemurga.licenta.Mesaj.RequestEnum;
import com.example.mihaelabuzemurga.licenta.Mesaj.RespondeEnum;
import com.example.mihaelabuzemurga.licenta.User.User;

public class MainActivity extends AppCompatActivity {
    EditText m_nume;
    EditText m_parola;
    UserLoginTask mAuthTask;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.autentificare);
        m_nume=(EditText) findViewById(R.id.editText);
        m_parola=(EditText) findViewById(R.id.editText2);

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
            mAuthTask=new UserLoginTask(m_nume.getText().toString(),m_parola.getText().toString());
            mAuthTask.execute((Void) null);
        }

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
                ConnectionManager.getInstance().connectToServer();
                Mesaj mesajj= (Mesaj)ConnectionManager.getInstance().sendMessage(mesaj);
                m_user=(User)mesajj.getObiect();
                if(mesajj.getM_raspunsServer()== RespondeEnum.LOGIN_FAIL)
                {
                    return false;
                }
                else {
                    return true;
                }

        }
        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            if(success)
            {
                finish();
                Intent myIntent =new Intent(MainActivity.this,MainPage.class);
                startActivity(myIntent);
            }
            else
            {
                EditText parola=(EditText) findViewById(R.id.editText2);
                parola.setError("Parola");
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;

        }



    }
}
