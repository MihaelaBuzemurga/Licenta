package Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {
	Connection connection = null;
	public Database() {

	}

	public void connect() {
		try {
			Class.forName("org.sqlite.JDBC");	
			connection = DriverManager.getConnection(
					"jdbc:sqlite:E:\\Database\\ReaderLiSh.sqlite");

		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	public Connection getConnection(){
		return connection;
	}

}
