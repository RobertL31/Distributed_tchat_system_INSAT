package database;

import java.lang.reflect.Constructor;
import java.net.InetAddress;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

import distantApp.MessageCode;
import distantApp.Network_Sender;
import localApp.LocalSystemConfig;
import tools.Pair;

public class Conversation {

	private int sourceIP = LocalSystemConfig.get_TCP_port();
	private int destIP;
	private ArrayList<Pair<Integer, String>> messages;
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
	public Conversation(int destIP, ArrayList<Pair<Integer, String>> messages) {
		super();
		this.destIP = destIP;
		this.messages = messages;
	}

	private boolean insert(String stmContent){
		try {
			Statement statement = con.createStatement();
			return statement.execute(stmContent);
			} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
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
							
			System.out.println(addToDB);
			insert(addToDB);
			//TODO add message to local arraylist 
			
		}


	}



}
