package gui;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.security.UnrecoverableKeyException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import config.GUIConfig;
import config.LocalSystemConfig;
import database.Conversation;

@SuppressWarnings("serial")
public class ConnectedListPanel extends JPanel{

	private JScrollPane scrollPane;
	private JPanel listPanel;
	private ArrayList<User> connectedList;
	private ConversationPanel conversationPanel;
	private Timer timer;


	public ConnectedListPanel(ConversationPanel conversationPanel) {
		super();		
		this.conversationPanel = conversationPanel;
		this.setPreferredSize(GUIConfig.CONNECTED_PANEL_DIM);
		listPanel = new JPanel();
		listPanel.setLayout(new GridLayout(0, 1, 0, 2));
		listPanel.setBounds(0, 0, GUIConfig.CONNECTED_PANEL_W, GUIConfig.CONNECTED_PANEL_H);
		scrollPane = new JScrollPane(listPanel);
		scrollPane.setPreferredSize(GUIConfig.CONNECTED_PANEL_DIM);
		connectedList = new ArrayList<User>();
		this.add(scrollPane);

		// Update list
		timer = new Timer();
		timer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				reloadList();
			}
		}, 0, 250);


	}

	public void reloadList() {
		if(!LocalSystemConfig.getNetworkManagerInstance().getPseudo().equals(LocalSystemConfig.UNKNOWN_USERNAME)) {
			ArrayList<Integer> actualizedList = new ArrayList<Integer>(LocalSystemConfig.getNetworkManagerInstance().getM_IP_Pseudo_Table().keySet());
			@SuppressWarnings("unchecked")
			ArrayList<User> connectedListClone = (ArrayList<User>) connectedList.clone();

			//Remove disconnected users
			for(User user : connectedListClone) {
				if(!actualizedList.contains(user.getPort())) {
					if(conversationPanel.getConversation() != null 
							&& user.getPort() == conversationPanel.getConversation().getDestIP()) {
						conversationPanel.getShownMessagePanel().add(
								new JLabel("l'utilisateur s\'est déconnecté, vous ne pouvez plus communiquer avec elle/lui"
										));
					}
					connectedList.remove(user);
					listPanel.remove(user);
				}
			}

			ArrayList<Integer> connectedPortList = new ArrayList<Integer>();
			//init connectedPortList
			for(User user : connectedList) {
				connectedPortList.add(user.getPort());
			}

			
			for(int userPort : actualizedList) {
				//Add new users
				if(!connectedPortList.contains(userPort) 
						&& !LocalSystemConfig.getNetworkManagerInstance().getPseudoFromPort(userPort).equals(LocalSystemConfig.UNKNOWN_USERNAME)) {
					User newUser = new User(userPort);
					newUser.addMouseListener(
							new MouseAdapter()  
							{  
								public void mouseClicked(MouseEvent e)  
								{  
									Conversation c = LocalSystemConfig.getNetworkManagerInstance().getConvManager().getConversation(userPort);
									conversationPanel.setConversation(c);

								}
							});
					
					
					connectedList.add(newUser);
					listPanel.add(newUser);
					
					newUser.setBounds(0, connectedList.size()*GUIConfig.USR_PANEL_H, GUIConfig.USR_PANEL_W, GUIConfig.USR_PANEL_H);
					
				}
				//Update pseudos
				else{
					User userShown = null;
					String actualizedPseudo = LocalSystemConfig.getNetworkManagerInstance().getPseudoFromPort(userPort);
					for(User usr : connectedList) {
						if(usr.getPort() == userPort) {
							userShown = usr;
							break;
						}
					}
					if(userShown != null 
							&& !actualizedPseudo.equals(userShown.getPseudo())) {
						userShown.setPseudo(actualizedPseudo);
					}
				}
			}

			updateUI();

		}
	}
}
