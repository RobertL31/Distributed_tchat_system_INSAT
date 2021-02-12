package network;

import java.io.IOException;
import java.util.HashMap;
import java.util.Set;

import config.DatabaseConfig;
import config.LocalSystemConfig;
import database.ConversationManager;

/**
 * 
 * Network manager provides high-level tools to communicate through the network
 *
 */
public class NetworkManager {

	// The hashmap [port : pseudo]
	public HashMap<Integer, String> m_IP_Pseudo_Table = new HashMap<>();
	// The sender (to send UDP / TCP messages)
	private Sender m_sender;
	// Thread that handle UDP incoming connections
	private UDPReceiver m_receiver_UDP;
	// The corresponding conversation manager
	private ConversationManager convManager;
	// Thread that accept and handle TCP connections
	private TCPAccepter accepter;
	
	private boolean validPseudo = true;

	// User current pseudo
	private String pseudo = LocalSystemConfig.UNKNOWN_USERNAME;

	public boolean isExternal;


	/**
	 * Create a NetworkManager
	 * @param isExternal True if the user in outside the company, false otherwise
	 */
	public NetworkManager(boolean isExternal) {
		super();
		this.isExternal = isExternal;
	}
	
	/**
	 * Initialize network manager components and launch threads
	 */
	public void init() {

		//Open a listening UDP port
		LocalSystemConfig.openUDPServer();
		LocalSystemConfig.openTCPServer();

		//Connect to database (Move to ConversationManager)
		DatabaseConfig.connectToDatabase();

		
		this.m_sender = new Sender();
		this.m_receiver_UDP =  new UDPReceiver();
		this.accepter = new TCPAccepter();
		this.convManager = new ConversationManager();

		//Launch UDP/TCP receiver thread
		initThreads();
	}
	
	/**
	 * Launch UDP and TCP listener threads
	 */
	private void initThreads() {
		accepter.start();
		m_receiver_UDP.start();
	}

	
	/**
	 * 
	 * @return the port / pseudo hashmap
	 */
	public HashMap<Integer, String> getM_IP_Pseudo_Table() {
		return m_IP_Pseudo_Table;
	}
	
	/**
	 * 
	 * @return the user pseudo
	 */
	public String getPseudo() {
		return pseudo;
	}

	/**
	 * Set the user username
	 * @param pseudo the username to set
	 */
	public void setPseudo(String pseudo) {
		this.pseudo = pseudo;
	}
	
	/**
	 * 
	 * @return the ConversationManager
	 */
	public ConversationManager getConvManager() {
		return convManager;
	}

	/**
	 * 
	 * @return the user Sender
	 */
	public Sender getM_sender() {
		return m_sender;
	}
	
	/**
	 * 
	 * @return true if the user is outside the company network, false otherwise
	 */
	public boolean isExternal() {
		return isExternal;
	}

	/**
	 * Get the port number of a giver username
	 * @param pseudo the username to look for
	 * @return the port number associated to the given pseudo, -1 if not found
	 */
	public int getPortFromPseudo(String pseudo) {
		String username;
		Set<Integer> keys = m_IP_Pseudo_Table.keySet();
		for(int key : keys) {
			username = m_IP_Pseudo_Table.get(key);
			if(username.equals(pseudo))
				return key;
		}
		return -1;
	}
	
	/**
	 * Get the pseudo associated to a given port number 
	 * @param port the port number to consider
	 * @return the pseudo associated to the port
	 */
	public String getPseudoFromPort(int port) {
		if(port == LocalSystemConfig.get_TCP_port()) 
			return this.pseudo;
		return m_IP_Pseudo_Table.get(port);
	}

	
	/**
	 * Broadcast a UDP datagram through the broadcast range
	 * @param message the message to send
	 */
	public void UDPBroadcast(String message) {
		for(int i=LocalSystemConfig.START_PORT; i<=LocalSystemConfig.END_PORT; ++i) {
			//System.out.println("here, UDP sent to : " + i );
			if(i != LocalSystemConfig.get_UDP_port()) {
				try {
					m_sender.send(Sender.createUDPDatagram(message, i));
				} catch (IOException e) {
					System.err.println("Cannot send UDP datagram to " + i);
				}
			}
		}
		//send to the presence server
		try {
			m_sender.send(Sender.createUDPDatagram(message, LocalSystemConfig.PRESENCE_SERVER_PORT));
		} catch (IOException e) {
			System.err.println("Cannot send UDP to Presence server, external users will be ignored");
		}
	}

	/**
	 * Initiate the network discovery
	 */
	public void discoverNetwork() {
		//Send a UDP datagram to notify all the network
		String msg = MessageCode.NOTIFY_JOIN;
		UDPBroadcast(msg);
	}

	/**
	 * Warn the network about the user disconnection
	 */
	public void disconnect() {
		String msg = MessageCode.NOTIFY_LEAVE;
		UDPBroadcast(msg);
	}

	/**
	 * Choose a pseudo 
	 * @param pseudo the pseudo to choose
	 * @return true if the chosen pseudo have been granted
	 */
	public boolean choosePseudo(String pseudo) {
		this.setPseudo(pseudo);
		boolean ret = m_sender.sendPseudoRequest();
		if(!ret) this.setPseudo(LocalSystemConfig.UNKNOWN_USERNAME);
		this.setValidPseudo(true);
		return ret;
	}

	/**
	 * 
	 * @return true is the user pseudo is valid, false otherwise
	 */
	public boolean isValidPseudo() {
		return validPseudo;
	}

	/**
	 * Set the pseudo validity
	 * @param validPseudo the value to set
	 */
	public void setValidPseudo(boolean validPseudo) {
		this.validPseudo = validPseudo;
	}
	
	/**
	 * Send a private message 
	 * @param pseudo the target
	 * @param message the message to send
	 */
	public void sendPM(String pseudo, String message) {
		if(pseudo.equals(LocalSystemConfig.UNKNOWN_USERNAME)) {
			System.out.println("Illegal pseudo");
			return;
		}
		int destPort = this.getPortFromPseudo(pseudo);
		if(destPort != -1)
			convManager.send(message, destPort);
		else
			System.out.println("Can't find username");
	}



}
