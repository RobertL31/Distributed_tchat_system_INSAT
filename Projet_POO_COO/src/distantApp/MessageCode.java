package distantApp;

import java.net.DatagramPacket;

// Contains macros for message -> action correspondence
public final class MessageCode {
	
	
	public static final String SEP_CHANGE_PSEUDO = "#separator#"; //NIT A NUMBER OR CHAR
	
	public static final String NOTIFY_JOIN = "new";
	public static final String NOTIFY_REPLY = "reply";
	public static final String NOTIFY_LEAVE = "leave";
	
	
	public static final String ASK_CHANGE_PSEUDO = "1";
	public static final String REPLY_CHANGE_PSEUDO = "2";
	public static final String NOTIFY_CHANGE_PSEUDO = "3";
	public static final String TELL_PSEUDO = "4";
	
	
	
}
