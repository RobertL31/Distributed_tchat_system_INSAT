package network;

///////////////////// SERVER RECEIVER /////////////////

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

public class TCPReceiver extends Thread{

	private NetworkManager client;
	private Socket listeningSocket;
	
	public static final String SEP = "#separator#"; //NOT A NUMBER OR CHAR
	public static final String FROM_SERVER = "#fromserver#"; //NOT A NUMBER OR CHAR

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
		
		String[] separated = message.split(SEP);
		System.out.println(message);
		int destPort = Integer.valueOf(separated[0]);
		
		//  Dest SEP Message
		message = message.substring(message.indexOf(SEP) + SEP.length());
		message = FROM_SERVER + message;
		
		
		TCPSender.send(message, destPort);
		
		System.out.println("\"" + message + "\"" + "sent to " + destPort);
		
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
