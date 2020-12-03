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

	public static boolean send(String msg, int port){
		Socket sock;
		try {
			sock = new Socket(InetAddress.getLocalHost(), port);
			PrintWriter out = new PrintWriter(sock.getOutputStream(),true);
			out.println(msg);
			out.flush();
			sock.close();
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean sendPseudoRequest() {
		//Send chosen pseudo to all the network
		client.getM_IP_Pseudo_Table().forEach((port, distPseudo) -> {
			String message = MessageCode.ASK_CHANGE_PSEUDO 
					+ LocalSystemConfig.get_TCP_port()
					+ MessageCode.SEP 
					+ client.getPseudo();
			Network_Sender.send(message, port);
		});

		try {
			Thread.sleep(LocalSystemConfig.SLEEP_TIME);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		if(client.isValidPseudo()) {
			client.getM_IP_Pseudo_Table().forEach((port, pseudo) -> 
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
