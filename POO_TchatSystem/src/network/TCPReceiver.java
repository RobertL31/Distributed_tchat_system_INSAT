package network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

import config.LocalSystemConfig;

public class TCPReceiver extends Thread{

	private NetworkManager client;
	private Socket listeningSocket;

	TCPReceiver(Socket listeninSocket){
		this.client = LocalSystemConfig.getNetworkManagerInstance();
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
		
		int srcPort = 0;
		if(message.startsWith(MessageCode.FROM_SERVER)) {
			//System.out.println("Here : received tcp from server");
			srcPort += 100000;
			message = message.substring(MessageCode.FROM_SERVER.length());
		}
		
		if(message.startsWith(MessageCode.ASK_CHANGE_PSEUDO)) {
			message = message.substring(MessageCode.ASK_CHANGE_PSEUDO.length());
			String[] answer = message.split(MessageCode.SEP);
			srcPort += Integer.valueOf(answer[0]);
			String pseudo = answer[1];
			boolean isValid = !pseudo.equals(client.getPseudo());

			//Init a new connection
			String reply = MessageCode.REPLY_CHANGE_PSEUDO + Boolean.toString(isValid);
			Sender.send(reply, srcPort);

		}
		else if(message.startsWith(MessageCode.REPLY_CHANGE_PSEUDO)) {
			String answer = message.substring(MessageCode.REPLY_CHANGE_PSEUDO.length());
			boolean validity = Boolean.parseBoolean(answer) && client.isValidPseudo();
			client.setValidPseudo(validity);
		}
		else if(message.startsWith(MessageCode.NOTIFY_CHANGE_PSEUDO)) {
			String answer = message.substring(MessageCode.NOTIFY_CHANGE_PSEUDO.length());
			String[] ansArray = answer.split(MessageCode.SEP);
			srcPort += Integer.valueOf(ansArray[0]);
			String pseudo = ansArray[1];
			client.getM_IP_Pseudo_Table().put(srcPort, pseudo);
			
			//Send my pseudo
			String reply = MessageCode.TELL_PSEUDO 
					+ LocalSystemConfig.get_TCP_port() 
					+ MessageCode.SEP
					+ client.getPseudo()
					;
			Sender.send(reply, srcPort);
		}
		else if(message.startsWith(MessageCode.TELL_PSEUDO)) {
			String answer = message.substring(MessageCode.TELL_PSEUDO.length());
			String[] ansArray = answer.split(MessageCode.SEP);
			srcPort += Integer.valueOf(ansArray[0]);
			String distPseudo = ansArray[1];
			client.getM_IP_Pseudo_Table().put(srcPort, distPseudo);
		}
		else if(message.startsWith(MessageCode.PRIVATE_MESSAGE)){
			String answer = message.substring(MessageCode.PRIVATE_MESSAGE.length());
			String[] ansArray = answer.split(MessageCode.SEP);
			srcPort += Integer.valueOf(ansArray[0]);
			String content = ansArray[1];
			client.getConvManager().receive(content, srcPort);
		}
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
