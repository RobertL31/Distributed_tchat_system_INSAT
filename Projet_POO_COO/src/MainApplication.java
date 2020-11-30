import java.sql.Time;
import java.util.Scanner;

import distantApp.NetworkManager;
import localApp.LocalSystemConfig;

public class MainApplication {

	public static void main(String[] args) {
		NetworkManager Client = new NetworkManager();
		Scanner input = new Scanner(System.in);
		
		System.out.println("[UDP] Listening on " + LocalSystemConfig.get_UDP_port());
		System.out.println("[TCP] Listening on " + LocalSystemConfig.get_TCP_port());
		
		Client.discoverNetwork();
		
		
		
		while(true)
		{
			System.out.println("Message: ");
			String m = input.nextLine();
			Client.setPseudo(m);
			Client.getM_sender().sendPseudoRequest();
			System.out.println(Client.getM_IP_Pseudo_Table().toString());
		}

	}

}
