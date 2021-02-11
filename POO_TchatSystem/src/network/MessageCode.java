package network;

// Contains macros for message -> action correspondence
public final class MessageCode {
	
	// Separator (to separate informations in a message)
	public static final String SEP = "#separator#"; //NOT A NUMBER OR CHAR
	
	// Code to notify that the message came from the server
	public static final String FROM_SERVER = "#fromserver#"; //NOT A NUMBER OR CHAR
	
	//Discover codes
	public static final String NOTIFY_JOIN = "new";
	public static final String NOTIFY_REPLY = "reply";
	public static final String NOTIFY_LEAVE = "leave";
	
	//Change pseudo codes
	public static final String ASK_CHANGE_PSEUDO = "acp";
	public static final String REPLY_CHANGE_PSEUDO = "rcp";
	public static final String NOTIFY_CHANGE_PSEUDO = "ncp";
	public static final String TELL_PSEUDO = "tp";
	
	//Send private message codes
	public static final String PRIVATE_MESSAGE = "pm";
	
	
	
}
