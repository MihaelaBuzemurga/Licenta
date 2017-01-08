package Message;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;

import org.apache.commons.lang3.*;

public class Message {
	private byte[] packet;
	private byte[] continut;

	public Message() {
		setPacket(null);
		continut=null;
	};

	public Message(byte[] buffer) {
		this.setPacket(buffer);
	}

	public void setBuffer(byte[] buffer) {
		this.setPacket(buffer);
	}

	public String getNextParameter() {
		String return_string = "";
		int i = 0;
		int b = 0;
		while ((b = getPacket()[i]) != 4) {
			i++;
			return_string += (char) b;
		}
		setPacket(Arrays.copyOfRange(getPacket(), return_string.length() + 1, getPacket().length));
		return return_string;
	}
	public byte[] getNextParameterBytes(int nr) {
		byte[] return_string =null;
		
		return_string = Arrays.copyOfRange(getPacket(), 0, nr);
		setPacket(Arrays.copyOfRange(getPacket(), nr + 1, getPacket().length));
		return return_string;
	}

	public byte[] getDataPacket(byte[] cmd) {
		
		try {
			byte[] initialize = new byte[1];
			initialize[0] = 2;
			byte[] separator = new byte[1];
			separator[0] = 4;
			byte[] data_length = String.valueOf(continut.length).getBytes("UTF8");
			setPacket(new byte[initialize.length + cmd.length + separator.length + data_length.length + continut.length]);

			System.arraycopy(initialize, 0, getPacket(), 0, initialize.length);
			System.arraycopy(cmd, 0, getPacket(), initialize.length, cmd.length);
			System.arraycopy(data_length, 0, getPacket(), initialize.length + cmd.length, data_length.length);
			System.arraycopy(separator, 0, getPacket(), initialize.length + cmd.length + data_length.length,
					separator.length);
			System.arraycopy(continut, 0, getPacket(), initialize.length + cmd.length + data_length.length + separator.length,
					continut.length);
			System.out.println("packet=" + getPacket().length);
		} catch (UnsupportedEncodingException ex) {

		}
		return getPacket();
	}

	
	
	public void addMessage(String data) {
		byte[] separator = new byte[1];
		separator[0] = 4;
		try {
			continut = new byte[data.getBytes("UTF8").length + separator.length];
			System.arraycopy(data.getBytes("UTF8"), 0, continut, 0, data.getBytes("UTF8").length);
			System.arraycopy(separator, 0, continut, data.getBytes("UTF8").length, separator.length);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	public void addMessage(int data) {
		byte[] separator = new byte[1];
		separator[0] = 4;
		try {
			continut = new byte[String.valueOf(data).getBytes("UTF8").length + separator.length];
			System.arraycopy(String.valueOf(data).getBytes("UTF8"), 0, continut, 0, String.valueOf(data).getBytes("UTF8").length);
			System.arraycopy(separator, 0, continut, String.valueOf(data).getBytes("UTF8").length, separator.length);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void addMessage(Object data_object) {
		byte[] separator = new byte[1];
		byte[] data = SerializationUtils.serialize((Serializable) data_object);
		separator[0] = 4;
		continut = new byte[data.length];
		System.arraycopy(data, 0, continut, 0, data.length);
		
	}

	public byte[] getPacket() {
		return packet;
	}

	public void setPacket(byte[] packet) {
		this.packet = packet;
	}


}
