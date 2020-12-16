package gui;

import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import config.LocalSystemConfig;
import database.Conversation;
import network.NetworkManager;

public class ConnectedListPanel extends JPanel{
	
	private JScrollPane scrollPane;
	private JPanel listPanel;
	private ArrayList<JLabel> connectedList;
	private ConversationPanel conversationPanel;
	
	public ConnectedListPanel(ConversationPanel conversationPanel) {
		super();		
		this.conversationPanel = conversationPanel;
		this.setPreferredSize(new Dimension(300, 500));
		listPanel = new JPanel();
		scrollPane = new JScrollPane(listPanel);
		scrollPane.setPreferredSize(new Dimension(300, 500));
		connectedList = new ArrayList<JLabel>();
		this.add(scrollPane);
	}
	
	public void reloadList() {
		NetworkManager client = LocalSystemConfig.getNetworkManagerInstance();
		ArrayList<String> pseudoList =  new ArrayList<String>(client.getM_IP_Pseudo_Table().values());
		int i;
		boolean found;
		for(JLabel l : connectedList) {
			i=0;
			found = false;
			for(String pseudo : pseudoList) {
				if(pseudo.equals(l.getText())) {
					pseudoList.remove(i);
					found = true;
				}
				i++;
			}
			if(!found) {
				listPanel.remove(l);
				connectedList.remove(l);
			}
		}
		
		for(String pseudo : pseudoList) {
			JLabel toAdd = new JLabel(pseudo);
			toAdd.addMouseListener(new MouseAdapter()  
			{  
			    public void mouseClicked(MouseEvent e)  
			    {  
			    	int port = client.getPortFromPseudo(pseudo);
			    	Conversation c = client.getConvManager().getConversation(port);
			    	conversationPanel.setConversation(c);
			    	
			    }
			});
			connectedList.add(toAdd);
			listPanel.add(toAdd);
		}
		updateUI();
	}
	
}
