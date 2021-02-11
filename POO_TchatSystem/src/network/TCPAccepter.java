package network;

import java.io.IOException;
import java.net.Socket;

import config.LocalSystemConfig;
/**
 * 
 * Accept incoming TCP connections
 *
 */
public class TCPAccepter extends Thread{
	NetworkManager client;

	TCPAccepter(){
		this.client = LocalSystemConfig.getNetworkManagerInstance();
	}

	public void run(){
		while(true) {
			Socket sock;
			TCPReceiver tcpRecv;
			try {
				//Accept the connection
				sock = LocalSystemConfig.m_TCP_socket.accept();

				tcpRecv = new TCPReceiver(sock);
				// Launch a thread to handle received message
				tcpRecv.start();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}


}
