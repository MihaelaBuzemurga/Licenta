package WorkerClientThread;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Scanner;

import org.apache.commons.lang3.SerializationUtils;
import org.omg.CORBA.Request;

import com.example.gabriel.readerlish.Carte.Carte;
import com.example.gabriel.readerlish.Mesaj.Mesaj;
import com.example.gabriel.readerlish.Mesaj.RequestEnum;
import com.example.gabriel.readerlish.Nota.Nota;
import com.example.gabriel.readerlish.User.User;

import Database.ManagerDb;
import FileManager.FileManager;
import Reusable.*;
import SessionManager.SessionManager;
import TransformerBytes.TransformerBytes;

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
			
			System.out.println("Eroare");
			

		}
	}

	@Override
	public void run() {
		while (true) {
			TransformerBytes mesaj;
			byte[] initilize = new byte[1];
			try {
				din.read(initilize, 0, initilize.length);
				if (initilize[0] == 2) {
					byte[] recv_data = ReadStream();
					if (recv_data != null) {
						Mesaj newMesaj=null;
						Mesaj m_mesaj = (Mesaj) SerializationUtils.deserialize(recv_data);
						switch (m_mesaj.getCmd()) {
						case REQUEST_LOGIN:
							newMesaj=SessionManager.getSession().Login((User)m_mesaj.getObiect());
							 dout.write(TransformerBytes.getDataPacket(newMesaj));
							 dout.flush();
							break;
						case REQUEST_REGISTER:
							newMesaj=SessionManager.getSession().Register((User)m_mesaj.getObiect());
							 dout.write(TransformerBytes.getDataPacket(newMesaj));
							 dout.flush();
							break;
						case REQUEST_EDIT_PROFIL:
							newMesaj=SessionManager.getSession().EditUser((User) m_mesaj.getObiect());
							 dout.write(TransformerBytes.getDataPacket(newMesaj));
							 dout.flush();
							 break;
						case REQUEST_EDIT_PHOTO_PROFILE:
							newMesaj=SessionManager.getSession().EditUserPhoto((User) m_mesaj.getObiect());
							 dout.write(TransformerBytes.getDataPacket(newMesaj));
							 dout.flush();
							 break;
						case REQUEST_UPLOAD_FILE:
							newMesaj=FileManager.getInstance().uploadFile(m_mesaj.getObiect());
							dout.write(TransformerBytes.getDataPacket(newMesaj));
							 dout.flush();
							break;
						case REQUEST_EDIT_BOOK:
							newMesaj=FileManager.getInstance().uploadEditFile(m_mesaj.getObiect());
							dout.write(TransformerBytes.getDataPacket(newMesaj));
							 dout.flush();
							break;
						case REQUEST_EDIT_FILE:
							newMesaj=FileManager.getInstance().updateCarteFromUser((Carte)m_mesaj.getObiect());
							dout.write(TransformerBytes.getDataPacket(newMesaj));
							 dout.flush();
							break;
						case REQUEST_FILE:
							Mesaj mesajj=FileManager.getInstance().getFile((Carte)m_mesaj.getObiect());
							 dout.write(TransformerBytes.getDataPacket(mesajj));
							 dout.flush();
							break;
						case REQUEST_FILE_CONTENT:
							newMesaj=FileManager.getInstance().getContent((Carte)m_mesaj.getObiect());
							 dout.write(TransformerBytes.getDataPacket(newMesaj));
							 dout.flush();
							break;
						case REQUEST_BOOKS:
							newMesaj=FileManager.getInstance().getBooks(m_mesaj.getId());
							 dout.write(TransformerBytes.getDataPacket(newMesaj));
							 dout.flush();
							 break;
						case REQUEST_MY_BOOKS:
							newMesaj=FileManager.getInstance().getMyBooks(m_mesaj.getId(),(int)m_mesaj.getObiect());
							 dout.write(TransformerBytes.getDataPacket(newMesaj));
							 dout.flush();
							 break;
						case REQUEST_GRUP_FOR_SUBSCRIBE:
							newMesaj=FileManager.getInstance().getGrupForSubscribe();
							 dout.write(TransformerBytes.getDataPacket(newMesaj));
							 dout.flush();
							 break;
						case REQUEST_ADD_TO_GRUP:
							newMesaj=FileManager.getInstance().addToGrup(m_mesaj.getId());
							 dout.write(TransformerBytes.getDataPacket(newMesaj));
							 dout.flush();
							 break;
						case REQUEST_ADD_NOTA:
							newMesaj=FileManager.getInstance().addNota((Nota) m_mesaj.getObiect());
							 dout.write(TransformerBytes.getDataPacket(newMesaj));
							 dout.flush();
							 break;
						case REQUEST_GEN:
							newMesaj=FileManager.getInstance().requestGenuri();
							 dout.write(TransformerBytes.getDataPacket(newMesaj));
							 dout.flush();
							 break;
						default:
							break;
						}

					} else {
						System.out.println("Am primit null");
					}
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			break;
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
