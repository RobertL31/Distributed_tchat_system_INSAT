package distantApp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.nio.charset.StandardCharsets;
import java.security.KeyPair;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import localApp.LocalSystemConfig;
import tools.Pair;

public class Network_receiver_UDP extends Thread{
	
	private DatagramSocket m_UDP_socket;
	private NetworkManager Client;
	
	public Network_receiver_UDP(NetworkManager Client) {
		this.m_UDP_socket = LocalSystemConfig.m_UDP_socket;
		this.Client = Client;
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
		return new Pair<String, Integer>(message, srcPort);
	}
	
	public void processMessage(Pair<String, Integer> recepted) {
		String msg = recepted.getFirst();
		int port = recepted.getSecond();
		
		if(msg.startsWith(MessageCode.NOTIFY_JOIN)) {
			//New connection, add port to HashMap and send answer
			if(!Client.getM_IP_Pseudo_Table().containsKey(port)) {
				Client.getM_IP_Pseudo_Table().put(port, LocalSystemConfig.UNKNOWN_USERNAME);
				try {
					Client.getM_sender().send(Network_Sender.createUDPDatagram(MessageCode.NOTIFY_JOIN, port));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		else if (msg.startsWith(MessageCode.NOTIFY_LEAVE)) {
			Client.getM_IP_Pseudo_Table().remove(port);
		}
	}
	
	
	
	public void run() {
		while(true) {
			Pair<String, Integer> recepted = receive();
			processMessage(recepted);
			System.out.println(Client.getM_IP_Pseudo_Table().toString());
		}
	}
}
