/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

/**
 *
 * @author Mihaela Buzemurga
 */
public class Client {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        // TODO code application logic here
        Client obj = new Client();
        Socket obj_client = new Socket(InetAddress.getByName("192.168.0.11"), 9090);
        DataInputStream din = new DataInputStream(obj_client.getInputStream());
        DataOutputStream dout = new DataOutputStream(obj_client.getOutputStream());
        Scanner mesaj= new Scanner(System.in);
      
        while (true){
            String Msg= mesaj.nextLine();
            dout.write(Msg.getBytes());
            byte[] cmd_buff = ReadStream(din);
           // din.read(cmd_buff, 0, cmd_buff.length);
           //FileUtils.writeByteArrayToFile(new File("Fisier_Creat"),cmd_buff);
           FileOutputStream fisier=new FileOutputStream("ceva.gif");
           fisier.write(cmd_buff);
           fisier.close();
            System.out.println(new String(cmd_buff));
    
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
