package com.example.gabriel.readerlish.User;

import java.io.Serializable;

public class User implements Serializable{
	int id=-1;
	String nume;
	String prenume;
	String nume_utilizator;
	String parola;
	byte [] imagineProfil;
	boolean logat;

	public User(){
		this.nume=null;
		this.prenume=null;
		this.nume_utilizator=null;
		this.parola=null;
		this.logat=true;
	}
	public byte[] getImagineProfil() {
        return imagineProfil;
    }

    public void setImagineProfil(byte[] imagineProfil) {
        this.imagineProfil = imagineProfil;
    }

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNume() {
		return nume;
	}

	public void setNume(String nume) {
		this.nume = nume;
	}

	public String getPrenume() {
		return prenume;
	}

	public void setPrenume(String prenume) {
		this.prenume = prenume;
	}

	public String getNume_utilizator() {
		return nume_utilizator;
	}

	public void setNume_utilizator(String nume_utilizator) {
		this.nume_utilizator = nume_utilizator;
	}

	public String getParola() {
		return parola;
	}

	public void setParola(String parola) {
		this.parola = parola;
	}
	
	public void setLogat(boolean var)
	{
		logat=var;
	}
	public boolean getLogat()
	{
		return logat;
	}
	
	
	
}
