package Server;

import Reusable.*;

public class ServerMain {
	
	public static void main(String[] args) {
		ThreadPooledServer server = new ThreadPooledServer(ServerInfo.SERVER_PORT.getValue());
		new Thread(server).start();

//		try {
//		    Thread.sleep(ServerInfo.SERVER_SLEEP_TIME.getValue());
//		} catch (InterruptedException e) {
//		    e.printStackTrace();
//		}
//		System.out.println("Stopping Server");
//		server.stop();
	}

}
