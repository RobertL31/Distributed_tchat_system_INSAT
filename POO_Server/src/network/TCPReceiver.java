package network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

public class TCPReceiver extends Thread{

	private NetworkManager client;
	private Socket listeningSocket;

	TCPReceiver(NetworkManager client, Socket listeningSocket){
		this.client = client;
		this.listeningSocket = listeningSocket;
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

	private void processMessage(String message) {
		if(message == null) return;
		
	}

	public void run() {
		String msg = receive();
		processMessage(msg);
		//Once the message processed, close socket + stop thread
		try {
			listeningSocket.close();
		} catch (IOException e) {
			System.err.println("Network_Receiver: Socket already closed !");
			e.printStackTrace();
		}
		
	}
}
