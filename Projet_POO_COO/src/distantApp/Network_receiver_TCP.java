package distantApp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
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
	
	private void processMessage(String message) {
		if(message.isEmpty()) return;
		if(message.startsWith(MessageCode.ASK_CHANGE_PSEUDO)) {
			String pseudo = message.substring(MessageCode.ASK_CHANGE_PSEUDO.length());
			boolean isValid = !pseudo.equals(client.getPseudo());
			PrintWriter out;
			try {
				out = new PrintWriter(this.listeningSocket.getOutputStream(),true);
				out.println(MessageCode.REPLY_CHANGE_PSEUDO + isValid + MessageCode.SEP_CHANGE_PSEUDO + client.getPseudo()); //println beacause BufferedReadder.readLine() expect a '\n'
				out.flush();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		else if(message.startsWith(MessageCode.NOTIFY_CHANGE_PSEUDO)) {
			String distPseudo = message.substring(MessageCode.NOTIFY_CHANGE_PSEUDO.length());
			client.getM_IP_Pseudo_Table().put(listeningSocket.getPort(), distPseudo);
		}
	}
	
	public void run() {
		while(true) {
			String msg = receive();
			processMessage(msg);
		}
	}
}
