package distantApp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.util.HashMap;

import localApp.LocalSystemConfig;

public class NetworkManager {

	private HashMap<Integer, String> m_IP_Pseudo_Table;
	private Network_Sender m_sender;
	private Network_receiver_UDP m_receiver_UDP;


	public NetworkManager() {
		super();

		//Open a listening UDP port
		LocalSystemConfig.openUDPServer();
		LocalSystemConfig.openTCPServer();

		this.m_IP_Pseudo_Table = new HashMap<Integer, String>();
		this.m_sender = new Network_Sender();
		this.m_receiver_UDP = new Network_receiver_UDP(this);

	}

	public HashMap<Integer, String> getM_IP_Pseudo_Table() {
		return m_IP_Pseudo_Table;
	}
	
	public Network_Sender getM_sender() {
		return m_sender;
	}

	private void initReceiver_UDP() {
		m_receiver_UDP.start();
	}

	public void discoverNetwork() {
		//Launch receiver thread
		initReceiver_UDP();
		
		//Send a UDP datagram to notify all the network
		String msg = MessageCode.NOTIFY_JOIN;
		for(int i=LocalSystemConfig.START_PORT; i<=LocalSystemConfig.END_PORT; ++i) {
			if(i != LocalSystemConfig.get_UDP_port()) {
				try {
					System.out.println("Send UDP to " + i);
					m_sender.send(Network_Sender.createUDPDatagram(msg, i));
				} catch (IOException e) {
					System.err.println("Cannot send UDP datagram to " + i);
				}
			}
		}
	}

	


}
