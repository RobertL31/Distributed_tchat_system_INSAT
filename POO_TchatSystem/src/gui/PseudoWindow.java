package gui;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import network.NetworkManager;

public class PseudoWindow extends JFrame implements ActionListener{

	private JPanel container;
	private JLabel info;
	private TextArea inputArea;
	private JButton sendButton;
	private NetworkManager client;
	
	public PseudoWindow(NetworkManager client) {
		super(GUIConfig.PSEUDO_WIN_NAME);
		this.client = client;
		this.container = new JPanel(); 
		this.info = new JLabel();
		this.inputArea = new TextArea();
		this.sendButton = new JButton();
		this.init();
	}
	
	
	private void init(){
		this.setSize(GUIConfig.PSEUDO_WINDOW_DIMENSIONS);
		this.setResizable(false);
		this.sendButton.addActionListener(this);
		info.setText("Entrer un pseudo");
		sendButton.setText("Envoyer");
		container.setLayout(new GridLayout(3, 1));
		container.add(info);
		container.add(inputArea);
		container.add(sendButton);
		this.setContentPane(container);
	}
	
	public void showWin() {
		this.pack();
		this.setVisible(true);
	}
	
	public void hideWin() {
		this.setVisible(false);
	}


	@Override
	public void actionPerformed(ActionEvent arg0) {
		String input = inputArea.getText();
		if(client.choosePseudo(input)) {
			info.setText("Entrer un pseudo");
			hideWin();
		}
		else {
			info.setText("Pseudo invalide, essayez en un autre !");
		}
	}
}
