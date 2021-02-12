package config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public final class DatabaseConfig {
	
	//Database username
	public static final String DB_USR = "tp_servlet_004";
	
	//Database Password
	public static final String DB_PSW = "Ua2wae0g";
	
	//Database url
	public static final String DB_URL = "jdbc:mysql://srv-bdens.insa-toulouse.fr/tp_servlet_004";
	
	//Database table name
	public static final String DB_TABLE_NAME = "Messages";
	
	//Query to get Databse Time
	public static final String DB_SELECT_TIME = "SELECT CURRENT_TIMESTAMP";
	
	private static Connection con = null;
	
	
	/**
	 * Function that initiate the connection to the database
	 */
	public static void connectToDatabase(){
		try {
			Class.forName ("com.mysql.cj.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			System.err.println("Cannot load sql driver");
		}

		try {
			con = DriverManager.getConnection(
					  DatabaseConfig.DB_URL
					, DatabaseConfig.DB_USR
					, DatabaseConfig.DB_PSW);
		} catch (SQLException e) {
			System.err.println("Cannot connect to db");
		}
	}
	
	/**
	 * Function that return a reference to the current database connection
	 * @return con the current connection
	 * @throws SQLException
	 */
	public static Connection getCon() throws SQLException {
		if(con == null) throw new SQLException("First connect to database");
		return con;
	}
	
	
	/**
	 * Function that insert an element in the table
	 * @param stmContent the statement to execute
	 */
	public static void insert(String stmContent){
		try {
			Statement statement = con.createStatement();
			statement.executeUpdate(stmContent);
			statement.close();
			} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Function that execute a select on the table
	 * @param stmContent the statement to execute (ex : SELECT * FROM Messages)
	 * @return the ResultSet corresponding to the select
	 */
	public static ResultSet select (String stmContent) {
		ResultSet rs = null;
		try {
			Statement statement = con.createStatement();
			rs = statement.executeQuery(stmContent);
			return rs;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return rs;
	}
	
	
	
	
	
	
	
}
