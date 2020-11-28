package distantApp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class Network_receiver_TCP extends Thread{
	
	private NetworkManager client;
	private Socket listeningSocket;
	
	Network_receiver_TCP(NetworkManager client, Socket listeninSocket){
		this.client = client;
		this.listeningSocket = listeninSocket;
	}
	
	
	private String receive() {
		BufferedReader buff;
		String msg="error";
		try {
			buff = new BufferedReader(new InputStreamReader(listeningSocket.getInputStream()));
			msg = buff.readLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return msg;
	}
	
	public void run() {
		while(true) {
			String msg = receive();
			if(msg != null)
			System.out.println(msg);
		}
	}
}
