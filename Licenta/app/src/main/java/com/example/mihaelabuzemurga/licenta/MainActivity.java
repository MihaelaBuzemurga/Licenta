package com.example.mihaelabuzemurga.licenta;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

public class MainActivity extends AppCompatActivity {

    Socket obj_client;
    DataInputStream din;
    DataOutputStream dout;
    private boolean connected = false;
    EditText msg;
    EditText msg2;
    Socket socket;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        verifyStoragePermissions((Activity)this);
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
//                    System.out.println("asteptam");
//                    byte[] cmd_buff = new byte[100];
//                    din.read(cmd_buff, 0, cmd_buff.length);
//                    final String mm = new String(cmd_buff);
//                    System.out.println(mm);
//                    System.out.println("am citit");
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            msg2.append(mm + "\n");
//                        }
//                    });
                    byte[] cmd_buff = ReadStream(din);
                    File file= new File (Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM),"/file.txt");
                    file.createNewFile();
                    FileOutputStream fisier=new FileOutputStream(file.getPath());
                    fisier.write(cmd_buff);
                    fisier.close();
                    System.out.println("terminat");


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
    public static void verifyStoragePermissions(Activity activity) {
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
    }

    static private byte[] ReadStream(DataInputStream din) {
        byte[] data_buff = null;
        try {
            int b = 0;
            String buff_length = "";
            System.out.print("ceva");
            while ((b = din.read()) != 4) {
                buff_length += (char) b;
            }
            int data_length = Integer.parseInt(buff_length);
            data_buff = new byte[Integer.parseInt(buff_length)];
            System.out.println(buff_length);
            int byte_read = 0;
            int byte_offset = 0;
            while (byte_offset < data_length) {
                byte_read = din.read(data_buff, byte_offset, data_length - byte_offset);
                System.out.println(byte_read);
                byte_offset += byte_read;
            }
        } catch (IOException ex) {
            // Logger.getLogger(TCPDataClient.class.getName()).log(Level.SEVERE, null, ex);
        }
        return data_buff;
    }
}