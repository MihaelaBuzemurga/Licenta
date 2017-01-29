import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

public class ClientWorker implements Runnable {
	private Socket target_socket;
	private DataInputStream din;
	private DataOutputStream dout;
	private String line;
	private String nume;
	Scanner s = new Scanner(System.in);
	public ClientWorker(Socket recv_socket) {
		try {
			target_socket = recv_socket;
			din = new DataInputStream(target_socket.getInputStream());
			dout = new DataOutputStream(target_socket.getOutputStream());
//			Thread trd= new Thread (new SThread());
//			trd.start();
		} catch (IOException ex) {
			
		}
	}

	@Override
	public void run() {
		
		// TODO Auto-generated method stub
		System.out.println("O prietena");
		 
		 while (true) {
		        try {
		        	  byte[] initilize = new byte[100];
					din.read(initilize, 0, initilize.length);
					String ms=new String(initilize);
					System.out.println(ms);
					if(ms.contains("start"))
					{
//						Thread trd= new Thread (new SThread());
//						trd.start();
						Path path = Paths.get("C:\\Users\\Gabriel\\Desktop\\file.txt");
						byte[] data = Files.readAllBytes(path);
						System.out.println(data);
						byte[] packet=CreateDataPacket("125".getBytes("UTF8"), data);
						System.out.println(packet);
						dout.write(packet);
					}
					else
					{
						nume=ms;
					}
					//dout.write("ceva!".getBytes());
					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		      }

	}
	public class SThread implements Runnable {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			while(true){
				for(int i=0;i<=100;i++)
				{
					try {
//						dout.write(msg.getBytes());
						dout.write(String.valueOf(i).getBytes());
						System.out.println(nume+": "+String.valueOf(i));
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
			}

		}

	}
	private byte[] CreateDataPacket(byte[] cmd, byte[] data) {
        byte[] packet = null;
        try {
          
            byte[] data_length = String.valueOf(data.length).getBytes("UTF8");
            byte[] separator = new byte[1];
            separator[0] = 4;
            packet = new byte[data_length.length + data.length+separator.length];
            System.arraycopy(data_length, 0, packet, 0, data_length.length);
            System.arraycopy(separator, 0, packet, data_length.length, separator.length);
            System.arraycopy(data, 0, packet,data_length.length+separator.length, data.length);

        } catch (UnsupportedEncodingException ex) {
           // Logger.getLogger(TCPDataClient.class.getName()).log(Level.SEVERE, null, ex);
        }
        return packet;
    }


}
