package distantApp;

import java.io.IOException;
import java.net.Socket;

import localApp.LocalSystemConfig;

public class TCPAccepter extends Thread{
	NetworkManager client;

	TCPAccepter(NetworkManager client){
		this.client = client;
	}

	public void run(){
		while(true) {
			Socket sock;
			Network_receiver_TCP tcpRecv;
			try {
				sock = LocalSystemConfig.m_TCP_socket.accept();
				tcpRecv = new Network_receiver_TCP(client, sock);
				tcpRecv.start();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}


}
