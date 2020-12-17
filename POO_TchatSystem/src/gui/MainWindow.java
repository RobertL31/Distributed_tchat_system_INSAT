package gui;

import java.awt.BorderLayout;
import java.awt.GridBagLayout;
import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JLabel;

import config.GUIConfig;
import config.LocalSystemConfig;
import network.NetworkManager;

public class MainWindow extends JFrame{

	
	GridBagLayout grid;
	NetworkManager client;
	
	PseudoPanel pseudoP;
	ConversationPanel conversationP;
	ConnectedListPanel listP;
	
	
	public MainWindow() {
		super();
		this.client = LocalSystemConfig.getNetworkManagerInstance();
		setPreferredSize(GUIConfig.MAINWINDOW_DIMENSIONS);
		
		initComponents();
		pack();
		setVisible(true);
	}
	
	public void test() {
		listP.reloadList();
	}
	
	
	public void initComponents() {
		
		pseudoP = new PseudoPanel();
		conversationP = new ConversationPanel();
		listP = new ConnectedListPanel(conversationP);
		
		add(pseudoP, BorderLayout.NORTH);
		add(listP);
		add(conversationP, BorderLayout.EAST);
		
		
	}
}
