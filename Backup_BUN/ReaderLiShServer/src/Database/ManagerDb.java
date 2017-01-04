package Database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import User.*;

public class ManagerDb {

	private Database m_db;
	private static ManagerDb manager_db=null;
	private ManagerDb() {
		m_db = new Database();
		m_db.connect();
	}
	
	public static ManagerDb getSession()
	{
		if(manager_db==null)
		{
			manager_db=new ManagerDb();
		}
		return manager_db;
	}

	public void Logare(User utilizator) {
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
		
	}
	public boolean isUserAvailable(User utilizator)
	{
		ResultSet result=null;
		
		try {
			String query = "SELECT * FROM USERS WHERE Nume_utilizator=?";
			PreparedStatement pState = m_db.getConnection().prepareStatement(query);
			pState.setString(1, utilizator.getNume_utilizator());
			result=pState.executeQuery();
			if(result.next())
			{
				return false;
			}


		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return true;
	}
	public void Register(User utilizator) {
		if(isUserAvailable(utilizator))
		{
			try {
				String query = "INSERT INTO Users (Nume, Prenume, Nume_utilizator, Parola)  VALUES(?,?,?,?)";
				PreparedStatement pState = m_db.getConnection().prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				pState.setString(1, utilizator.getNume());
				pState.setString(2, utilizator.getPrenume());
				pState.setString(3, utilizator.getNume_utilizator());
				pState.setString(4, utilizator.getParola());
				int result = pState.executeUpdate();
				ResultSet rs = pState.getGeneratedKeys();
				 while ( rs.next() ) {
					 utilizator.setId(rs.getInt(1));
				 }


			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
		}
		
	}

}
