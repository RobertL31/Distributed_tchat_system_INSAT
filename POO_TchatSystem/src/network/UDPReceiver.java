package network;

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

import config.LocalSystemConfig;
import tools.Pair;

/**
 * 
 * Handle UDP messages received
 *
 */
public class UDPReceiver extends Thread{

	private DatagramSocket m_UDP_socket;
	private NetworkManager client;

	public UDPReceiver() {
		this.m_UDP_socket = LocalSystemConfig.m_UDP_socket;
		this.client = LocalSystemConfig.getNetworkManagerInstance();
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

	/**
	 * Process a UDP message
	 * @param recepted the message to process
	 */
	public void processMessage(Pair<String, Integer> recepted) {
		String msg = recepted.getFirst();
		int srcPort = recepted.getSecond();
		int destinationPort = recepted.getSecond();
		
		//If the message comes from the presence server
		if(srcPort == LocalSystemConfig.PRESENCE_SERVER_PORT) {
			String extSrcPort = msg.split(MessageCode.SEP)[0];
			//Src port is in the message, add 100 000
			srcPort = Integer.valueOf(extSrcPort) + 100000;
			msg = msg.substring(extSrcPort.length() + MessageCode.SEP.length());
		}

		//for TCP communications//
		Socket sock;
		TCPReceiver tcpRecv;

		//Join
		if(msg.startsWith(MessageCode.NOTIFY_JOIN)) {
			String respMsg = MessageCode.NOTIFY_REPLY + (LocalSystemConfig.get_TCP_port() + MessageCode.SEP + srcPort);
			//New connection, add TCP port to HashMap and send answer
			client.getM_IP_Pseudo_Table().put(srcPort, LocalSystemConfig.UNKNOWN_USERNAME);
			try {
				//Notify back that user is on the network
				client.getM_sender().send(Sender.createUDPDatagram(respMsg, destinationPort));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		//Reply
		else if (msg.startsWith(MessageCode.NOTIFY_REPLY)) {
			//Add port to the connected users list
			client.getM_IP_Pseudo_Table().put(srcPort, LocalSystemConfig.UNKNOWN_USERNAME);

		}
		//Leave
		else if (msg.startsWith(MessageCode.NOTIFY_LEAVE)) {
			//Remove sender from the connected users list
			client.getM_IP_Pseudo_Table().remove(srcPort);
		}
	}



	public void run() {
		while(true) {
			Pair<String, Integer> recepted = receive();
			processMessage(recepted);
		}
	}
}
