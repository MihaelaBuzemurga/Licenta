/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package User;

/**
 *
 * @author Gabriel
 */
import java.io.Serializable;

public class User implements Serializable{
	int id=-1;
	String nume;
	String prenume;
	String nume_utilizator;
	String parola;

	public User(){
		this.nume=null;
		this.prenume=null;
		this.nume_utilizator=null;
		this.parola=null;
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
	
	
	
}