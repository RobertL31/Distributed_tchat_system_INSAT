package gui;

import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import network.NetworkManager;

public class MainWindow extends JFrame{
	
	PseudoWindow pseudoWin;
	NetworkManager client;
	
	public MainWindow(NetworkManager client) {
		super(GUIConfig.APP_NAME);
		pseudoWin = new PseudoWindow(client);
		this.client = client;
		this.setPreferredSize(GUIConfig.MAINWINDOW_DIMENSIONS);
		this.pack();
		this.setVisible(true);
	}
	
	public void showPseudoWindow() {
		pseudoWin.showWin();
	}
	
}
