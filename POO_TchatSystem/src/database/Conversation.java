package database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import config.DatabaseConfig;
import config.LocalSystemConfig;
import network.MessageCode;
import network.Sender;

/**
 * 
 * This class allows to store every informations about a conversation between two users
 *
 */
public class Conversation {
	
	//Port of the first speaker (i.e the user)
	private int sourceIP = LocalSystemConfig.get_TCP_port();
	//Port of the second speaker
	private int destIP;
	//Store messages list. (Message format: [(source port, destination port), (time, message)])
	private ArrayList<Message> messages;
	//The database connection (to insert / retrieve messages)
	private Connection con;

	
	/**
	 * Create a new conversation from scratch 
	 * @param destIP the port of the second speaker
	 */
	public Conversation(int destIP){
		this.destIP = destIP;
		this.messages = new ArrayList<>();
		try{
			this.con = DatabaseConfig.getCon();
		} catch(SQLException e){
			e.printStackTrace();
		}
	}

	/**
	 * Create a new conversation from retrieved messages 
	 * @param destIP the second speaker port
	 * @param messages the list of retrieved messages
	 */
	public Conversation(int destIP, ArrayList<Message> messages) {
		super();
		this.destIP = destIP;
		this.messages = messages;
	}
	
	/**
	 * Insert a message in the @ArrayList @messages, adding the database time in Ms
	 * @param message the message to add
	 * @param received True if the message comes from someone, false otherwise (the client is the sender)
	 */
	public Message addMessageToList(String message, boolean received) {
		//get the database timestamp to insert the sent message in messages arraylist with the db timestamp
		ResultSet rsTimestamp = DatabaseConfig.select(DatabaseConfig.DB_SELECT_TIME);
		Message recvMsg = null;
		try {
			if(rsTimestamp.next()) {
				int src = this.sourceIP;
				int dst = this.destIP;
				if(received) {
					src = this.destIP;
					dst = this.sourceIP;
				}
				
				long msTime = rsTimestamp.getTimestamp(1).getTime();
				recvMsg = new Message(src, dst, msTime, message);
				messages.add(recvMsg);
				rsTimestamp.close();
			}
		} catch (SQLException e) {
			System.err.println("Cannot get time from database, message not added");
		}
		return recvMsg;
	}
	
	/**
	 * Insert a message in the @ArrayList @messages
	 * @param m the message to insert
	 */
	public void addMessageToList(Message m) {
		long m_time = m.getTime();
		long c_time;
		for(int i=0; i<messages.size(); i++) {
			c_time = messages.get(i).getTime();
			if(m_time < c_time) {
				messages.add(i, m);
				return;
			}
		}
		//If this is the last message: add at the end of the list
		messages.add(m);
		return;
	}
	
	/**
	 * 
	 * @return the second speaker port
	 */
	public int getDestIP() {
		return destIP;
	}
	
	/**
	 * Set the second speaker port
	 * @param destIP the port to set
	 */
	public void setDestIP(int destIP) {
		this.destIP = destIP;
	}
	
	/**
	 * 
	 * @return the first speaker port
	 */
	public int getSourceIP() {
		return sourceIP;
	}
	
	/**
	 * 
	 * @return the list of messages sent between the two speakers
	 */
	public ArrayList<Message> getMessages() {
		return messages;
	}

	/**
	 * Send a message to the second speaker (destIP port)
	 * @param message the message to send
	 * @return the message if successfully sent, null otherwise
	 */
	public Message send(String message){
		String toSend =   	  MessageCode.PRIVATE_MESSAGE
				+ LocalSystemConfig.get_TCP_port()
				+ MessageCode.SEP
				+ message;

		if(Sender.send(toSend, destIP)){
			//If the message have been sent, add it to the database
			
			String addToDB = 
					"INSERT INTO "
							+ DatabaseConfig.DB_TABLE_NAME
							+ "(source, dest, content)" 
							+ " VALUES ("
							+ this.sourceIP 
							+ ", "
							+ destIP 
							+ ", "
							+ "\""
							+ message
							+ "\""
							+ ")";
			DatabaseConfig.insert(addToDB);
			return addMessageToList(message, false);
		}
		return null;
	}

	@Override
	public String toString() {
		String res = "---- BEG ----\n";
		for(Message m : messages) {
			res += m.toString();
		}
		res += "---- END ----\n";
		return res;
	}

	public Connection getCon() {
		return con;
	}



}
