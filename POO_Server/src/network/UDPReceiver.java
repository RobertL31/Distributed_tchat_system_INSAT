package network;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;

/**
 * 
 * Redirect UDP Message
 *
 */
public class UDPReceiver extends Thread{

	private DatagramSocket m_UDP_socket;
	private NetworkManager client;

	public UDPReceiver(NetworkManager client) {
		this.m_UDP_socket = client.UDPSocket;
		this.client = client;
	}

	public static boolean isExtPort(int port) {
		return ((port >= Config.START_PORT_EXT) && (port <= Config.END_PORT_EXT));
	}


	public static DatagramPacket createUDPDatagram(String message, int port) throws IOException {
		byte[] buff = message.getBytes();
		return new DatagramPacket(buff, buff.length, InetAddress.getLocalHost(), port);
	}

	/**
	 * Receive a UDP message and redirect it
	 * 
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
		message = senderPort + Config.SEP + message;


		int start;
		int end;		
		//External user : send to everyone (EXT + IN)
		if(isExtPort(senderPort)) {
			start = Config.START_PORT_GENERAL;
			end = Config.END_PORT_GENERAL;
		}
		//Internal user : send to External users only (EXT)
		else {
			start = Config.START_PORT_EXT;
			end = Config.END_PORT_EXT;
		}
		String[] splitString = message.split(Config.SEP);
		// If it is a UDP Reply, only send to the target
		if(splitString.length > 2 
				&& splitString[1].startsWith("reply")) {
			int dest = Integer.valueOf(splitString[2]) - 100000; // -100 000 because the sender added 100 000
			try {
				client.UDPSocket.send(createUDPDatagram(message, dest));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		else {
			for(int i=start; i<=end; i++) {
				if(i != client.UDPSocket.getLocalPort() && i != recv.getPort()) {
					try {
						client.UDPSocket.send(createUDPDatagram(message, i));
					} catch (IOException e) {
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
