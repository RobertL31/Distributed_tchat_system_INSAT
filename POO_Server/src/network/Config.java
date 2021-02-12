package network;

import java.util.ResourceBundle;

public class Config {
	
	private static ResourceBundle bundle = ResourceBundle.getBundle("properties.config");
	
	public static final int LISTENING_PORT = Integer.valueOf(bundle.getString("listening_port"));
	
	// Codes
	public static final String SEP = "#separator#"; //NOT A NUMBER OR CHAR
	public static final String FROM_SERVER = "#fromserver#"; //NOT A NUMBER OR CHAR
	
	
	// Port distribution
	public static int START_PORT_GENERAL = Integer.valueOf(bundle.getString("start_general"));
	public static int END_PORT_GENERAL = Integer.valueOf(bundle.getString("end_general"));

	public static int START_PORT_EXT = START_PORT_GENERAL;
	public static int END_PORT_EXT = Integer.valueOf(bundle.getString("end_port_ext"));

	public static int START_PORT_IN = Integer.valueOf(bundle.getString("start_port_in"));
	public static int END_PORT_IN = END_PORT_GENERAL;
	
}
