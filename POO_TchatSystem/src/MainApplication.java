import javax.swing.JLabel;
import javax.swing.JOptionPane;
import config.LocalSystemConfig;
import gui.MainWindow;
import network.NetworkManager;

public class MainApplication {
	

	public static void main(String[] args) {
		
		
		boolean isInside = true;
		
		// Pop-up asking the user if he is inside or outside the company network
		int result = JOptionPane.showConfirmDialog(null, new JLabel("Êtes-vous sur le réseau de l'entreprise ?"), "Réseau local ?", JOptionPane.YES_NO_OPTION);
		if(result == JOptionPane.NO_OPTION) {isInside = false;}
		
		// Initialize network manager
		LocalSystemConfig.initialize(!isInside);
		
		// Get the network manager
		NetworkManager client = LocalSystemConfig.getNetworkManagerInstance();
		
		// Initialize GUI
		@SuppressWarnings("unused") //No need to use it
		MainWindow app = new MainWindow();
		
		System.out.println("[UDP] Listening on " + LocalSystemConfig.get_UDP_port());
		System.out.println("[TCP] Listening on " + LocalSystemConfig.get_TCP_port());

		client.discoverNetwork();
		
		while(true);
		
		

	}

}
