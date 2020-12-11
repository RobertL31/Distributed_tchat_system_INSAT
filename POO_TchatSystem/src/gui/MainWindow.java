package gui;

import java.awt.GridBagLayout;
import java.awt.GridLayout;

import javax.swing.JFrame;

import config.GUIConfig;

public class MainWindow extends JFrame{

	
	GridBagLayout grid;
	PseudoPanel pseudoP;
	
	public MainWindow() {
		super();
		
		setPreferredSize(GUIConfig.MAINWINDOW_DIMENSIONS);
		grid = new GridBagLayout();
		
		
		initComponents();
		pack();
		setVisible(true);
	}
	
	
	public void initComponents() {
		pseudoP = new PseudoPanel();
		add(pseudoP);
	}
}
