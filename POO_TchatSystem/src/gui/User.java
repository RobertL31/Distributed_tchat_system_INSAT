package gui;

import java.awt.Color;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import config.GUIConfig;
import config.LocalSystemConfig;

/**
 * 
 * UI to display a user (in the user connected list)
 *
 */
@SuppressWarnings("serial")
public class User extends JLabel{
	// The user port
	private int port;
	//The user pseudo
	private String pseudo = LocalSystemConfig.getNetworkManagerInstance().getPseudoFromPort(port);
	
	/**
	 * Create a user UI
	 * @param port the port number of the user
	 */
	public User(int port) {
		super("<html><div style='text-align: center;'>" 
				+ LocalSystemConfig.getNetworkManagerInstance().getPseudoFromPort(port)
				+ "</div></html>", SwingConstants.CENTER);
		this.pseudo = LocalSystemConfig.getNetworkManagerInstance().getPseudoFromPort(port);
		this.setPreferredSize(GUIConfig.USR_PANEL_DIM);
		this.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
		this.port = port;
	}
	
	/**
	 * 
	 * @return the user port
	 */
	public int getPort() {
		return port;
	}

	/**
	 * 
	 * @return the user pseudo 
	 */
	public String getPseudo() {
		return pseudo;
	}

	/**
	 * Stylize the user pseudo (To center it on the JLabel) 
	 * @param pseudo the pseudo to set
	 */
	public void setPseudo(String pseudo) {
		this.pseudo = pseudo;
		this.setText("<html><div style='text-align: center;'>" 
				+ pseudo
				+ "</div></html>");
	}

	
}
