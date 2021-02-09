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


public class UDPReceiver extends Thread{

	private DatagramSocket m_UDP_socket;
	private NetworkManager client;

	public static int START_PORT_GENERAL = 64000;
	public static int END_PORT_GENERAL = 65100;

	public static int START_PORT_EXT = START_PORT_GENERAL;
	public static int END_PORT_EXT = 64999;

	public static int START_PORT_IN = 65001;
	public static int END_PORT_IN = END_PORT_GENERAL;

	public UDPReceiver(NetworkManager client) {
		this.m_UDP_socket = client.UDPSocket;
		this.client = client;
	}

	public static boolean isExtPort(int port) {
		return ((port >= START_PORT_EXT) && (port <= END_PORT_EXT));
	}


	public static DatagramPacket createUDPDatagram(String message, int port) throws IOException {
		byte[] buff = message.getBytes();
		return new DatagramPacket(buff, buff.length, InetAddress.getLocalHost(), port);
	}

	/**
	 * Receive a UDP message
	 * @return (message, source port)
	 */
	public void receive() {
		byte[] b0 = new byte[255];
		DatagramPacket recv = new DatagramPacket(b0, b0.length);
		try {
			m_UDP_socket.receive(recv);
		} catch (IOException e) {
			e.printStackTrace();
		}
		int senderPort = recv.getPort();
		String message = new String(recv.getData(), StandardCharsets.UTF_8);
		// Remove unused bytes (because buffer size >= data transmitted size)
		message = message.substring(0, recv.getLength());
		message = senderPort + TCPReceiver.SEP + message;


		int start;
		int end;		
		//External user : send to everyone (EXT + IN)
		if(isExtPort(senderPort)) {
			start = START_PORT_GENERAL;
			end = END_PORT_GENERAL;
		}
		//Internal user : send to External users only (EXT)
		else {
			start = START_PORT_EXT;
			end = END_PORT_EXT;
		}

		System.out.println("[UDP] Received : " + message);


		String[] splitString = message.split(TCPReceiver.SEP);
		
		System.out.println("[UDP] Received : " + message + "length = " + splitString.length);
		
		
		// If it is a UDP Reply, only send to the target
		if(splitString.length > 2 
				&& splitString[1].startsWith("reply")) {
			int dest = Integer.valueOf(splitString[2]) - 100000;
			try {
				client.UDPSocket.send(createUDPDatagram(message, dest));
				System.out.println("UDP reply Sent to " + dest);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else {
			for(int i=start; i<=end; i++) {
				if(i != client.UDPSocket.getLocalPort() && i != recv.getPort()) {
					try {
						client.UDPSocket.send(createUDPDatagram(message, i));
						//System.out.println("Diustributed to : " + i);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			} 
		}
	}




	public void run() {
		while(true) {
			receive();
		}
	}
}
