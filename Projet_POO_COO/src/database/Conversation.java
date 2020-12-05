package database;

import java.lang.reflect.Constructor;
import java.net.InetAddress;
import java.security.Timestamp;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.util.ArrayList;
import java.util.HashMap;

import com.mysql.cj.exceptions.RSAException;

import distantApp.MessageCode;
import distantApp.Network_Sender;
import localApp.LocalSystemConfig;
import tools.Pair;

public class Conversation {

	private int sourceIP = LocalSystemConfig.get_TCP_port();
	private int destIP;
	//format: [(source port, destination port), (time, message)]
	private ArrayList<Message> messages;
	private Connection con = null;

	// Constructor for new conversation
	public Conversation(int destIP){
		this.destIP = destIP;
		this.messages = new ArrayList<>();
		try{
			this.con = DatabaseConfig.getCon();
		} catch(SQLException e){
			e.printStackTrace();
		}
	}

	//Constructor for retreived conversation (from history)
	public Conversation(int destIP, ArrayList<Message> messages) {
		super();
		this.destIP = destIP;
		this.messages = messages;
	}
	
	/**
	 * Insert a message in the @ArrayList @messages, adding the database Timestamp in Ms
	 * @param message the message to add
	 * @param received True if the message comes from someone, false otherwise (the client is the sender)
	 */
	public void addMessageToList(String message, boolean received) {
		//get the database timestamp to insert the sent message in messages arraylist with the db timestamp
		ResultSet rsTimestamp = DatabaseConfig.select(DatabaseConfig.DB_SELECT_TIME);
		try {
			if(rsTimestamp.next()) {
				int src = this.sourceIP;
				int dst = this.destIP;
				if(received) {
					src = this.destIP;
					dst = this.sourceIP;
				}
				
				long msTime = rsTimestamp.getTimestamp(1).getTime();
				Message recvMsg = new Message(src, dst, msTime, message);
				messages.add(recvMsg);
				rsTimestamp.close();
				
				//TEST TODO REMOVE
				if(received)System.out.println(recvMsg);
				///////////////////////////
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
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

	public int getDestIP() {
		return destIP;
	}

	public void setDestIP(int destIP) {
		this.destIP = destIP;
	}

	public int getSourceIP() {
		return sourceIP;
	}

	public ArrayList<Message> getMessages() {
		return messages;
	}

	public void send(String message){
		String toSend =   	  MessageCode.PRIVATE_MESSAGE
				+ LocalSystemConfig.get_TCP_port()
				+ MessageCode.SEP
				+ message;

		// message sended => add to database
		if(Network_Sender.send(toSend, destIP)){
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
			addMessageToList(message, false);

		}


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



}
