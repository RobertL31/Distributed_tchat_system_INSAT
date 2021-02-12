package network;

import java.io.IOException;
import java.net.Socket;
/**
 * 
 * Accept incoming connections (TCP)
 *
 */
public class TCPAccepter extends Thread{
	NetworkManager client;

	TCPAccepter(NetworkManager client){
		this.client = client;
	}

	public void run(){
		while(true) {
			Socket sock;
			TCPReceiver tcpRecv;
			try {
				sock = client.TCPServer.accept();
				tcpRecv = new TCPReceiver(client, sock);
				// Start a thread to process the received message
				tcpRecv.start();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}


}
