import java.io.IOException;
import java.net.ServerSocket;

public class Server {
	public static void main(String[] args) {
        try {
        	System.out.println("Se pregateste!");
            ServerSocket server_socket = new ServerSocket(9090);
            while (true) {
            	System.out.println("a pornit!");
                new Thread(new ClientWorker(server_socket.accept())).start();
            }
        } catch (IOException ex) {
           
        }
    }

}
