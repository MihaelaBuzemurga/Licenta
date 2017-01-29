package Database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import FileManager.Carte;
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
	
	public void uploadCarte(Carte carte)
	{
		try {
			String query = "INSERT INTO Files (Nume, Autor,DESCRIERE,Gen)  VALUES(?,?,?,?)";
			PreparedStatement pState = m_db.getConnection().prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			pState.setString(1, carte.getNume());
			pState.setString(2, carte.getAutor());
			pState.setString(3, carte.getDescriere());
			pState.setInt(4,carte.getId_gen());
			int result = pState.executeUpdate();
			ResultSet rs = pState.getGeneratedKeys();
			 while ( rs.next() ) {
				 carte.setID(rs.getInt(1));
			 }


		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void updateCarteCale(Carte carte)
	{
		try {
			String query = "UPDATE Files SET Cale_pdf=?, Cale_img=? where ID=?";
			PreparedStatement pState = m_db.getConnection().prepareStatement(query);
			pState.setString(1, carte.getCale());
			pState.setString(2, carte.getCale_img());
			pState.setInt(3,carte.getID());
			int result = pState.executeUpdate();


		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public Carte getCarte(int id)
	{
		Carte carte=null;
		ResultSet result=null;
		
		try {
			String query = "SELECT * FROM Files WHERE ID=?";
			PreparedStatement pState = m_db.getConnection().prepareStatement(query);
			pState.setInt(1, id);
			result=pState.executeQuery();
			 while ( result.next() ) {
				 carte=new Carte();
				carte.setID(result.getInt("ID"));
				carte.setNume(result.getString("Nume"));
				carte.setAutor(result.getString("Autor"));
				carte.setCale(result.getString("Cale_pdf"));
				carte.setId_gen(result.getInt("Gen"));
				carte.setDescriere(result.getString("Descriere"));
				carte.setNota(result.getInt("NOTA"));
				carte.setNr_votanti(result.getInt("NR_VOT"));
				carte.setCale_img(result.getString("Cale_img"));
			 }


		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return carte;
	}


}
