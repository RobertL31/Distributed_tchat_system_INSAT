package config;
import java.awt.Dimension;

// GUI Dimensions

public class GUIConfig {
	
	// Reload Times (in ms)
	public static final int RLD_TIME_CONNECTED_LIST = 250;
	
	// Text
	public static final String APP_NAME = "ClavardApp";
	public static final String PSEUDO_BUTTON_TXT = "Valider";
	
	// Pseudo constaints
	public static final int MAX_PSEUDO_LEN = 15;
	public static final int MIN_PSEUDO_LEN = 2;
	
	// Windows dimensions (Width, Height)
	// Main Window
	public static final int MAINWINDOW_W = 1280;
	public static final int MAINWINDOW_H = 720;
	public static final Dimension MAINWINDOW_DIMENSIONS = new Dimension(MAINWINDOW_W, MAINWINDOW_H);
	
	// Pseudo Panel
	public static final int PSEUDO_PANEL_W = MAINWINDOW_W/4;
	public static final int PSEUDO_PANEL_H = MAINWINDOW_H/8;
	public static final Dimension PSEUDO_PANEL_DIM = new Dimension(PSEUDO_PANEL_W, PSEUDO_PANEL_H);
	
		
	// ConnectedListPanel
	public static final int CONNECTED_PANEL_W = 1*MAINWINDOW_W/4;
	public static final int CONNECTED_PANEL_H = 9*MAINWINDOW_H/10;
	public static final Dimension CONNECTED_PANEL_DIM = new Dimension(CONNECTED_PANEL_W, CONNECTED_PANEL_H);
	
	// UserPanel (in connectedListPanel)
	public static final int USR_PANEL_W = 9*CONNECTED_PANEL_W/10;
	public static final int USR_PANEL_H = 1*CONNECTED_PANEL_H/6;
	public static final Dimension USR_PANEL_DIM = new Dimension(USR_PANEL_W, USR_PANEL_H);
	
	
	// ConversationPanel
	public static final int CONV_PANEL_W = 2*MAINWINDOW_W/3;
	public static final int CONV_PANEL_H = CONNECTED_PANEL_H;
	public static final Dimension CONV_PANEL_DIM = new Dimension(CONV_PANEL_W, CONV_PANEL_H);
	
	public static final int SP_CONV_PANEL_W = CONV_PANEL_W;
	public static final int SP_CONV_PANEL_H = 3*CONV_PANEL_H/4;
	public static final Dimension SP_CONV_PANEL_DIM = new Dimension(SP_CONV_PANEL_W, SP_CONV_PANEL_H);
	
	public static final int MA_CONV_PANEL_W = CONV_PANEL_W;
	public static final int MA_CONV_PANEL_H = CONV_PANEL_H/4;
	public static final Dimension MA_CONV_PANEL_DIM = new Dimension(MA_CONV_PANEL_W, MA_CONV_PANEL_H);
	
	public static final int MTA_CONV_PANEL_W = 3*MA_CONV_PANEL_W/4;
	public static final int MTA_CONV_PANEL_H = 4*MA_CONV_PANEL_H/5;
	public static final Dimension MTA_CONV_PANEL_DIM = new Dimension(MTA_CONV_PANEL_W, MTA_CONV_PANEL_H);
	
	public static final int MAX_CHAR_PER_LINE = 40;
	
}
