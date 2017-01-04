package Message;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

public class Message {
	private byte[] packet;
	private byte[] continut;

	public Message() {
		packet = null;
		continut=null;
	};

	public Message(byte[] buffer) {
		this.packet = buffer;
	}

	public void setBuffer(byte[] buffer) {
		this.packet = buffer;
	}

	public String getNextParameter() {
		String return_string = "";
		int i = 0;
		int b = 0;
		while ((b = packet[i]) != 4) {
			i++;
			return_string += (char) b;
		}
		packet = Arrays.copyOfRange(packet, return_string.length() + 1, packet.length);
		return return_string;
	}

	public byte[] getDataPacket(byte[] cmd) {
		
		try {
			byte[] initialize = new byte[1];
			initialize[0] = 2;
			byte[] separator = new byte[1];
			separator[0] = 4;
			byte[] data_length = String.valueOf(continut.length).getBytes("UTF8");
			packet = new byte[initialize.length + cmd.length + separator.length + data_length.length + continut.length];

			System.arraycopy(initialize, 0, packet, 0, initialize.length);
			System.arraycopy(cmd, 0, packet, initialize.length, cmd.length);
			System.arraycopy(data_length, 0, packet, initialize.length + cmd.length, data_length.length);
			System.arraycopy(separator, 0, packet, initialize.length + cmd.length + data_length.length,
					separator.length);
			System.arraycopy(continut, 0, packet, initialize.length + cmd.length + data_length.length + separator.length,
					continut.length);
			System.out.println("packet=" + packet.length);
		} catch (UnsupportedEncodingException ex) {

		}
		return packet;
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


}
