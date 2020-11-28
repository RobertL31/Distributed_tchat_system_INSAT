package distantApp;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import localApp.LocalSystemConfig;

public class Network_Sender {
	
	private DatagramSocket m_UDP_socket;
	private NetworkManager client;
	
	public Network_Sender(NetworkManager client) {
		m_UDP_socket = LocalSystemConfig.m_UDP_socket;
		this.client = client;
	}
	
	public static DatagramPacket createUDPDatagram(String message, int port) throws IOException {
		byte[] buff = message.getBytes();
		return new DatagramPacket(buff, buff.length, InetAddress.getLocalHost(), port);
	}
	
	public void send(String msg, int port){
		//TODO
	}
	
	public void TCP_sendAll(String msg) {
		PrintWriter out;
		for(Socket sock : client.getSocketList()){
			try {
				out = new PrintWriter(sock.getOutputStream(),true);
				out.println(msg); //println beacause BufferedReadder.readLine() expect a '\n'
				out.flush();
			} catch (IOException e) {
				System.out.println("Socket " + sock.getPort() + "is closed!");
			}
		}
	}
	
	public void send(DatagramPacket p) {
		
		try {
			m_UDP_socket.send(p);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
