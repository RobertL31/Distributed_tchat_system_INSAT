import java.util.Scanner;

import distantApp.NetworkManager;
import localApp.LocalSystemConfig;

public class MainApplication {

	public static void main(String[] args) {
		NetworkManager Client = new NetworkManager();
		Scanner input = new Scanner(System.in);
		
		System.out.println("[UDP] Listening on " + LocalSystemConfig.get_UDP_port());
		
		Client.discoverNetwork();
		
		
				
		while(true);

	}

}
