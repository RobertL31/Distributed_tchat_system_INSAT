package distantApp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

import localApp.LocalSystemConfig;

public class NetworkManager {
	
	private HashMap<Integer, String> m_IP_Pseudo_Table;
	public ArrayList<Socket> socketList;
	private Network_Sender m_sender;
	private Network_receiver_UDP m_receiver_UDP;
	private TCPAccepter accepter;
	
	private boolean validPseudo = true;
	
	private String pseudo = LocalSystemConfig.UNKNOWN_USERNAME;
	
	


	public NetworkManager() {
		super();

		//Open a listening UDP port
		LocalSystemConfig.openUDPServer();
		LocalSystemConfig.openTCPServer();

		this.m_IP_Pseudo_Table = new HashMap<>();
		this.socketList = new ArrayList<Socket>();
		this.m_sender = new Network_Sender(this);
		this.m_receiver_UDP = new Network_receiver_UDP(this);
		this.accepter = new TCPAccepter(this);
		
		//Launch UDP receiver thread
		initThreads();
	}

	public HashMap<Integer, String> getM_IP_Pseudo_Table() {
		return m_IP_Pseudo_Table;
	}

	public ArrayList<Socket> getSocketList() {
		return socketList;
	}

	public String getPseudo() {
		return pseudo;
	}

	public void setPseudo(String pseudo) {
		this.pseudo = pseudo;
	}

	public Network_Sender getM_sender() {
		return m_sender;
	}

	private void initThreads() {
		accepter.start();
		m_receiver_UDP.start();
	}

	public void discoverNetwork() {
		
		//Send a UDP datagram to notify all the network
		String msg = MessageCode.NOTIFY_JOIN;
		for(int i=LocalSystemConfig.START_PORT; i<=LocalSystemConfig.END_PORT; ++i) {
			if(i != LocalSystemConfig.get_UDP_port()) {
				try {
					m_sender.send(Network_Sender.createUDPDatagram(msg, i));
				} catch (IOException e) {
					System.err.println("Cannot send UDP datagram to " + i);
				}
			}
		}
	}
	
	public boolean choosePseudo(String pseudo) {
		this.setPseudo(pseudo);
		boolean ret = m_sender.sendPseudoRequest();
		if(!ret) this.setPseudo(LocalSystemConfig.UNKNOWN_USERNAME);
		this.setValidPseudo(true);
		return ret;
	}

	public boolean isValidPseudo() {
		return validPseudo;
	}

	public void setValidPseudo(boolean validPseudo) {
		this.validPseudo = validPseudo;
	}

	


}
