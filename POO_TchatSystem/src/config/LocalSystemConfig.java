package config;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.util.ResourceBundle;

import network.NetworkManager;

/**
 * 
 * This class contains methods to configure the NetworkManager and provide constants concerning network communications
 *
 */
public final class LocalSystemConfig {


	/* Configuration class, no constructor has to be built*/
	
	// The NetworkManager to configure
	private static NetworkManager client;
	
	
	/**
	 * Initialize the NetworkManager (Only one per program instance)
	 * @param isExt False : the client is inside the company, True otherwise
	 */
	public static void initialize(boolean isExt) {
		client = new NetworkManager(isExt);
		client.init();
	}
	
	/**
	 * 
	 * @return the program NetworkManager
	 */
	public static NetworkManager getNetworkManagerInstance() {
		 return client;
	}
	
	// Default pseudo (choose this pseudo is forbidden)
	public static final String UNKNOWN_USERNAME = "/uknw";
	
	private static ResourceBundle bundle = ResourceBundle.getBundle("properties.config");
	
	// Internal users / Broadcast start port range 
	public static int START_PORT = Integer.valueOf(bundle.getString("pr.start_port_in"));
	// Internal users / Broadcast end port range
	public static int END_PORT = Integer.valueOf(bundle.getString("pr.end_port_in"));
	
	// External users start port range
	public static final int START_PORT_EXT = Integer.valueOf(bundle.getString("pr.start_port_ext"));
	// External users end port range
	public static final int END_PORT_EXT = Integer.valueOf(bundle.getString("pr.end_port_ext"));
	
	// Time waiting for pseudo_request answer (in ms)
	public static final int SLEEP_TIME = 300;

	// Maximum UDP Datagram length
	public static final int UDP_MESSAGE_MAXLENGTH = 256;
	
	// Presence server listening port
	public static final int PRESENCE_SERVER_PORT = Integer.valueOf(bundle.getString("pr.presence_server"));

	public static DatagramSocket m_UDP_socket = null;
	public static ServerSocket m_TCP_socket = null;	

	
	/**
	 * Function that open a UDP server on a port (try to open it between start_port & end_port)
	 */
	public static void openUDPServer() {
		
		//For internal users
		int start_port = START_PORT; 
		int end_port = END_PORT;
		
		//If the user is not inside the company
		if(client.isExternal) {
			// if external user, no broadcast range (Broadcast => Only send to presence server)
			START_PORT = PRESENCE_SERVER_PORT; 
			END_PORT = PRESENCE_SERVER_PORT;
			
			// Select the external users port range
			start_port = START_PORT_EXT;
			end_port = END_PORT_EXT;
		}
		
		// Open UDP Port
		for(int i = start_port; i <= end_port; ++i) {

			try {
				m_UDP_socket = new DatagramSocket(i);
				break;

			} catch (IOException e) {/* Do Nothing, try another port */}
		}
	}
	
	
	/**
	 * Open a TCP server listening on the same port as UDP one
	 */
	public static void openTCPServer(){
		try {
			m_TCP_socket = new ServerSocket(get_UDP_port());
			return;
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	
	/**
	 * 
	 * @return the UDP DatagramSocket
	 */
	public static DatagramSocket get_UDP_socket() {
		return m_UDP_socket;
	}
	
	/**
	 * 
	 * @return the TCP ServerSocket
	 */
	public static ServerSocket get_TCP_socket() {
		return m_TCP_socket;
	}
	
	/**
	 * 
	 * @return the listening UDP Port number
	 */
	public static int get_UDP_port() {
		return m_UDP_socket.getLocalPort();
	}
	
	/**
	 * 
	 * @return the listening TCP Port number
	 */
	public static int get_TCP_port() {
		return m_TCP_socket.getLocalPort();
	}
	

	
	
	
	

}
