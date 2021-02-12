package network;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.ServerSocket;
/**
 * 
 * The NetworkManager listen on a specified port to transmit messages between external <=> internal user && external <=> external users 
 *
 */
public class NetworkManager {
	
	// To receive messages (TCP)
	ServerSocket TCPServer;
	// To accept incoming connections
	TCPAccepter tcpAccepter;
	// To process received messages (UDP)
	UDPReceiver UDPrecv;
	// To receives messages (UDP)
	DatagramSocket UDPSocket;
	
	/**
	 * Create a NetworkManager
	 */
	public NetworkManager() {
		super();
		try {
			TCPServer = new ServerSocket(65000);
			UDPSocket = new DatagramSocket(65000);
			UDPrecv = new UDPReceiver(this);
			tcpAccepter = new TCPAccepter(this);
		} catch (IOException e) {
			System.err.println("Cannot open the server on port " + Config.LISTENING_PORT);
		}
		
	}
	
	/**
	 * Initialize thread (to listen on the specified port in Config)
	 */
	public void init() {
		tcpAccepter.start();
		UDPrecv.start();
	}
	
	
	
	
	
}
