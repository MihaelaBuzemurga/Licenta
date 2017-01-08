package Mesaj;

import java.io.Serializable;

public class Mesaj implements Serializable{
	
	
	private Object obiect;
	private RequestEnum comanda;
    private String raspuns;
    private int id;
	
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
        public String getRaspuns()
        {
            return this.raspuns;
        }
        
        public void setRaspuns(String raspuns)
        {
            this.raspuns=raspuns;
        }

		public int getId() {
			return id;
		}

		public void setId(int id) {
			this.id = id;
		}
	
	
	

}