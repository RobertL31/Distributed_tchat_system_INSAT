package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class DatabaseConfig {
	public static final String DB_USR = "insaclient";
	public static final String DB_PSW = "insa";
	public static final String DB_URL = "jdbc:mysql://localhost:3306/INSA_POO";
	public static final String DB_TABLE_NAME = "Messages";
	
	private static Connection con = null;
	
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
	
	public static Connection getCon() throws SQLException {
		if(con == null) throw new SQLException("First connect to database");
		return con;
	}
}
