package network;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;

public class NetworkManager {
	
	ServerSocket TCPServer;
	TCPAccepter tcpAccepter;
	public static ArrayList<Integer> connectedList;
	
	public NetworkManager() {
		super();
		
		//////////////////////// TODO ///////////////////////////
		try {
			TCPServer = new ServerSocket(65000);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/////////////////////////////////////////////////////////
		connectedList = new ArrayList<Integer>();
		tcpAccepter = new TCPAccepter(this);
		tcpAccepter.start();
		
	}
	
	
	
	
	
}
