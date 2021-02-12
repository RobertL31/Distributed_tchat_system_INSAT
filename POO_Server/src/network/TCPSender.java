package network;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

/**
 * 
 * Tool to send a message thought TCP
 *
 */
public class TCPSender {
	
	/**
	 * Send a TCP message
	 * @param msg the message to send 
	 * @param port the destination port
	 * @return true if the message have been sent, false otherwise
	 */
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
			
			return false;
		}
	}

}
