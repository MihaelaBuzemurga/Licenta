package TransformerBytes;


import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;

import org.apache.commons.lang3.*;

public class TransformerBytes {


	public TransformerBytes() {
		
	};



	public static byte[] getDataPacket(Object obiect) {

		byte[] data = SerializationUtils.serialize((Serializable) obiect);
		byte[] packet = null;

		try {
			byte[] separator = new byte[1];
			separator[0] = 4;
			byte[] initialize = new byte[1];
			initialize[0] = 2;
			byte[] data_length = String.valueOf(data.length).getBytes("UTF8");
			packet = new byte[initialize.length+separator.length + data_length.length + data.length];
			
			System.arraycopy(initialize, 0, packet, 0, initialize.length);
			
			System.arraycopy(data_length, 0, packet, initialize.length, data_length.length);
			System.arraycopy(separator, 0, packet, initialize.length+data_length.length, separator.length);
			System.arraycopy(data, 0, packet, initialize.length+data_length.length + separator.length, data.length);
		} catch (UnsupportedEncodingException ex) {

		}
		return packet;
	}

}
