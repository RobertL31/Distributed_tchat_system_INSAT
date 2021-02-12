package network;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.util.HashMap;

import config.LocalSystemConfig;

/**
 * 
 * Provide tools to send UDP / TCP messages
 *
 */
public class Sender {
	// The UDP Socket to consider
	private DatagramSocket m_UDP_socket;
	// The user network manager 
	private NetworkManager client;

	public Sender() {
		m_UDP_socket = LocalSystemConfig.get_UDP_socket();
		this.client = LocalSystemConfig.getNetworkManagerInstance();
	}

	/**
	 * Create a datagram packet
	 * @param message the message to include in the datagram
	 * @param port the destination port
	 * @return the built datagram packet
	 * @throws IOException
	 */
	public static DatagramPacket createUDPDatagram(String message, int port) throws IOException {
		byte[] buff = message.getBytes();
		return new DatagramPacket(buff, buff.length, InetAddress.getLocalHost(), port);
	}

	/**
	 * Send a TCP message 
	 * @param msg the message to send
	 * @param port the destination port
	 * @return true if the message have been sent, false otherwise
	 */
	public static boolean send(String msg, int port){
		Socket sock;
		int dstPort = port;
		
		try {
			if (port == LocalSystemConfig.PRESENCE_SERVER_PORT) return false;
			if(port > 100000) {
				dstPort = LocalSystemConfig.PRESENCE_SERVER_PORT;
				msg = (port - 100000) + MessageCode.SEP + msg;
			}
			sock = new Socket(InetAddress.getLocalHost(), dstPort);
			PrintWriter out = new PrintWriter(sock.getOutputStream(),true);
			out.println(msg);
			out.flush();
			sock.close();
			return true;
		} catch (IOException e) {
			//If send fail => target not connected => remove from the connected list
			LocalSystemConfig.getNetworkManagerInstance().getM_IP_Pseudo_Table().remove(port);
			return false;
		}
	}

	@SuppressWarnings("unchecked")
	/**
	 * Send a request to change pseudo
	 * @return true if the pseudo have been granted
	 */
	public boolean sendPseudoRequest() {
		//Send chosen pseudo to all the network
		//Clone the HashMap to avoid modifications conflicts during forEach
		HashMap<Integer, String> users;
		users = (HashMap<Integer, String>) client.getM_IP_Pseudo_Table().clone();
		users.forEach((port, distPseudo) -> {
			String message = MessageCode.ASK_CHANGE_PSEUDO 
					+ LocalSystemConfig.get_TCP_port()
					+ MessageCode.SEP 
					+ client.getPseudo();
			Sender.send(message, port);
		});
		
		try {
			//Wait for answer (True / False)
			Thread.sleep(LocalSystemConfig.SLEEP_TIME);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		if(client.isValidPseudo()) {
			//If the pseudo have been granted, tell everyone chosen pseudo
			users = (HashMap<Integer, String>) client.getM_IP_Pseudo_Table().clone();
			users.forEach((port, pseudo) -> 
			{
				String message = MessageCode.NOTIFY_CHANGE_PSEUDO 
						+ LocalSystemConfig.get_TCP_port() 
						+ MessageCode.SEP 
						+ client.getPseudo();
				send(message, port);
			});
			return true;
		}

		return false;

	}

	/**
	 * Send a datagram packet
	 * @param p the datagram packet to send
	 */
	public void send(DatagramPacket p) {
		try {
			m_UDP_socket.send(p);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
