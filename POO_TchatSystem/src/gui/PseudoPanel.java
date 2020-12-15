package gui;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import config.GUIConfig;
import config.LocalSystemConfig;

public class PseudoPanel extends JPanel implements ActionListener{
	private GridLayout grid;
	private JTextField input;
	private JButton sendButton;
	private ActionListener sendButtonListener;
	private JLabel placeholder;
	private JLabel actualPseudo;
	
	public PseudoPanel() {
		super();
		setSize(GUIConfig.PSEUDO_PANEL_DIM);
		input = new JTextField();
		input.setPreferredSize(new Dimension(100, 20));
		placeholder = new JLabel("Pseudo:");
		sendButton = new JButton(GUIConfig.PSEUDO_BUTTON_TXT);
		sendButton.addActionListener(this);
		actualPseudo = new JLabel("Vous n'avez pas encore de pseudo");
		add(placeholder);
		add(input);
		add(sendButton);
		add(actualPseudo);
	}
	
	private boolean checkPseudo(String pseudo) {
		return pseudo.length() >= GUIConfig.MIN_PSEUDO_LEN 
				&& pseudo.length() <= GUIConfig.MAX_PSEUDO_LEN
				&& pseudo.matches("[A-Za-z0-9]+"); //Allow alphanumeric char only
	}

	@Override
	public void actionPerformed(ActionEvent evt) {
		Object performer = evt.getSource();
		if(performer == sendButton) {
			String pseudo = input.getText();
			if(checkPseudo(pseudo)) {
				//Pseudo OK
				if(LocalSystemConfig.getNetworkManagerInstance().choosePseudo(pseudo)) {
					placeholder.setText("Pseudo:");
					actualPseudo.setText("Votre pseudo: " + LocalSystemConfig.getNetworkManagerInstance().getPseudo());
				}
				//Pseudo Already Used
				else {
					placeholder.setText("Pseudo Invalide !");
				}
			}
			//Illegal char(s) in pseudo
			else {
				placeholder.setText("Le pseudo ne peut contenir que des alphanumériques, entre 2-15 caractères:");
			}
		}
	}
}


//Pseudo : [   INPUT FIELD    ] (Boutton)