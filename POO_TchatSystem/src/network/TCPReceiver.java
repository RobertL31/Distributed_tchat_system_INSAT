package network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

import config.LocalSystemConfig;
/**
 * 
 * Handle received TCP messages
 *
 */
public class TCPReceiver extends Thread{

	private NetworkManager client;
	private Socket listeningSocket;

	TCPReceiver(Socket listeninSocket){
		this.client = LocalSystemConfig.getNetworkManagerInstance();
		this.listeningSocket = listeninSocket;
	}

	/**
	 * Receive a message
	 * @return the received message
	 */
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
	 * Process a message
	 * @param message the message to process
	 */
	private void processMessage(String message) {
		if(message == null) return;
		
		int srcPort = 0;
		//Message coming from server, add 100.000 to the source port (In case of an answer)
		if(message.startsWith(MessageCode.FROM_SERVER)) {
			srcPort += 100000;
			message = message.substring(MessageCode.FROM_SERVER.length());
		}
		
		//If change pseudo request
		if(message.startsWith(MessageCode.ASK_CHANGE_PSEUDO)) {
			//Remove message header (MessageCode)
			message = message.substring(MessageCode.ASK_CHANGE_PSEUDO.length());
			String[] answer = message.split(MessageCode.SEP);
			srcPort += Integer.valueOf(answer[0]);
			String pseudo = answer[1];
			//Check if the asked pseudo is valid (!= of the user one)
			boolean isValid = !pseudo.equals(client.getPseudo());
			
			//Reply to who ask
			String reply = MessageCode.REPLY_CHANGE_PSEUDO + Boolean.toString(isValid);
			Sender.send(reply, srcPort);

		}
		//If reply to a change pseudo request
		else if(message.startsWith(MessageCode.REPLY_CHANGE_PSEUDO)) {
			// Remove header
			String answer = message.substring(MessageCode.REPLY_CHANGE_PSEUDO.length());
			boolean validity = Boolean.parseBoolean(answer) && client.isValidPseudo();
			// Set the new pseudo validity according to the received answer
			client.setValidPseudo(validity);
		}
		//If pseudo notification
		else if(message.startsWith(MessageCode.NOTIFY_CHANGE_PSEUDO)) {
			//Remove header
			String answer = message.substring(MessageCode.NOTIFY_CHANGE_PSEUDO.length());
			String[] ansArray = answer.split(MessageCode.SEP);
			srcPort += Integer.valueOf(ansArray[0]);
			String pseudo = ansArray[1];
			//Add the received pseudo to the user list
			client.getM_IP_Pseudo_Table().put(srcPort, pseudo);
			
			//Send the user pseudo
			String reply = MessageCode.TELL_PSEUDO 
					+ LocalSystemConfig.get_TCP_port() 
					+ MessageCode.SEP
					+ client.getPseudo()
					;
			Sender.send(reply, srcPort);
		}
		// If Tell pseudo received 
		else if(message.startsWith(MessageCode.TELL_PSEUDO)) {
			// Remove header
			String answer = message.substring(MessageCode.TELL_PSEUDO.length());
			String[] ansArray = answer.split(MessageCode.SEP);
			srcPort += Integer.valueOf(ansArray[0]);
			String distPseudo = ansArray[1];
			//Add the received pseudo to the user list
			client.getM_IP_Pseudo_Table().put(srcPort, distPseudo);
		}
		// If private message
		else if(message.startsWith(MessageCode.PRIVATE_MESSAGE)){
			//Remove header
			String answer = message.substring(MessageCode.PRIVATE_MESSAGE.length());
			String[] ansArray = answer.split(MessageCode.SEP);
			srcPort += Integer.valueOf(ansArray[0]);
			String content = ansArray[1];
			// Transmit the message to the conversation manager
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
