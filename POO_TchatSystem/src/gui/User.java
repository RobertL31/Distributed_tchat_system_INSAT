package gui;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

import config.GUIConfig;
import config.LocalSystemConfig;

public class User extends JLabel{
	
	private int port;
	private String pseudo = LocalSystemConfig.getNetworkManagerInstance().getPseudoFromPort(port);
	
	public User(int port) {
		super("<html><div style='text-align: center;'>" 
				+ LocalSystemConfig.getNetworkManagerInstance().getPseudoFromPort(port)
				+ "</div></html>", SwingConstants.CENTER);
		this.pseudo = LocalSystemConfig.getNetworkManagerInstance().getPseudoFromPort(port);
		this.setPreferredSize(GUIConfig.USR_PANEL_DIM);
		this.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
		this.port = port;
	}
	

	public int getPort() {
		return port;
	}


	public String getPseudo() {
		return pseudo;
	}


	public void setPseudo(String pseudo) {
		this.pseudo = pseudo;
		this.setText("<html><div style='text-align: center;'>" 
				+ pseudo
				+ "</div></html>");
	}

	
}
