package gui;

import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import config.GUIConfig;

public class PseudoPanel extends JPanel{
	private GridLayout grid;
	private JTextField input;
	private JButton sendButton;
	
	public PseudoPanel() {
		super();
		setSize(GUIConfig.PSEUDO_PANEL_DIM);
		grid = new GridLayout(1, 3);
		this.setLayout(grid);

		input = new JTextField();
		sendButton = new JButton(GUIConfig.PSEUDO_BUTTON_TXT);

		add(new JLabel("Choisir pseudo"));
		add(input);
		add(sendButton);
		
		
	}
}




//Pseudo : [   INPUT FIELD    ] (Boutton)