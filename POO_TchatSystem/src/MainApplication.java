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
		
		// Popup asking the user if he is inside or outside the company network
		int result = JOptionPane.showConfirmDialog(null, new JLabel("Êtes-vous sur le réseau de l'entreprise ?"), "Réseau local ?", JOptionPane.YES_NO_OPTION);
		if(result == JOptionPane.NO_OPTION) {isInside = false;}
		
		// Initialize network manager
		LocalSystemConfig.initialize(!isInside);
		
		// Get the network manager
		NetworkManager client = LocalSystemConfig.getNetworkManagerInstance();
		
		// Initialize GUI
		MainWindow app = new MainWindow();
		
		System.out.println("[UDP] Listening on " + LocalSystemConfig.get_UDP_port());
		System.out.println("[TCP] Listening on " + LocalSystemConfig.get_TCP_port());

		client.discoverNetwork();
		
		while(true);

	}

}
