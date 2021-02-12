package gui;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;

import config.GUIConfig;
import config.LocalSystemConfig;
import database.Conversation;
import database.Message;

/**
 * 
 * ConversationPanel (MainWindow right side), provides : read / send messages
 *
 */
@SuppressWarnings("serial")
public class ConversationPanel extends JPanel implements ActionListener{

	// The conversation to display
	private Conversation conversation;
	private JPanel shownMessagePanel;
	// Make it scrollable
	private JScrollPane messageScrollPane;
	// To check if day . month have already been displayed
	private long lastMessageTime;
	
	// To schedule GUI update
	private Timer timer;
	// To check new messages or not
	private int displayedMessagesNb;

	// Panel to send message (TextArea + send Button)
	private JPanel sendMessagePanel;
	private JTextArea messageTextArea;
	private JScrollPane messageAreaScrollPane;
	private JButton sendButton;

	/**
	 * Create a Conversation Panel
	 */
	public ConversationPanel() {
		super();
		
		this.setPreferredSize(GUIConfig.CONV_PANEL_DIM);
		
		//to update GUI
		lastMessageTime = 0;
		displayedMessagesNb = 0;
		
		timer = new Timer();
		
		//empty task
		timer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				if(conversation != null) {
					updateConversation();
				}
			}
		}, 0, 250);
		
		//Conversation area
		shownMessagePanel = new JPanel();
		shownMessagePanel.setLayout(new BoxLayout(shownMessagePanel, BoxLayout.Y_AXIS));
		shownMessagePanel.setBackground(Color.WHITE);
		shownMessagePanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
		messageScrollPane = new JScrollPane(shownMessagePanel);
		messageScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		messageScrollPane.setPreferredSize(GUIConfig.SP_CONV_PANEL_DIM);
		messageScrollPane.setAutoscrolls(true);
		shownMessagePanel.add(new JLabel("Cliquez sur un utilisateur pour démarrer une conversation !"));
		
		
		//Send message Areas
		sendMessagePanel = new JPanel();
		sendMessagePanel.setPreferredSize(GUIConfig.MA_CONV_PANEL_DIM);
		messageTextArea = new JTextArea("");
		messageTextArea.setPreferredSize(GUIConfig.MTA_CONV_PANEL_DIM);
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
		
	}
	
	/**
	 * Set and load a conversation to the panel
	 * @param c the conversation to set
	 */
	public void setConversation(Conversation c) {
		this.conversation = c;
		loadConversation();
	}
	
	/**
	 * 
	 * @return the displayed conversation
	 */
	public Conversation getConversation() {
		return conversation;
	}
	
	/**
	 * 
	 * @return the shown messagePanel
	 */
	public JPanel getShownMessagePanel() {
		return shownMessagePanel;
	}

	/**
	 * Decompose a String in parts (to put '\n' between each cells)
	 * @param message the message to parse
	 * @return an array of the decomposed message
	 */
	public ArrayList<String> parser(String message) {
		int cnt=0;
		String tmpLine="";
		String[] splited = message.split("[\\s]"); //Split on ' ' (blank space) to get words
		ArrayList<String> res = new ArrayList<String>();
		for(String s : splited) {
			if(s.length() >= GUIConfig.MAX_CHAR_PER_LINE) {
				int parts = (s.length() / GUIConfig.MAX_CHAR_PER_LINE)+1;
				for(int i=0; i<parts-1; ++i) {
					res.add(s.substring(i*GUIConfig.MAX_CHAR_PER_LINE, (i+1)*GUIConfig.MAX_CHAR_PER_LINE));
				}
				res.add(s.substring((parts-1)*GUIConfig.MAX_CHAR_PER_LINE));
			}
			else if(s.length() + cnt + 1 <= GUIConfig.MAX_CHAR_PER_LINE) {
				cnt += s.length()+1;
				tmpLine += s + " ";
			}
			else {
				res.add(tmpLine);
				cnt=0;
			}
		}
		if(cnt <= GUIConfig.MAX_CHAR_PER_LINE) {
			res.add(tmpLine);
		}
		return res;
	}
	
	/**
	 * Display a message in the conversation panel
	 * @param m the message to display
	 */
	private void addMessage(Message m) {

		////////////// Show date (Day / Month) every new Day (Once per day) //////////////
		Calendar lastMessageCalendar = Calendar.getInstance();
		lastMessageCalendar.setTimeInMillis(lastMessageTime);
		int lDay, lMonth, lYear;
		lDay = lastMessageCalendar.get(Calendar.DAY_OF_MONTH);
		lMonth = lastMessageCalendar.get(Calendar.MONTH);
		lYear = lastMessageCalendar.get(Calendar.YEAR);
		
		lastMessageCalendar.setTimeInMillis(m.getTime());
		int mDay, mMonth, mYear;
		mDay = lastMessageCalendar.get(Calendar.DAY_OF_MONTH);
		mMonth = lastMessageCalendar.get(Calendar.MONTH);
		mYear = lastMessageCalendar.get(Calendar.YEAR);
		
		
		if(mDay != lDay
			|| (mMonth != lMonth)
			|| (mYear != lYear)) {
			
			SimpleDateFormat ft = new SimpleDateFormat ("E dd.MM.yyyy");
			JLabel dayLabel = new JLabel("<html><div style='text-align: center;'>" 
							+ ft.format(new Date(m.getTime())) 
							+ "</div></html>", SwingConstants.CENTER);
			dayLabel.setForeground(Color.RED);
			lastMessageTime = m.getTime();
			shownMessagePanel.add(dayLabel);
		}
		////////////////////////////////////////////////////////
		
		
		////////////// Show message ////////////////////////////
		SimpleDateFormat ft = new SimpleDateFormat("kk:mm");
		ArrayList<String> parsedMessage = parser(m.getContent());
		/*for(String s : parsedMessage) {
			System.out.println(s);
		}*/
		
		String srcPseudo = LocalSystemConfig.getNetworkManagerInstance().getPseudoFromPort(m.getSrc());
		
		String tab = "&nbsp;".repeat(2*(8+srcPseudo.length()));
		String finalMessage="<html>" 
				+ "[" + ft.format(new Date(m.getTime())) + "] "
				+ srcPseudo + ": ";
		for(String s : parsedMessage) {
			finalMessage+=s + "<br/>" + tab;
		}
		finalMessage += "</html>";
		
		//Set the message color according to the sender / receiver
		Color c = (m.getSrc()==LocalSystemConfig.get_TCP_port())?Color.BLUE:(new Color(0, 153, 0));
		
		JLabel l = new JLabel(finalMessage);
		l.setForeground(c);			
		shownMessagePanel.add(l);
		
		messageScrollPane.validate();
		JScrollBar sb = messageScrollPane.getVerticalScrollBar();
		sb.setValue(sb.getMaximum());
	}
	
	/**
	 * Load the current conversation
	 */
	public void loadConversation() {
		lastMessageTime = 0;
		displayedMessagesNb = 0;
		if(conversation != null) {
			shownMessagePanel.removeAll();
		}
		updateUI();
	}
	
	/**
	 * Update the conversation (GUI)
	 */
	public void updateConversation() {
		@SuppressWarnings("unchecked")
		ArrayList<Message> allMessages = (ArrayList<Message>) conversation.getMessages().clone();
		int allMsgNb = allMessages.size();
		// There's new message(s) (else, return);
		if(displayedMessagesNb < allMsgNb) {
			Collections.reverse(allMessages);
			int newMsgNb = allMsgNb - displayedMessagesNb;
			for(int i=newMsgNb; i>0; i--) {
				addMessage(allMessages.get(i-1));
				displayedMessagesNb++;
			}
		}
		updateUI();
	}

	/**
	 * Send the TextArea message on click on sendButton
	 */
	@Override
	public void actionPerformed(ActionEvent evt) {
		Object performer = evt.getSource();
		String text = messageTextArea.getText();
		text = text.replaceAll("\n", "<br/>");
		if(performer == sendButton && conversation != null && text.length() > 0) {
			conversation.send(text);
			messageTextArea.setText("");
		}

	}



}
