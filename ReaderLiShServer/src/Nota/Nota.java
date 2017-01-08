package Nota;

import java.io.Serializable;

public class Nota implements Serializable{
	private int id;
	private int id_carte;
	private int nr_utilizatori;
	private int nota;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getId_carte() {
		return id_carte;
	}
	public void setId_carte(int id_carte) {
		this.id_carte = id_carte;
	}
	public int getNr_utilizatori() {
		return nr_utilizatori;
	}
	public void setNr_utilizatori(int nr_utilizatori) {
		this.nr_utilizatori = nr_utilizatori;
	}
	public int getNota() {
		return nota;
	}
	public void setNota(int nota) {
		
		this.nota = nota;
	}

}
