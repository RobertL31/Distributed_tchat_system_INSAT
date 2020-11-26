package distantApp;

import java.net.DatagramPacket;

// Contains macros for message -> action correspondence
public final class MessageCode {
	
	private static final String PREFIX = "!/";
	private static final String SUFFIX = " ";
	
	
	public static final String NOTIFY_JOIN = PREFIX + "new" + SUFFIX;
	public static final String NOTIFY_LEAVE = PREFIX + "leave" + SUFFIX;
	
	
	
}
