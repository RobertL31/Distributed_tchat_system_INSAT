package database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;

import config.DatabaseConfig;
import config.LocalSystemConfig;
import tools.Pair;

public class ConversationManager {
	
	ArrayList<Conversation> conversations;
	
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
		// we create it with destination = the message sender
		Conversation newConv = new Conversation(port);
		conversations.add(newConv);
		return newConv;
	}
	
	public void receive(String message, int srcPort) {
		Conversation c = getConversation(srcPort);
		c.addMessageToList(message, true);
	}
	
	public void send(String message, int dstPort) {
		Conversation c = getConversation(dstPort);
		c.send(message);
	}
	
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
	
	
	
	public ArrayList<Conversation> getConversations() {
		return conversations;
	}

	public String toString() {
		String res = "";
		for(Conversation c : conversations) {
			res += c.toString();
		}
		return res;
	}

	
	
}
