package database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;

import config.DatabaseConfig;
import config.LocalSystemConfig;
import tools.Pair;

/**
 * 
 * Manage conversations (Retrieve from database, manage received messages, get conversations)
 *
 */

public class ConversationManager {
	
	// The conversations list
	ArrayList<Conversation> conversations;
	
	/**
	 * Create and initialize a conversation manager (retrieve conversations from database)
	 */
	public ConversationManager() {
		conversations = new ArrayList<Conversation>();
		this.retreiveHistory();
	}
	
	/**
	 * Function which find the conversation corresponding to the right destination port
	 * Otherwise, it create it
	 * @param port the port of the message sender
	 * @return the corresponding Conversation
	 */
	public Conversation getConversation(int port) {
		int dstPort;
		for(Conversation c : conversations) {
			dstPort = c.getDestIP();
			if(port == dstPort)
				return c;
			
		}
		// If the conversation does not exists,
		// create it with destination = the message sender
		Conversation newConv = new Conversation(port);
		conversations.add(newConv);
		return newConv;
	}
	
	/**
	 * Handle a received message (put it in the right conversation)
	 * @param message the message to handle
	 * @param srcPort the mesage sender port
	 */
	public void receive(String message, int srcPort) {
		Conversation c = getConversation(srcPort);
		c.addMessageToList(message, true);
	}
	
	/**
	 * Send a message to dstPort
	 * @param message the message to send
	 * @param dstPort the destination port
	 */
	public void send(String message, int dstPort) {
		Conversation c = getConversation(dstPort);
		c.send(message);
	}
	
	/**
	 * Retrieve conversations from the database
	 */
	public void retreiveHistory() {
		String messagesQuery = 
				  "SELECT * FROM "
				+ DatabaseConfig.DB_TABLE_NAME
				+ " WHERE ";
		String sentMessageQuery = 
				messagesQuery
				+ "source=" 
				+ LocalSystemConfig.get_TCP_port();
		String receivedMessageQuery = 
				messagesQuery
				+ "dest=" 
				+ LocalSystemConfig.get_TCP_port();
		
		//Variables to store messages informations
		int src, dst, srcCol, dstCol, contentCol, dateCol;
		String content;
		Timestamp date;
		
		long msTime;
		Message toAdd;
		ResultSet sentSet;
		//Process sent messages first
		sentSet = DatabaseConfig.select(sentMessageQuery);
		
		try {
			srcCol = sentSet.findColumn("source");
			dstCol = sentSet.findColumn("dest");
			contentCol = sentSet.findColumn("content");
			dateCol = sentSet.findColumn("date");
		} catch (SQLException e1) {
			System.err.println("Cannot find columns in database !");
			e1.printStackTrace();
			return;
		}
		
		
		try {
			while(sentSet.next()) {
				src = sentSet.getInt(srcCol);
				dst = sentSet.getInt(dstCol);
				content = sentSet.getString(contentCol);
				date = sentSet.getTimestamp(dateCol);
				msTime = date.getTime();
				toAdd = new Message(src, dst, msTime, content);
				getConversation(dst).addMessageToList(toAdd);
			}
		} catch (SQLException e) {
			System.err.println("Problem ResultSet while retreiving sent messages history") ;
			e.printStackTrace();
		}
		
		//Process received messages
		sentSet = DatabaseConfig.select(receivedMessageQuery);
		try {
			while(sentSet.next()) {
				src = sentSet.getInt(srcCol);
				dst = sentSet.getInt(dstCol);
				content = sentSet.getString(contentCol);
				date = sentSet.getTimestamp(dateCol);
				msTime = date.getTime();
				toAdd = new Message(src, dst, msTime, content);
				getConversation(src).addMessageToList(toAdd);
			}
		} catch (SQLException e) {
			System.err.println("Problem on ResultSet while retreiving received messages history") ;
			e.printStackTrace();
		}
		
	}
	
	
	/**
	 * 
	 * @return the conversations list
	 */
	public ArrayList<Conversation> getConversations() {
		return conversations;
	}

	@Override
	public String toString() {
		String res = "";
		for(Conversation c : conversations) {
			res += c.toString();
		}
		return res;
	}

	
	
}
