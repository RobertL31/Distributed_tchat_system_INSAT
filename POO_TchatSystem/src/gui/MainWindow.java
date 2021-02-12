package gui;

import java.awt.BorderLayout;
import java.awt.GridBagLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import config.GUIConfig;
import config.LocalSystemConfig;
import network.NetworkManager;

/**
 * 
 * Main window of the application
 *
 */
@SuppressWarnings("serial")
public class MainWindow extends JFrame{

	
	GridBagLayout grid;
	NetworkManager client;
	
	PseudoPanel pseudoP;
	ConversationPanel conversationP;
	ConnectedListPanel listP;
	
	/**
	 * Create a MainWindow
	 */
	public MainWindow() {
		super();
		this.client = LocalSystemConfig.getNetworkManagerInstance();
		setPreferredSize(GUIConfig.MAINWINDOW_DIMENSIONS);
		
		initComponents();
		pack();
		setVisible(true);
	}
	
	/**
	 * Initialize all the MainWindow components needed (PseudoPanel, ConversationPanel, ConnectedListPanel)
	 */
	public void initComponents() {
		
		pseudoP = new PseudoPanel();
		conversationP = new ConversationPanel();
		listP = new ConnectedListPanel(conversationP);
		
		add(pseudoP, BorderLayout.NORTH);
		add(listP);
		add(conversationP, BorderLayout.EAST);
		
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent event) {
		        exitProgram();
		    }
		});
	}
	
	/**
	 * Close the GUI and kill the application
	 */
	private void exitProgram() {
		this.dispose();
		client.disconnect();
		System.exit(0);
	}
}
