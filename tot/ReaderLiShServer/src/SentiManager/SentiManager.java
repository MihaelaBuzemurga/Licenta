package SentiManager;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.example.gabriel.readerlish.Mesaj.Mesaj;

public class SentiManager {
	private static SentiManager manageSenti = null;
	private String pathSenti="senti.txt";
	SentiWord sentiwordnet;

	private SentiManager() {
		
		try {
			sentiwordnet = new SentiWord(pathSenti);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static SentiManager getSession() {
		if (manageSenti == null) {
			manageSenti = new SentiManager();
		}
		return manageSenti;
	}
	
	public Mesaj getSenti(String words)
	{
		Mesaj mesaj=new Mesaj();
		 Map<String,Double> map=new HashMap<String,Double>();  
		 String[] listWords=words.split(" ");
		 for(int i=0;i<listWords.length;i++)
		 {
			 map.put(listWords[i], sentiwordnet.calculate_sentiment_average(listWords[i]));
		 }
		 mesaj.setObiect(map);
		 return mesaj;
	}
	
	
	

}
