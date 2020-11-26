package distantApp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;

import localApp.LocalSystemConfig;

public class Network_Sender {
	
	private DatagramSocket m_UDP_socket;
	
	public Network_Sender() {
		m_UDP_socket = LocalSystemConfig.m_UDP_socket;
	}
	
	public static DatagramPacket createUDPDatagram(String message, int port) throws IOException {
		byte[] buff = message.getBytes();
		return new DatagramPacket(buff, buff.length, InetAddress.getLocalHost(), port);
	}
	
	public void send(String s, int port){
		//TODO
	}
	
	public void send(DatagramPacket p) {
		
		try {
			m_UDP_socket.send(p);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
