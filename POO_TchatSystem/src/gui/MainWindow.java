package gui;

import java.awt.GridBagLayout;
import java.awt.GridLayout;

import javax.swing.JFrame;

import config.GUIConfig;
import config.LocalSystemConfig;
import network.NetworkManager;

public class MainWindow extends JFrame{

	
	GridBagLayout grid;
	PseudoPanel pseudoP;
	NetworkManager client;
	
	public MainWindow() {
		super();
		this.client = LocalSystemConfig.getNetworkManagerInstance();
		setPreferredSize(GUIConfig.MAINWINDOW_DIMENSIONS);
		initComponents();
		pack();
		setVisible(true);
	}
	
	
	public void initComponents() {
		pseudoP = new PseudoPanel();
		add(pseudoP);
	}
}
