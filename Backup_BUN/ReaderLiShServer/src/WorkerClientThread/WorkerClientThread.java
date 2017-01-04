package WorkerClientThread;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Scanner;

import org.omg.CORBA.Request;

import Database.ManagerDb;
import Message.Message;
import Reusable.*;
import SessionManager.SessionManager;
import User.User;

public class WorkerClientThread implements Runnable {
	private Socket target_socket;
	private DataInputStream din;
	private DataOutputStream dout;
	private String line;
	private String nume;
	Scanner s = new Scanner(System.in);
	ManagerDb managerDb = null;

	public WorkerClientThread(Socket recv_socket) {
		try {
			System.out.println("Connectat");
			target_socket = recv_socket;
			din = new DataInputStream(target_socket.getInputStream());
			dout = new DataOutputStream(target_socket.getOutputStream());
		} catch (IOException ex) {

		}
	}

	@Override
	public void run() {
		while (true) {
			Message mesaj;
			byte[] initilize = new byte[1];
			try {
				din.read(initilize, 0, initilize.length);
				if (initilize[0] == 2) {
					byte[] cmd_buff = new byte[3];
					din.read(cmd_buff, 0, cmd_buff.length);
					System.out.println("cmd="+Integer.parseInt(new String(cmd_buff)));
					byte[] recv_data = ReadStream();
					Message message=new Message(recv_data);
					ReqeustEnum cmd = ReqeustEnum.fromId(Integer.parseInt(new String(cmd_buff)));
					switch (cmd) {
					case REQUEST_LOGIN:
						 mesaj=SessionManager.getSession().Login(message);
						dout.write(mesaj.getDataPacket("101".getBytes("UTF8")));
						dout.flush();
						break;
					case REQUEST_REGISTER:
						 mesaj=SessionManager.getSession().Register(message);
						dout.write(mesaj.getDataPacket("101".getBytes("UTF8")));
						dout.flush();
						break;
					case REQUEST_UPLOAD_FILE:
						 mesaj=SessionManager.getSession().Register(message);
						dout.write(mesaj.getDataPacket("101".getBytes("UTF8")));
						dout.flush();
						break;
					default:
						break;
					}
				}

			} catch (IOException ex) {

			}

		}

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
