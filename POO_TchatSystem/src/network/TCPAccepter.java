package network;

import java.io.IOException;
import java.net.Socket;

import config.LocalSystemConfig;

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
				sock = LocalSystemConfig.m_TCP_socket.accept();
				tcpRecv = new TCPReceiver(sock);
				tcpRecv.start();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}


}
