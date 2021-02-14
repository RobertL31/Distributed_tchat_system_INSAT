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

		for(int i=Config.LISTENING_PORT_TRY_START; i<=Config.LISTENING_PORT_TRY_END; i++) {
			try {
				TCPServer = new ServerSocket(i);
				UDPSocket = new DatagramSocket(i);
				System.out.println("Serveur lancé en écoute sur le port : " 
				+ i 
				+ ", veuillez reporter ce port dans le fichier \"src/properties/config.properties\" de client.jar (pr.presence_server)");
				break;
			}
		 catch (IOException e) {}
		}
		UDPrecv = new UDPReceiver(this);
		tcpAccepter = new TCPAccepter(this);

	}

	/**
	 * Initialize thread (to listen on the specified port in Config)
	 */
	public void init() {
		tcpAccepter.start();
		UDPrecv.start();
	}





}
