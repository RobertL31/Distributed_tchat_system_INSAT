package distantApp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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
	
	public void sendPseudoRequest() {
		
		//Send chosen pseudo to all the network
		client.getM_IP_Pseudo_Table().forEach((port, distPseudo) -> {
			try {
				PrintWriter out;
				Socket sock = new Socket(InetAddress.getLocalHost(), port);
				out = new PrintWriter(sock.getOutputStream(),true);
				out.println(MessageCode.ASK_CHANGE_PSEUDO + client.getPseudo()); //println beacause BufferedReadder.readLine() expect a '\n'
				out.flush();
				client.getSocketList().add(sock);
			} catch (IOException e) {
				
			}
		});
		try {
			Thread.sleep(LocalSystemConfig.SLEEP_TIME);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// check answers + boolean op on client.validPseudo
		for(Socket sock : client.getSocketList()) {
			BufferedReader buff;
			try {
				buff = new BufferedReader(new InputStreamReader(sock.getInputStream()));
				String ans = buff.readLine();
				String pseudos= ans.substring(MessageCode.REPLY_CHANGE_PSEUDO.length());
				String[] ansArray = pseudos.split(MessageCode.SEP_CHANGE_PSEUDO);
				String boolValue = ansArray[0];
				String distPseudo = ansArray[1];
				
				boolean validity = Boolean.parseBoolean(boolValue);
				System.out.println("Validity = " + validity + "src = " + sock.getPort());
				client.setValidPseudo(client.isValidPseudo() && validity);
				client.getM_IP_Pseudo_Table().put(sock.getPort(), distPseudo);
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		//If valid , send pseudo to everyone
		if(client.isValidPseudo()) {
			for(Socket sock : client.getSocketList()) {
				PrintWriter out;
				try {
					out = new PrintWriter(sock.getOutputStream(),true);
					out.println(MessageCode.NOTIFY_CHANGE_PSEUDO + client.getPseudo()); //println beacause BufferedReadder.readLine() expect a '\n'
					out.flush();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		
		client.getSocketList().clear();
		
	}
	
	public void send(DatagramPacket p) {
		
		try {
			m_UDP_socket.send(p);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
