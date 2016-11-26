package com.example.mihaelabuzemurga.licenta;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.logging.Handler;

public class MainActivity extends AppCompatActivity {

    Socket obj_client;
    DataInputStream din;
    DataOutputStream dout;
    private boolean connected = false;
    EditText msg;
    EditText msg2;
    Socket socket;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

         msg=(EditText)findViewById(R.id.editText);
         msg2=(EditText)findViewById(R.id.editText2);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    public void send(View view) {

        try {
            DataInputStream din = new DataInputStream(socket.getInputStream());
            PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
            out.println(msg.getText().toString());
            System.out.print("Trimis=" + msg.getText().toString() + "\n");
            msg.setText("");


        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public class Worker implements Runnable {
        @Override
        public void run() {
            while(connected) {
                DataInputStream din = null;
                try {
                    din = new DataInputStream(socket.getInputStream());
                    System.out.println("asteptam");
                    byte[] cmd_buff = new byte[100];
                    din.read(cmd_buff, 0, cmd_buff.length);
                    final String mm = new String(cmd_buff);
                    System.out.println(mm);
                    System.out.println("am citit");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            msg2.append(mm + "\n");
                        }
                    });


                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    }
    public void ConnectToServer(View view) {
        if (!connected) {

                Thread cThread = new Thread(new ClientThread());
                cThread.start();
           while(!connected){};
            Thread ct2 = new Thread(new Worker());
            ct2.start();


        }
    }
    public class ClientThread implements Runnable {

        @Override
        public void run() {
            try {
                InetAddress serverAddr = InetAddress.getByName("192.168.0.11");

                socket = new Socket(serverAddr, 9090);
                connected = true;
                while (connected) {
                    try {
//                        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
//                        System.out.println("asteptam");
//                        String line=in.readLine();
//                        System.out.println("am citit");
//                        msg2.append(line+"\n");


                    } catch (Exception e) {

                    }
                }
                socket.close();

            } catch (Exception e) {

                connected = false;
            }
        }

    }

}