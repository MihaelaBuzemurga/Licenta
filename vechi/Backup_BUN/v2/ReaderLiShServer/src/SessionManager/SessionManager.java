package SessionManager;

import java.util.HashMap;
import java.util.Map;

import Database.ManagerDb;
import Message.Message;
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
	
	public Message Login(Message message) {
		
		Message mesaj=new Message();
		System.out.println("Start Login");
		User utilizator = new User();
		String id=message.getNextParameter();
		String nume=message.getNextParameter();
		String parola=message.getNextParameter();
		if(!user_session.containsKey(id))
		{
	
			System.out.println(parola);
			utilizator.setNume_utilizator(nume);
			utilizator.setParola(parola);
			System.out.println(utilizator.getNume_utilizator());
			System.out.println(utilizator.getParola());
			ManagerDb.getSession().Logare(utilizator);
			if (utilizator.getNume() != null) {
				Session newSession=new Session(utilizator);
				String sessionId=SessionIdentifier.nextSessionId();
				user_session.put(sessionId, newSession);
				mesaj.addMessage(sessionId);
			}
		}
		else
		{
			mesaj.addMessage("Esti deja logat");
		}
		return mesaj;
	}
	public Message Register(Message message)
	{
		Message mesaj=new Message();
		User utilizator = new User();
		String nume=message.getNextParameter();
		String prenume=message.getNextParameter();
		String nume_utilizator=message.getNextParameter();
		String parola=message.getNextParameter();
		utilizator.setNume(nume);
		utilizator.setPrenume(prenume);
		utilizator.setNume_utilizator(nume_utilizator);
		utilizator.setParola(parola);
		ManagerDb.getSession().Register(utilizator);
		if(utilizator.getId()!=-1)
		{
			Session newSession=new Session(utilizator);
			String sessionId=SessionIdentifier.nextSessionId();
			user_session.put(sessionId, newSession);
			mesaj.addMessage(sessionId);
		}
		else
		{
			mesaj.addMessage("Utilizatorul exista deja!");
		}
		return mesaj;
	}
	public void Logout(Message message)
	{
		String id=message.getNextParameter();
		user_session.remove(id);
	}

}
