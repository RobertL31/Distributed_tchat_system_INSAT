package network;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;


public class TCPSender {
	
	
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
			//If send fail => target not connected => remove from the connected list
			int index = -1;
			for(int i=0; i<NetworkManager.connectedList.size(); i++) {
				if(port == NetworkManager.connectedList.get(i)) {
					index = i;
					break;
				}
			}
			NetworkManager.connectedList.remove(index);
			return false;
		}
	}

}
