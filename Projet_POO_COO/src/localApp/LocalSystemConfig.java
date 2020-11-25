package localApp;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.ServerSocket;

public final class LocalSystemConfig {
	
	
	/* Configuration class, no constructor has to be built*/
	
	
	public static final int START_PORT = 65040;
	public static final int END_PORT = 65050;
	
	public static DatagramSocket m_UDP_socket = null;
	
	
	
	
	public static void openUDPServer() {
		
		for(int i = START_PORT; i <= END_PORT; ++i) {
			
			try {
				m_UDP_socket = new DatagramSocket(i);
				break;
				
			} catch (IOException e) {}
		}
	}


	
	public static int get_UDP_port() {
		return m_UDP_socket.getLocalPort();
	}

	
	
	
	
}
