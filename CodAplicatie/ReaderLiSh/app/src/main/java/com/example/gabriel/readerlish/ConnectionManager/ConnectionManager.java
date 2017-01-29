package com.example.gabriel.readerlish.ConnectionManager;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.example.gabriel.readerlish.Mesaj.Mesaj;
import com.example.gabriel.readerlish.Reusable.ServerInfo;
import com.example.gabriel.readerlish.TransformerBytes.TransformerBytes;

import org.apache.commons.lang3.SerializationUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

/**
 * Created by Gabriel on 18.01.2017.
 */

public class ConnectionManager {

    public String serverIpAddress;
    public InetAddress serverAddr;
    public int port;
    public SocketAddress addr;
    boolean isConnected;
    static Context m_context;
    public Socket socket;
    OutputStream out= null;
    InputStream din=null;
    private static ConnectionManager instance = null;
    private ConnectionManager(){
        try{
            isConnected=false;
            serverIpAddress = ServerInfo.IP;
            serverAddr = InetAddress.getByName(serverIpAddress);
            addr = new InetSocketAddress(serverAddr, ServerInfo.PORT);
            socket = new Socket();
        } catch (Exception e) {
            Log.d("ConnManager()",e.toString());
        }
    }
    public static ConnectionManager getInstance(){
        if(instance == null) {
            instance = new ConnectionManager();
        }
        return instance;
    }
    public void setContext(Context context)
    {
        m_context=context;
    }
    private static boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) m_context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
    public boolean connectToServer(){
        if(!isConnected && isNetworkAvailable()) {
            try {
                Log.d("Connecting to socket", "c");
                socket.setReceiveBufferSize(65536);
                socket.connect(addr, 3000);
                out = socket.getOutputStream();
                din = socket.getInputStream();
                isConnected = true;
                return true;
            } catch (Exception e) {
                isConnected = false;
                Log.d("connectToServer", e.toString());
                if (e.getMessage().equals("Already connected")) {
                    return true;
                }
                return false;
            }
        }
        return true;
    }
    public boolean disconnectFromSesrver(){
        try {
            Log.d("Connecting to socket","c");
            socket.close();
            socket = new Socket();
            return true;
        } catch (Exception e) {
            Log.d("connectToServer",e.toString());
            if(e.getMessage().equals("Already connected")){
                return true;
            }
            return false;
        }
    }
    public void closeConnectionToServer(){
        try{
            socket.close();
            isConnected = false;
        }
        catch (Exception e){
            Log.d("closeSocket",e.toString());
        }
    }

    public Object sendMessage(Object obiect)
    {
        Mesaj mesaj=null;
        if(isNetworkAvailable() && isConnected) {

            boolean test = false;
            try {
                out.write(TransformerBytes.getDataPacket(obiect));
                mesaj = readMesage();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return mesaj;
    }

    private Mesaj readMesage()
    {
        Mesaj mesaj=null;
        byte[] initilize = new byte[1];
        try {
            din.read(initilize, 0, initilize.length);
            if (initilize[0] == 2) {
                byte[] recv_data = ReadStream();
                mesaj = (Mesaj) SerializationUtils.deserialize(recv_data);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return mesaj;
    }
    private byte[] ReadStream() {
        byte[] data_buff = null;
        try {
            int b = 0;
            String buff_length = "";
            while ((b = din.read()) != 4) {
                buff_length += (char) b;
            }
            int data_length = Integer.parseInt(buff_length);
            data_buff = new byte[Integer.parseInt(buff_length)];
            int byte_read = 0;
            int byte_offset = 0;
            while (byte_offset < data_length) {
                byte_read = din.read(data_buff, byte_offset, data_length - byte_offset);
                byte_offset += byte_read;
            }
        } catch (IOException ex) {

        }
        return data_buff;
    }

}