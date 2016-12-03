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
import Reusable.*;
import User.User;

public class WorkerClientThread implements Runnable {
	private Socket target_socket;
	private DataInputStream din;
	private DataOutputStream dout;
	private String line;
	private String nume;
	Scanner s = new Scanner(System.in);
	ManagerDb managerDb=null;
	public WorkerClientThread(Socket recv_socket) {
		try {
			System.out.println("Connectat");
			managerDb= new ManagerDb();
			target_socket = recv_socket;
			din = new DataInputStream(target_socket.getInputStream());
			dout = new DataOutputStream(target_socket.getOutputStream());
		} catch (IOException ex) {

		}
	}

	@Override
	public void run() {
		while (true) {
			System.out.println("ceva");
			byte[] cmd_buff = ReadStream();
			System.out.println(cmd_buff);
			//String parameter=getNextParameter(cmd_buff);
			//System.out.println(parameter);
			ReqeustEnum cmd = ReqeustEnum.values()[1];
			switch (cmd) {
			case REQUEST_LOGIN:
				Login(cmd_buff);
				break;
			case REQUEST_REGISTER:
				break;

			default:
				break;
			}
			
		}

	}

	private void Login(byte [] buffer ){
		System.out.println("Start Login");
		User utilizator= new User();

		String str=null;
		try {
			str = new String(buffer, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		System.out.println(str);
		utilizator.setNume_utilizator("ogabi");
		utilizator.setParola("vasile");
		System.out.println(utilizator.getNume_utilizator());
		System.out.println(utilizator.getParola());
		managerDb.Logare(utilizator);
		if(utilizator.getNume()!=null)
		{
			System.out.println("logat");
		}
	}
	private String getNextParameter(byte [] buffer)
	{
		String return_string=null;
		int i=0;
		int b=0;
		while((b=buffer[i])!=4)
		{
			i++;
			return_string+=(char)b;
		}
		buffer=Arrays.copyOfRange(buffer,return_string.length()+1, buffer.length);
		return return_string;
	}
 
	private byte[] ReadStream() {
		byte[] data_buff = null;
		try {
			int b = 0;
			String buff_length = "";
			while ((b = din.read()) != 4) {
				System.out.println("Dimensiunea");
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
		}
		return data_buff;
	}

	private byte[] CreateDataPacket(byte[] cmd, byte[] data) {
		byte[] packet = null;
		try {

			byte[] data_length = String.valueOf(data.length).getBytes("UTF8");
			byte[] separator = new byte[1];
			separator[0] = 4;
			packet = new byte[data_length.length + data.length + separator.length];
			System.arraycopy(data_length, 0, packet, 0, data_length.length);
			System.arraycopy(separator, 0, packet, data_length.length, separator.length);
			System.arraycopy(data, 0, packet, data_length.length + separator.length, data.length);

		} catch (UnsupportedEncodingException ex) {

		}
		return packet;
	}

}
