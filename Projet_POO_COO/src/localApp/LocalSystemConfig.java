package localApp;

import java.io.IOException;
import java.net.ServerSocket;

public final class LocalSystemConfig {
	
	
	/* Configuration class, no constructor has to be built*/
	
	
	public static final int START_PORT = 65040;
	public static final int END_PORT = 65050;
	
	public static ServerSocket m_TCP_socket = null;
	
	
	
	
	public static void openTCPServer() {
		
		for(int i = START_PORT; i <= END_PORT; ++i) {
			
			try {
				m_TCP_socket = new ServerSocket(i);
				break;
				
			} catch (IOException e) {}
		}
	}


	
	public static int get_TCP_port() {
		return m_TCP_socket.getLocalPort();
	}

	
	
	
	
}
