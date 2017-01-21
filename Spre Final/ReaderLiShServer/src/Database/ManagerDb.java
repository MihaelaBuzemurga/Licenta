package Database;

import java.awt.List;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;

import com.example.gabriel.readerlish.Carte.Carte;
import com.example.gabriel.readerlish.Grup.Grup;
import com.example.gabriel.readerlish.Mesaj.Mesaj;
import com.example.gabriel.readerlish.Nota.Nota;
import com.example.gabriel.readerlish.User.*;

public class ManagerDb {

	private Database m_db;
	private static ManagerDb manager_db = null;

	private ManagerDb() {
		m_db = new Database();
		m_db.connect();
	}

	public static ManagerDb getSession() {
		if (manager_db == null) {
			manager_db = new ManagerDb();
		}
		return manager_db;
	}

	public User Logare(User utilizator) {
		ResultSet result = null;

		try {
			String query = "SELECT *FROM USERS WHERE Nume_utilizator=? AND Parola= ?";
			PreparedStatement pState = m_db.getConnection().prepareStatement(query);
			pState.setString(1, utilizator.getNume_utilizator());
			pState.setString(2, utilizator.getParola());
			result = pState.executeQuery();
			while (result.next()) {
				utilizator.setId(result.getInt("ID"));
				utilizator.setNume(result.getString("Nume"));
				utilizator.setPrenume(result.getString("Prenume"));
				utilizator.setLogat(true);
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return utilizator;

	}

	public boolean isUserAvailable(User utilizator) {
		ResultSet result = null;

		try {
			String query = "SELECT * FROM USERS WHERE Nume_utilizator=?";
			PreparedStatement pState = m_db.getConnection().prepareStatement(query);
			pState.setString(1, utilizator.getNume_utilizator());
			result = pState.executeQuery();
			if (result.next()) {
				return false;
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return true;
	}

	public User Register(User utilizator) {
		if (isUserAvailable(utilizator)) {
			try {
				String query = "INSERT INTO Users (ID,Nume, Prenume, Nume_utilizator, Parola)  VALUES(NULL,?,?,?,?)";
				PreparedStatement pState = m_db.getConnection().prepareStatement(query,
						Statement.RETURN_GENERATED_KEYS);
				pState.setString(1, utilizator.getNume());
				pState.setString(2, utilizator.getPrenume());
				pState.setString(3, utilizator.getNume_utilizator());
				pState.setString(4, utilizator.getParola());
				
				int result = pState.executeUpdate();
				ResultSet rs = pState.getGeneratedKeys();
				while (rs.next()) {
					utilizator.setId(rs.getInt(1));
					utilizator.setLogat(true);
				}

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		return utilizator;

	}

	public void uploadCarte(Carte carte) {
		try {
			String query = "INSERT INTO Files (Nume, Autor,DESCRIERE,Gen)  VALUES(?,?,?,?)";
			PreparedStatement pState = m_db.getConnection().prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			pState.setString(1, carte.getNume());
			pState.setString(2, carte.getAutor());
			pState.setString(3, carte.getDescriere());
			pState.setInt(4, carte.getId_gen());
			int result = pState.executeUpdate();
			ResultSet rs = pState.getGeneratedKeys();
			while (rs.next()) {
				carte.setID(rs.getInt(1));
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void updateCarteCale(Carte carte) {
		try {
			String query = "UPDATE Files SET Cale_pdf=?, Cale_img=? where ID=?";
			PreparedStatement pState = m_db.getConnection().prepareStatement(query);
			pState.setString(1, carte.getCale());
			pState.setString(2, carte.getCale_img());
			pState.setInt(3, carte.getID());
			int result = pState.executeUpdate();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void updateCarte(Carte carte) {
		try {
			String query = "UPDATE Files SET Nume=?, Autor=?, Gen=?,Descriere=? where ID=?";
			PreparedStatement pState = m_db.getConnection().prepareStatement(query);
			pState.setString(1, carte.getNume());
			pState.setString(2, carte.getAutor());
			pState.setInt(3, carte.getId_gen());
			pState.setString(4, carte.getDescriere());
			pState.setInt(5, carte.getID());
			int result = pState.executeUpdate();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public Carte getCarte(Carte carte) {
		ResultSet result = null;

		try {
			String query = "SELECT * FROM Files WHERE ID=?";
			PreparedStatement pState = m_db.getConnection().prepareStatement(query);
			pState.setInt(1, carte.getID());
			result = pState.executeQuery();
			while (result.next()) {
				carte = new Carte();
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

	public Carte[] getBooks(int id) {
		ResultSet result = null;

		Carte[] carti = new Carte[6];
		try {
			String query = "SELECT * FROM Files LIMIT ? OFFSET ?";
			PreparedStatement pState = m_db.getConnection().prepareStatement(query);
			pState.setInt(1, 6);
			pState.setInt(2, id * 6);
			result = pState.executeQuery();
			int i = 0;
			while (result.next()) {
				Carte carte = new Carte();
				carte.setID(result.getInt("ID"));
				carte.setNume(result.getString("Nume"));
				carte.setAutor(result.getString("Autor"));
				carte.setCale(result.getString("Cale_pdf"));
				carte.setId_gen(result.getInt("Gen"));
				carte.setDescriere(result.getString("Descriere"));
				carte.setNota(result.getInt("NOTA"));
				carte.setNr_votanti(result.getInt("NR_VOT"));
				carte.setCale_img(result.getString("Cale_img"));
				carti[i] = carte;
				System.out.println("inca una");
				i++;
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return carti;
	}
	public ArrayList<Grup> getAllGrup() {
		ArrayList<Grup> grupuri = new ArrayList();
		ResultSet result = null;
		try {
			String query = "SELECT * from GEN";
			PreparedStatement pState = m_db.getConnection().prepareStatement(query);
			result = pState.executeQuery();
			while (result.next()) {

				Grup grup = new Grup();
				grup.setId(result.getInt("ID"));
				grup.setNume(result.getString("NUME"));
				grupuri.add(grup);
					
				}	

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return grupuri;
	}
	public ArrayList<Grup> getGrupForSubscribe(int id) {
		
		ArrayList<Grup> grupuri_temp = new ArrayList();
		ResultSet result = null;

		
		try {
			String query = "SELECT DISTINCT ID, NUME FROM (SELECT * FROM GRUP WHERE ID_USER =?) AS GENURI LEFT OUTER JOIN GEN ON GENURI.ID_GEN  = GEN.ID";
			PreparedStatement pState = m_db.getConnection().prepareStatement(query);
			pState.setInt(1, id);
			result = pState.executeQuery();
			int i = 0;
			while (result.next()) {
				Grup grup = new Grup();
				grup.setId(result.getInt("ID"));
				grup.setNume(result.getString("NUME"));
				grupuri_temp.add(grup);
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return grupuri_temp;
	}

	public ArrayList<Grup> getYourGrups(int id) {
		ArrayList<Grup> grupuri = new ArrayList();
		ResultSet result = null;

		Grup[] carti = new Grup[4];
		try {
			String query = "SELECT ID, NUME FROM (SELECT * FROM GRUP WHERE ID_USER =? ) AS GENURI INNER JOIN GEN ON GENURI.ID_GEN=GEN.ID";
			PreparedStatement pState = m_db.getConnection().prepareStatement(query);
			pState.setInt(1, id);
			result = pState.executeQuery();
			int i = 0;
			while (result.next()) {
				Grup grup = new Grup();
				grup.setId(result.getInt("ID"));
				grup.setNume(result.getString("NUME"));
				grupuri.add(grup);
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return grupuri;
	}
	public ArrayList<Grup> addToGrup(int id_GEN) {

		ArrayList<Grup> grupuri = new ArrayList();
		try {
			String query = "INSERT INTO GRUP (ID_GEN, ID_USER)  VALUES(?,?)";
			PreparedStatement pState = m_db.getConnection().prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			pState.setInt(1, id_GEN);
			pState.setInt(2, 1);
			int result = pState.executeUpdate();	
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		grupuri=getYourGrups(1);
		return grupuri;


	}
	public void addNota(Nota nota) {
		try {
			String query = "UPDATE Files SET NOTA=?, NR_VOT=?  where ID=?";
			PreparedStatement pState = m_db.getConnection().prepareStatement(query);
			pState.setInt(1, nota.getNota());
			pState.setInt(2, nota.getNr_utilizatori());
			pState.setInt(3, nota.getId_carte());
			int result = pState.executeUpdate();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
