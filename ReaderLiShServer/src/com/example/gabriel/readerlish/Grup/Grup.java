package com.example.gabriel.readerlish.Grup;

import java.io.Serializable;

public class Grup implements Serializable{
	
	private int id;
	private String nume;
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
	

}
