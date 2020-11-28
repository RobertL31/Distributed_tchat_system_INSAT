package distantApp;

import java.net.DatagramPacket;

// Contains macros for message -> action correspondence
public final class MessageCode {
	
	private static final String PREFIX = "!n";
	
	
	public static final String NOTIFY_JOIN = PREFIX + "new";
	public static final String NOTIFY_LEAVE = PREFIX + "leave";
	
	public static final String ASK_CHANGE_PSEUDO = PREFIX + "cp";
	
	
	
}
