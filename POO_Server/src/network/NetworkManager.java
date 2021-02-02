package network;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.util.ArrayList;

public class NetworkManager {
	
	ServerSocket TCPServer;
	TCPAccepter tcpAccepter;
	
	UDPReceiver UDPrecv;
	
	DatagramSocket UDPSocket;
	
	public NetworkManager() {
		super();
		//////////////////////// TODO ///////////////////////////
		try {
			TCPServer = new ServerSocket(65000);
			UDPSocket = new DatagramSocket(65000);
			UDPrecv = new UDPReceiver(this);
			tcpAccepter = new TCPAccepter(this);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/////////////////////////////////////////////////////////
		
	}
	
	public void init() {
		tcpAccepter.start();
		UDPrecv.start();
	}
	
	
	
	
	
}
