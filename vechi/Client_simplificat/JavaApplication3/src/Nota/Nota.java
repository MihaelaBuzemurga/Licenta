/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Nota;

/**
 *
 * @author Gabriel
 */
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
