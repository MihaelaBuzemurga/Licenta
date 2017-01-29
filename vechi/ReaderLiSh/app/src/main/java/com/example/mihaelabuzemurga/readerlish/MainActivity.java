package com.example.mihaelabuzemurga.readerlish;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.Socket;

public class MainActivity extends AppCompatActivity {

    DataInputStream din;
    DataOutputStream dout;
    Socket socket;
    private boolean connected = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.login)
    }
    public void ConnectToServer() {
        System.out.println("rrrrrrrrrrrrrrrrrr");

            Thread cThread = new Thread(new ClientThread());
            cThread.start();

    }
    public class ClientThread implements Runnable {

        @Override
        public void run() {
            try {
                InetAddress serverAddr = InetAddress.getByName("192.168.0.11");
                System.out.print("PP");
                socket = new Socket(serverAddr, 9091);
                System.out.print("CONNECT");
                connected = true;
                System.out.print("CONNECT2");

                    try {
                        System.out.print("SATART OUT");
                        dout = new DataOutputStream(socket.getOutputStream());
                        System.out.print("SATART pREPARE");


                        byte[] buff=CreateDataPacket(String.valueOf(1).getBytes(),"ogabi|vasile".getBytes());
                        System.out.print(buff);
                        try {
                            dout.write(buff);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    } catch (Exception e) {

                    }



            } catch (Exception e) {

                connected = false;
            }
        }

    }
    private byte[] CreateDataPacket(byte[] cmd, byte[] data) {
        byte[] packet = null;


        byte[] data_length = new byte[0];
        try {
            data_length = String.valueOf(data.length).getBytes("UTF8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        byte[] separator = new byte[1];
            separator[0] = 4;
            packet = new byte[data_length.length + data.length + separator.length];
            System.arraycopy(data_length, 0, packet, 0, data_length.length);
            System.arraycopy(separator, 0, packet, data_length.length, separator.length);
            System.arraycopy(data, 0, packet, data_length.length + separator.length, data.length);

        return packet;
    }
    public void Log(View view) {
            System.out.println("Button");
        ConnectToServer();





    }
}
