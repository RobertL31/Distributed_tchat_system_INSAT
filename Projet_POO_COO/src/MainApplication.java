import java.sql.Time;
import java.util.Scanner;

import distantApp.NetworkManager;
import localApp.LocalSystemConfig;

public class MainApplication {

	public static void main(String[] args) {
		NetworkManager client = new NetworkManager();
		Scanner input = new Scanner(System.in);

		System.out.println("[UDP] Listening on " + LocalSystemConfig.get_UDP_port());
		System.out.println("[TCP] Listening on " + LocalSystemConfig.get_TCP_port());

		client.discoverNetwork();



		while(true)
		{
			System.out.println("cmd: printlist | pseudo");
			String in = input.next();
			if(in.equals("printlist"))
				System.out.println(client.getM_IP_Pseudo_Table().toString());

			else if(in.equals("pseudo")) {
				System.out.print("Pseudo: ");
				String pseudo = input.next();
				while(!client.choosePseudo(pseudo)) {
					System.out.println("Already used !");
					pseudo = input.next();
				}
				System.out.println("Pseudo OK: " + client.getPseudo());
			}
		}

	}

}
