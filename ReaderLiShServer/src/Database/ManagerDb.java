package Database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import User.*;

public class ManagerDb {

	private Database m_db;

	public ManagerDb() {
		m_db = new Database();
		m_db.connect();
	}

	public User Logare(User utilizator) {
		ResultSet result=null;
		
		try {
			String query = "SELECT *FROM USERS WHERE Nume_utilizator=? AND Parola= ?";
			PreparedStatement pState = m_db.getConnection().prepareStatement(query);
			pState.setString(1, utilizator.getNume_utilizator());
			pState.setString(2, utilizator.getParola());
			result=pState.executeQuery();
			 while ( result.next() ) {
				 utilizator.setId(result.getInt("ID"));
				 utilizator.setNume(result.getString("Nume"));
				 utilizator.setPrenume(result.getString("Prenume"));
			 }


		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return utilizator;
		
	}

}
