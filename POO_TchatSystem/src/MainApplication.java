import java.sql.Time;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import config.LocalSystemConfig;
import database.Conversation;
import gui.MainWindow;
import network.NetworkManager;

public class MainApplication {
	

	public static void main(String[] args) {
		
		
		boolean isInside = true;
		
		int result = JOptionPane.showConfirmDialog(null, new JLabel("Êtes-vous sur le réseau de l'entreprise ?"), "Réseau local ?", JOptionPane.YES_NO_OPTION);
		if(result == JOptionPane.NO_OPTION) {isInside = false;}
		
		
		LocalSystemConfig.initialize(!isInside);
		
		
		NetworkManager client = LocalSystemConfig.getNetworkManagerInstance();
		
		///////// GUI Version //////////
		 
		MainWindow app = new MainWindow();
		
		////////////////////////////////
		
		
		///////// Command Line Version //////////////
		
		Scanner input = new Scanner(System.in);

		System.out.println("[UDP] Listening on " + LocalSystemConfig.get_UDP_port());
		System.out.println("[TCP] Listening on " + LocalSystemConfig.get_TCP_port());

		client.discoverNetwork();


		System.out.println("cmd: printlist | pseudo | send | printconv | disconnect | test | up");
		while(true)
		{
			
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
			else if(in.equals("send")) {
				System.out.print("target pseudo: ");
				String target = input.next();
				String msg = input.next();
				
				client.sendPM(target, msg);
			}
			else if(in.equals("printconv")) {
				System.out.println(client.getConvManager().toString());
			}
			
			else if(in.equals("disconnect")) {
				client.disconnect();
				System.exit(1);
			}
			
			else if(in.equals("test")) {
				app.test();
			}
			else if(in.equals("up")) {
				app.test2();
			}
			
		}

	}

}
