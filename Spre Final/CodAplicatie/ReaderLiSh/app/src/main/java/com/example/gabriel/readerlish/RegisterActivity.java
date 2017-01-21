package com.example.gabriel.readerlish;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
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

public class RegisterActivity extends AppCompatActivity {

    EditText numeUtilizator;
    EditText parola;
    EditText parola1;
    ProgressDialog m_pd;
    UserRegisterTask m_task;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        m_pd= new ProgressDialog(this);
        m_pd.setTitle("Inregistrare!");
        m_pd.setMessage("Asteapta");

        Intent myIntent = getIntent();
        User m_user =(User)myIntent.getSerializableExtra("user");
        if(m_user != null){
            numeUtilizator = (EditText) findViewById(R.id.nume);
            numeUtilizator.setText(m_user.getNume_utilizator());

            parola=(EditText)findViewById(R.id.parola);
            parola.setText(m_user.getParola());

            parola1=(EditText) findViewById(R.id.parola1);
            parola1.setText(m_user.getParola());

        }
        numeUtilizator=(EditText) findViewById(R.id.nume);
        parola=(EditText)findViewById(R.id.parola);
        parola1=(EditText) findViewById(R.id.parola1);
    }

    public void pressRegister(View view) {
        if(verifyNameAndPassword() && verifyMatchPassword())
        {
            m_pd.show();
            m_task=new UserRegisterTask(numeUtilizator.getText().toString(),parola.getText().toString());
            m_task.execute((Void) null);
        }
    }
    public boolean verifyMatchPassword()
    {
        boolean isOk=true;
        View focus=null;

        if(!TextUtils.equals(parola.getText().toString(),parola1.getText().toString()))
        {
            isOk=false;
            parola.setText("");
            parola1.setText("");
            parola1.setError("Parolele nu se potrivesc");
            focus=parola;

        }
        return isOk;
    }
    public boolean verifyNameAndPassword()
    {
        View focus=null;
        boolean isOk=true;
        if(TextUtils.isEmpty(numeUtilizator.getText().toString()) && isOk)
        {
            focus=numeUtilizator;
            numeUtilizator.setError("Nume gol");
            isOk=false;
        }
        if(TextUtils.isEmpty(parola.getText().toString()) && isOk)
        {
            focus=parola;
            parola.setError("Parola goala");
            isOk=false;
        }
        if(TextUtils.isEmpty(parola1.getText().toString()) && isOk) {
            focus = parola1;
            parola1.setError("Parola goala");
            isOk = false;
        }
        if(!isOk)
        {
            focus.requestFocus();
        }
        return isOk;
    }
    public class UserRegisterTask extends AsyncTask<Void, Void, Boolean>
    {
        private final String mUsername;
        private final String mPassword;
        private  User m_user;

        UserRegisterTask(String email, String password) {
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
            mesaj.setCmd(RequestEnum.REQUEST_REGISTER);
            Mesaj mesajj= (Mesaj) ConnectionManager.getInstance().sendMessage(mesaj);
            if(mesajj!=null) {
                m_user = (User) mesajj.getObiect();
                if (mesajj.getM_raspunsServer() == RespondeEnum.REGISTER_FAIL) {
                    return false;
                } else {
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
            m_pd.cancel();
            m_task = null;
            if(success)
            {
                finish();
                Intent myIntent =new Intent(RegisterActivity.this,MainActivity.class);
                myIntent.putExtra("user",m_user);
                startActivity(myIntent);
            }
            else
            {
                numeUtilizator.setError("Numele exista!");
            }
        }

        @Override
        protected void onCancelled() {
            m_task = null;
            m_pd.cancel();

        }
    }
}
