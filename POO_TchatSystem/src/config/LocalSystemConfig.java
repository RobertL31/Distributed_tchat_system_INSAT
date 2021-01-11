package config;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.ServerSocket;

import network.NetworkManager;

public final class LocalSystemConfig {


	/* Configuration class, no constructor has to be built*/
	private static NetworkManager client;
	
	public static void initialize() {
		client = new NetworkManager();
		client.init();
	}
	
	public static NetworkManager getNetworkManagerInstance() {
		 return client;
	}
	
	
	
	public static final String UNKNOWN_USERNAME = "/uknw";
	
	public static final int START_PORT = 65000;
	public static final int END_PORT = 65100;
	
	public static final int SLEEP_TIME = 300;

	public static final int UDP_MESSAGE_MAXLENGTH = 256;
	
	public static final int PRESENCE_SERVER_PORT = 65000;

	public static DatagramSocket m_UDP_socket = null;
	public static ServerSocket m_TCP_socket = null;	

	public static void openUDPServer() {

		for(int i = START_PORT; i <= END_PORT; ++i) {

			try {
				m_UDP_socket = new DatagramSocket(i);
				break;

			} catch (IOException e) {/* Do Nothing, try another port */}
		}
	}

	public static DatagramSocket get_UDP_socket() {
		return m_UDP_socket;
	}

	public static ServerSocket get_TCP_socket() {
		return m_TCP_socket;
	}

	public static int get_UDP_port() {
		return m_UDP_socket.getLocalPort();
	}
	
	public static int get_TCP_port() {
		return m_TCP_socket.getLocalPort();
	}
	

	public static void openTCPServer(){
		try {
			m_TCP_socket = new ServerSocket(get_UDP_port());
			return;
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	
	
	

}
