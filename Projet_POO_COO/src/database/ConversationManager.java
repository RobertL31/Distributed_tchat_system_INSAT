package database;

import java.sql.Time;
import java.util.ArrayList;

import tools.Pair;

public class ConversationManager {
	
	ArrayList<Conversation> conversations;
	
	public ConversationManager() {
		conversations = new ArrayList<Conversation>();
	}
	
	/**
	 * Function which find the conversation corresponding to the right destination port
	 * Otherwise, it create it
	 * @param port the port of the message sender
	 * @return the corresponding Conversation
	 */
	private Conversation getConversation(int port) {
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
	
	public String toString() {
		String res = "";
		for(Conversation c : conversations) {
			res += c.toString();
		}
		return res;
	}

	
	
}
