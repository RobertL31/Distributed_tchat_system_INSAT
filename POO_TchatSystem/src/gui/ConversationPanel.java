package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ListModel;

import config.LocalSystemConfig;
import database.Conversation;
import database.Message;

@SuppressWarnings("serial")
public class ConversationPanel extends JPanel implements ActionListener{

	private Conversation conversation = null;
	private JPanel shownMessagePanel;
	private JScrollPane messageScrollPane;

	private JPanel sendMessagePanel;
	private JTextArea messageTextArea;
	private JScrollPane messageAreaScrollPane;
	private JButton sendButton;



	public ConversationPanel() {
		super();
		this.setPreferredSize(new Dimension( 600, 600));

		//Conversation area
		shownMessagePanel = new JPanel();
		shownMessagePanel.setBackground(Color.WHITE);
		messageScrollPane = new JScrollPane(shownMessagePanel);
		messageScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		messageScrollPane.setPreferredSize(new Dimension(500, 300));
		messageScrollPane.setAutoscrolls(true);

		//Send message Areas
		sendMessagePanel = new JPanel();
		sendMessagePanel.setPreferredSize(new Dimension(500, 200));
		messageTextArea = new JTextArea("");
		messageTextArea.setPreferredSize(new Dimension(300, 50));
		messageTextArea.setLineWrap(true);
		sendButton = new JButton("Envoyer");
		sendButton.addActionListener(this);
		messageAreaScrollPane = new JScrollPane(messageTextArea);
		messageAreaScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		sendMessagePanel.add(messageAreaScrollPane);
		sendMessagePanel.add(sendButton);
		//Add components
		this.add(messageScrollPane);
		this.add(sendMessagePanel);
		
		parser("bonjour je suis\nici ");
	}

	public void setConversation(Conversation c) {
		this.conversation = c;
		loadConversation();
	}
	
	//\n tous les 30 caractères 
	public ArrayList<String> parser(String message) {
		int cnt=0;
		String tmpLine="";
		String[] splited = message.split("[\\s]");
		ArrayList<String> res = new ArrayList<String>();
		for(String s : splited) {
			if(cnt+s.length()+1<30) {
				cnt+=s.length();
				tmpLine+=s + " ";
				
			}
			else {
				res.add(new String(tmpLine));
				tmpLine = "";
				cnt=0;
			}
		}
		
		if(cnt < 30){
			res.add(new String(tmpLine));
		}
		
		return res;
	}

	public void loadConversation() {
		if(conversation != null) {
			int coeff = 40;
			shownMessagePanel.removeAll();
			for(Message m : conversation.getMessages()) {
				ArrayList<String> parsedMessage = parser(m.getContent());
				String finalMessage="<html>"
						+ LocalSystemConfig.getNetworkManagerInstance().getPseudoFromPort(m.getSrc()) + ": ";
				for(String s : parsedMessage) {
					finalMessage+=s + "<br/>";
				}
				finalMessage += "</html>";
				
				Color c = (m.getSrc()==LocalSystemConfig.get_TCP_port())?Color.BLUE:Color.BLACK;
				
				JLabel l = new JLabel(finalMessage);
				int mSize = coeff*parsedMessage.size();
				l.setForeground(c);			
				l.setPreferredSize(new Dimension(500, mSize));
				shownMessagePanel.add(l);
				
				shownMessagePanel.setPreferredSize(new Dimension(500, shownMessagePanel.getHeight()+mSize));
			}
			JScrollBar sb = messageScrollPane.getVerticalScrollBar();
			sb.setValue(sb.getMaximum());
			updateUI();
		}
	}	


	@Override
	public void actionPerformed(ActionEvent evt) {
		Object performer = evt.getSource();
		String text = messageTextArea.getText();
		if(performer == sendButton && conversation != null && text.length() > 0) {
			conversation.send(text);
			messageTextArea.setText("");
			loadConversation();
		}

	}



}
