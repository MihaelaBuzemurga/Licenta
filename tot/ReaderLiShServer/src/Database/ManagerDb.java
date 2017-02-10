package Database;

import java.awt.List;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.example.gabriel.readerlish.Carte.Carte;
import com.example.gabriel.readerlish.Gen.Gen;
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
	
	
	public  synchronized   Map<String,Integer>  getGenuri() {
		ResultSet result = null;

		
		 Map<String,Integer> map=new HashMap<String,Integer>();  
		try {
			String query = "SELECT * FROM GEN";
			PreparedStatement pState = m_db.getConnection().prepareStatement(query);
			result = pState.executeQuery();
			int i = 0;
			while (result.next()) {
				map.put(result.getString("NUME"), result.getInt("ID"));
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return map;
	}
	public int getNotification(int idUser)
	{
		ResultSet result = null;
		int nr=0;

		try {
			String query = "SELECT * FROM RECOMANDARI  WHERE ID_USER_P=? AND READ=1";
			PreparedStatement pState = m_db.getConnection().prepareStatement(query);
			pState.setInt(1, idUser);
			result = pState.executeQuery();
			while (result.next()) {
				nr++;
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return nr;
	}
	public synchronized String getGen(int idGen)
	{
		ResultSet result = null;
		String numeGen = "";

		try {
			String query = "SELECT * FROM GEN  WHERE ID=?";
			PreparedStatement pState = m_db.getConnection().prepareStatement(query);
			pState.setInt(1, idGen);
			result = pState.executeQuery();
			while (result.next()) {
				numeGen=result.getString("NUME");
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return numeGen;
	}
	public synchronized User Logare(User utilizator) {
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
	public synchronized User EditUser(User utilizator) {
	

		try {
			String query = "UPDATE Users SET Nume=?, Prenume=?, Parola=? where ID=?";
			PreparedStatement pState = m_db.getConnection().prepareStatement(query);
			pState.setString(1, utilizator.getNume());
			pState.setString(2, utilizator.getPrenume());
			pState.setString(3, utilizator.getParola());
			pState.setInt(4, utilizator.getId());
			pState.executeUpdate();

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
			String query = "INSERT INTO Files (Nume, Autor,Utilizator_ID,DESCRIERE,Gen)  VALUES(?,?,?,?,?)";
			PreparedStatement pState = m_db.getConnection().prepareStatement(query, Statement.RETURN_GENERATED_KEYS);//returneaza toata id-urile inserate de catre functia INSERT
			pState.setString(1, carte.getNume());
			pState.setString(2, carte.getAutor());
			pState.setInt(3, carte.getId_utilizator());
			pState.setString(4, carte.getDescriere());
			pState.setInt(5, carte.getId_gen());
			int result = pState.executeUpdate();
			ResultSet rs = pState.getGeneratedKeys();
			while (rs.next()) {//parcurgem id-urile returnate si luam primul id
				carte.setID(rs.getInt(1));//atribuim obiectului carte id-ul
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void uploadEditCarte(Carte carte) {
		try {
			String query = "UPDATE Files SET Nume=?, Autor=?,DESCRIERE=?,Gen=? WHERE ID=?";
			PreparedStatement pState = m_db.getConnection().prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			pState.setString(1, carte.getNume());
			pState.setString(2, carte.getAutor());
			pState.setString(3, carte.getDescriere());
			pState.setInt(4, carte.getId_gen());
			pState.setInt(5, carte.getID());
			int result = pState.executeUpdate();
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
				carte.setId_utilizator(result.getInt("Utilizator_ID"));
				carte.setID(result.getInt("ID"));
				carte.setNume(result.getString("Nume"));
				carte.setAutor(result.getString("Autor"));
				carte.setCale(result.getString("Cale_pdf"));
				carte.setId_gen(result.getInt("Gen"));
				carte.setDescriere(result.getString("Descriere"));
				carte.setNota(result.getFloat("NOTA"));
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
				carte.setId_utilizator(result.getInt("Utilizator_ID"));
				carte.setID(result.getInt("ID"));
				carte.setNume(result.getString("Nume"));
				carte.setAutor(result.getString("Autor"));
				carte.setCale(result.getString("Cale_pdf"));
				carte.setId_gen(result.getInt("Gen"));
				carte.setDescriere(result.getString("Descriere"));
				carte.setNota(result.getFloat("NOTA"));
				carte.setNr_votanti(result.getInt("NR_VOT"));
				carte.setCale_img(result.getString("Cale_img"));
				carti[i] = carte;
				i++;
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return carti;
	}
	public Carte[] getMyBooks(int pagine,int idUilizator) {
		ResultSet result = null;

		Carte[] carti = new Carte[6];
		try {
			String query = "SELECT * FROM Files where Utilizator_ID=? LIMIT ? OFFSET ?";
			PreparedStatement pState = m_db.getConnection().prepareStatement(query);
			pState.setInt(1, idUilizator);
			pState.setInt(2, 6);
			pState.setInt(3, pagine * 6);
			result = pState.executeQuery();
			int i = 0;
			while (result.next()) {
				Carte carte = new Carte();
				carte.setId_utilizator(result.getInt("Utilizator_ID"));
				carte.setID(result.getInt("ID"));
				carte.setNume(result.getString("Nume"));
				carte.setAutor(result.getString("Autor"));
				carte.setCale(result.getString("Cale_pdf"));
				carte.setId_gen(result.getInt("Gen"));
				carte.setDescriere(result.getString("Descriere"));
				carte.setNota(result.getFloat("NOTA"));
				carte.setNr_votanti(result.getInt("NR_VOT"));
				carte.setCale_img(result.getString("Cale_img"));
				carti[i] = carte;
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
	public void addNota(Carte carte) {
		try {
			String query = "UPDATE Files SET NOTA=?, NR_VOT=?  where ID=?";
			PreparedStatement pState = m_db.getConnection().prepareStatement(query);
			pState.setFloat(1, carte.getNota());
			pState.setInt(2, carte.getNr_votanti());
			pState.setInt(3, carte.getID());
			int result = pState.executeUpdate();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
