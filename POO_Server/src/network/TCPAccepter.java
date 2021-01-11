package network;

import java.io.IOException;
import java.net.Socket;

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
				tcpRecv.start();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}


}
