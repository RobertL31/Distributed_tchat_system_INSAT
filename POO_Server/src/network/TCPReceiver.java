package network;

///////////////////// SERVER RECEIVER /////////////////

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
/**
 * 
 * Redirect a TCP message
 * 
 */
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

	/**
	 * Redirect the message
	 * @param message to redirect
	 */
	private void processMessage(String message) {
		if(message == null) return;
		
		String[] separated = message.split(Config.SEP);
		System.out.println("[TCP]" + message);
		int destPort = Integer.valueOf(separated[0]);
		
		//  Dest SEP Message
		message = message.substring(message.indexOf(Config.SEP) + Config.SEP.length());
		message = Config.FROM_SERVER + message;
		
		
		TCPSender.send(message, destPort);
		
	}

	public void run() {
		String msg = receive();
		processMessage(msg);
		//Once the message is processed, close socket + stop thread
		try {
			listeningSocket.close();
		} catch (IOException e) {
			System.err.println("Network_Receiver: Socket already closed !");
			e.printStackTrace();
		}
		
	}


	public NetworkManager getClient() {
		return client;
	}
}
