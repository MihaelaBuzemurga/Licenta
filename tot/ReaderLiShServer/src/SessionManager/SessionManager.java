package SessionManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import com.example.gabriel.readerlish.Mesaj.Mesaj;
import com.example.gabriel.readerlish.Mesaj.RespondeEnum;
import com.example.gabriel.readerlish.User.User;

import Database.ManagerDb;
import TransformerBytes.TransformerBytes;

public class SessionManager {
	
	
	private static SessionManager m_session=null;
	private Map<String, Session> user_session=new HashMap();

	private SessionManager(){};
	
	public static SessionManager getSession()
	{
		if(m_session==null)
		{
			m_session=new SessionManager();
		}
		return m_session;
	}
	
	public boolean isLoggin(String ID)
	{
		if(user_session.containsKey(ID))
		{
			return true;
		}
		return false;
	}
	
	public Mesaj Login(User user) {
		Mesaj mesaj=new Mesaj();
		
		if(!user_session.containsKey(user.getId()))
		{
			System.out.println("pregatim");
			user=ManagerDb.getSession().Logare(user);
			System.out.println(user.getNume());
			if (user.getLogat()) {
				mesaj.setM_raspunsServer(RespondeEnum.LOGIN_SUCCES);
				Session newSession=new Session(user);
				String sessionId=SessionIdentifier.nextSessionId();
				user_session.put(sessionId, newSession);
				user.setImagineProfil(getFileFromServer(String.valueOf(user.getId())+".jpg"));
				mesaj.setObiect(user);
				
			}
			else
			{
				mesaj.setM_raspunsServer(RespondeEnum.LOGIN_FAIL);
			}
		}
		else
		{
			System.out.println("nu exista");
			mesaj.setRaspuns("Nume/parola incoracta");
		}
		return mesaj;
	}
	public Mesaj Register(User user)
	{
		Mesaj mesaj=new Mesaj();
		
	
		user=ManagerDb.getSession().Register(user);
		if(user.getId()!=-1)
		{
			Session newSession=new Session(user); //creaza o noua sesiune a userului cae incearca sa se inregistreze
			String sessionId=SessionIdentifier.nextSessionId(); //creaza un id unic pentru acesta
			user_session.put(sessionId, newSession); //introduce in user_session id si sesiunea specifica lui
			mesaj.setObiect(user); //pune obiectul de tip user in mesaj
			mesaj.setM_raspunsServer(RespondeEnum.REGISTER_SUCCES); //mesajul este trimis
		}
		else
		{
			mesaj.setM_raspunsServer(RespondeEnum.REGISTER_FAIL);
		}
		return mesaj;
	}
	public Mesaj EditUser(User user)
	{
		Mesaj mesaj=new Mesaj();
		
		user=ManagerDb.getSession().EditUser(user);
		mesaj.setObiect(user);
		return mesaj;
	}
	
	public Mesaj getNotification(int idUser)
	{
		System.out.println("check notification");
		Mesaj mesaj=new Mesaj();
		int nr=ManagerDb.getSession().getNotification(idUser);
		mesaj.setObiect(nr);
		 return mesaj;
	}
	public Mesaj EditUserPhoto(User user)
	{
		Mesaj mesaj=new Mesaj();
		if(user.getImagineProfil()!=null)
		{
			uploadFileOnFolder(String.valueOf(user.getId())+".jpg",user.getImagineProfil());
		}
		return mesaj;
	}
	private void uploadFileOnFolder(String nume,byte[] continut) {
		File file = new File("test_upload\\Users\\"+nume);
		try {
			if(!file.exists())
			{
				file.createNewFile();
			}
			FileOutputStream fisier = new FileOutputStream(file.getPath());
			fisier.write(continut);
			fisier.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	private byte[] getFileFromServer(String path_file)
	{
		 byte[] data = null;
		 Path path = Paths.get("test_upload\\Users\\"+path_file);
		 if(path.toFile().exists())
		 {
			 try {
				 data = Files.readAllBytes(path);
	       } catch (IOException ex) {
	          
	       }
		 }
		 return data;
	}
	public void Logout(User user)
	{
		user_session.remove(user.getId());
	}

}
