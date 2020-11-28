package distantApp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.security.KeyPair;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import localApp.LocalSystemConfig;
import tools.Pair;

public class Network_receiver_UDP extends Thread{
	
	private DatagramSocket m_UDP_socket;
	private NetworkManager client;
	
	public Network_receiver_UDP(NetworkManager client) {
		this.m_UDP_socket = LocalSystemConfig.m_UDP_socket;
		this.client = client;
	}
	
	/**
	 * Receive a UDP message
	 * @return (message, source port)
	 */
	public Pair<String, Integer> receive() {
		byte[] b0 = new byte[LocalSystemConfig.UDP_MESSAGE_MAXLENGTH];
		DatagramPacket recv = new DatagramPacket(b0, b0.length);
		try {
			m_UDP_socket.receive(recv);
		} catch (IOException e) {
			e.printStackTrace();
		}
		String message = new String(recv.getData(), StandardCharsets.UTF_8);
		int srcPort  = recv.getPort(); 
		
		// Remove unused bytes (because buffer size >= data transmitted size)
		message = message.substring(0, recv.getLength());
		
		return new Pair<String, Integer>(message, srcPort);
	}
	
	public void processMessage(Pair<String, Integer> recepted) {
		String msg = recepted.getFirst();
		int srcPort = recepted.getSecond();
		
		if(msg.startsWith(MessageCode.NOTIFY_JOIN)) {
			String respMsg = MessageCode.NOTIFY_JOIN + (LocalSystemConfig.get_TCP_port());
			//for TCP communications//
			Socket sock;
			Network_receiver_TCP tcpRecv;
			//////////////////////////
			//New connection, add TCP port to HashMap and send answer
			if(!client.getM_IP_Pseudo_Table().containsKey(srcPort)) {
				client.getM_IP_Pseudo_Table().put(srcPort, LocalSystemConfig.UNKNOWN_USERNAME);
				try {
					//Notify that i'm here too
					client.getM_sender().send(Network_Sender.createUDPDatagram(respMsg, srcPort));
					
					//Open TCP connection
					sock = new Socket(InetAddress.getLocalHost(), srcPort);
					client.getSocketList().add(sock);
					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		/*TODO : disconnect
		 *  else if ....
		} */
	}
	
	
	
	public void run() {
		while(true) {
			Pair<String, Integer> recepted = receive();
			processMessage(recepted);
		}
	}
}
