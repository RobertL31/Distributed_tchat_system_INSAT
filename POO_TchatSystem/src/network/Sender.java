package network;

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
import java.util.HashMap;

import com.mysql.cj.xdevapi.Client;

import config.LocalSystemConfig;

public class Sender {

	private DatagramSocket m_UDP_socket;
	private NetworkManager client;

	public Sender() {
		m_UDP_socket = LocalSystemConfig.get_UDP_socket();
		this.client = LocalSystemConfig.getNetworkManagerInstance();
	}

	public static DatagramPacket createUDPDatagram(String message, int port) throws IOException {
		byte[] buff = message.getBytes();
		return new DatagramPacket(buff, buff.length, InetAddress.getLocalHost(), port);
	}

	public static boolean send(String msg, int port){
		Socket sock;
		int dstPort = port;
		
		try {
			if(port > 100000) {
				dstPort = LocalSystemConfig.PRESENCE_SERVER_PORT;
				msg += MessageCode.SEP + port;
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
			Thread.sleep(LocalSystemConfig.SLEEP_TIME);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		if(client.isValidPseudo()) {
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

	public void send(DatagramPacket p) {

		try {
			m_UDP_socket.send(p);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
