package SessionManager;

import java.util.HashMap;
import java.util.Map;

import Database.ManagerDb;
import Mesaj.Mesaj;
import TransformerBytes.TransformerBytes;
import User.User;

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
			if (user.getNume() != null) {
				Session newSession=new Session(user);
				String sessionId=SessionIdentifier.nextSessionId();
				user_session.put(sessionId, newSession);
				mesaj.setObiect(user);
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
			Session newSession=new Session(user);
			String sessionId=SessionIdentifier.nextSessionId();
			user_session.put(sessionId, newSession);
			mesaj.setObiect(user);
		}
		else
		{
			mesaj.setRaspuns("Utilizatorul exista deja!");
		}
		return mesaj;
	}
	public void Logout(User user)
	{
		user_session.remove(user.getId());
	}

}
