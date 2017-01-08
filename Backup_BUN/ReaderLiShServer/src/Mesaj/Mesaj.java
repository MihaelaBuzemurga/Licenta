package Mesaj;

import java.io.Serializable;

public class Mesaj implements Serializable{
	
	
	private Object obiect;
	private RequestEnum comanda;
	
	public Mesaj(){};
	
	public Object getObiect()
	{
		return obiect;
	}
	
	public void setObiect(Object obiect)
	{
		this.obiect=obiect;
	}
	
	public RequestEnum getCmd()
	{
		return this.comanda;
	}
	public void setCmd(RequestEnum cmd)
	{
		this.comanda=cmd;
	}
	
	
	

}
