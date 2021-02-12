package network;

public class Config {
	
	public static final int LISTENING_PORT = 65000;
	
	// Codes
	public static final String SEP = "#separator#"; //NOT A NUMBER OR CHAR
	public static final String FROM_SERVER = "#fromserver#"; //NOT A NUMBER OR CHAR
	
	
	// Port distribution
	public static int START_PORT_GENERAL = 64000;
	public static int END_PORT_GENERAL = 65100;

	public static int START_PORT_EXT = START_PORT_GENERAL;
	public static int END_PORT_EXT = 64999;

	public static int START_PORT_IN = 65001;
	public static int END_PORT_IN = END_PORT_GENERAL;
	
}
