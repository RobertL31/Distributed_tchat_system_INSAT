package gui;

import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;

import config.LocalSystemConfig;

public class User extends JPanel{
	
	private JLabel pseudo;
	private int port;
	private boolean newMessage;
	
	
	public User(int port) {
		super();
		this.pseudo = new JLabel(LocalSystemConfig.getNetworkManagerInstance().getPseudoFromPort(port));
		this.port = port;
		this.newMessage = false;
		pseudo.setBorder(BorderFactory.createLineBorder(Color.BLUE, 1));
		this.add(this.pseudo);
	}


	public JLabel getPseudoLabel() {
		return pseudo;
	}
	
	public String getPseudo() {
		return pseudo.getText();
	}

	public void setPseudo(JLabel pseudo) {
		this.pseudo = pseudo;
	}

	public int getPort() {
		return port;
	}

	public boolean isNewMessage() {
		return newMessage;
	}
	
	
	
}
