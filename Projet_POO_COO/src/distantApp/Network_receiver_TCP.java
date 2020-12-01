package distantApp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

import localApp.LocalSystemConfig;

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
		if(message == null) return;
		if(message.startsWith(MessageCode.ASK_CHANGE_PSEUDO)) {
			message = message.substring(MessageCode.ASK_CHANGE_PSEUDO.length());
			String[] answer = message.split(MessageCode.SEP_CHANGE_PSEUDO);
			int srcPort = Integer.valueOf(answer[0]);
			String pseudo = answer[1];
			boolean isValid = !pseudo.equals(client.getPseudo());

			//Init a new connection
			String reply = MessageCode.REPLY_CHANGE_PSEUDO + Boolean.toString(isValid);
			Network_Sender.send(reply, srcPort);

		}
		else if(message.startsWith(MessageCode.REPLY_CHANGE_PSEUDO)) {
			String answer = message.substring(MessageCode.REPLY_CHANGE_PSEUDO.length());
			boolean validity = Boolean.parseBoolean(answer) && client.isValidPseudo();
			client.setValidPseudo(validity);
		}
		else if(message.startsWith(MessageCode.NOTIFY_CHANGE_PSEUDO)) {
			String answer = message.substring(MessageCode.NOTIFY_CHANGE_PSEUDO.length());
			String[] ansArray = answer.split(MessageCode.SEP_CHANGE_PSEUDO);
			int port = Integer.valueOf(ansArray[0]);
			String pseudo = ansArray[1];
			client.getM_IP_Pseudo_Table().put(port, pseudo);
			
			//Send my pseudo
			String reply = MessageCode.TELL_PSEUDO 
					+ LocalSystemConfig.get_TCP_port() 
					+ MessageCode.SEP_CHANGE_PSEUDO
					+ client.getPseudo()
					;
			Network_Sender.send(reply, port);
		}
		else if(message.startsWith(MessageCode.TELL_PSEUDO)) {
			String answer = message.substring(MessageCode.TELL_PSEUDO.length());
			String[] ansArray = answer.split(MessageCode.SEP_CHANGE_PSEUDO);
			int port = Integer.valueOf(ansArray[0]);
			String distPseudo = ansArray[1];
			client.getM_IP_Pseudo_Table().put(port, distPseudo);
		}
	}

	public void run() {
		while(true) {
			String msg = receive();
			processMessage(msg);
		}
	}
}
