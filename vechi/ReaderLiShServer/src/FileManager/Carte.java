package FileManager;

import java.io.Serializable;

public class Carte implements Serializable{
	private String nume;
	private String autor;
	private String cale_pdf;
	private String cale_img;
	private int id_utilizator;
	private String descriere;
	private String gen;
	private int id_gen;
	private int ID;
	private byte[] continut;
	private byte[] imagine;
	private int nota;
	private int nr_votanti;
	public Carte(){}
	public Carte(String nume,String autor, String descriere,int id_gen,byte[] continut,byte[] imagine)
	{
		this.nume=nume;
		this.autor=autor;
		this.cale_pdf=cale_pdf;
		this.cale_img=cale_img;
		this.descriere=descriere;
		this.id_gen=id_gen;
		this.setContinut(continut);
		this.setImagine(imagine);
	}
	public String getNume() {
		return nume;
	}
	public void setNume(String nume) {
		this.nume = nume;
	}
	public String getAutor() {
		return autor;
	}
	public void setAutor(String autor) {
		this.autor = autor;
	}
	public String getCale() {
		return cale_pdf;
	}
	public void setCale(String cale) {
		this.cale_pdf = cale;
	}
	public int getId_utilizator() {
		return id_utilizator;
	}
	public void setId_utilizator(int id_utilizator) {
		this.id_utilizator = id_utilizator;
	}
	public String getDescriere() {
		return descriere;
	}
	public void setDescriere(String descriere) {
		this.descriere = descriere;
	}
	public String getCale_img() {
		return cale_img;
	}
	public void setCale_img(String cale_img) {
		this.cale_img = cale_img;
	}
	public int getID() {
		return ID;
	}
	public void setID(int iD) {
		ID = iD;
	}
	public String getGen() {
		return gen;
	}
	public void setGen(String gen) {
		this.gen = gen;
	}
	public int getId_gen() {
		return id_gen;
	}
	public void setId_gen(int id_gen) {
		this.id_gen = id_gen;
	}
	public byte[] getContinut() {
		return continut;
	}
	public void setContinut(byte[] continut) {
		this.continut = continut;
	}
	public byte[] getImagine() {
		return imagine;
	}
	public void setImagine(byte[] imagine) {
		this.imagine = imagine;
	}
	public int getNota() {
		return nota;
	}
	public void setNota(int nota) {
		this.nota = nota;
	}
	public int getNr_votanti() {
		return nr_votanti;
	}
	public void setNr_votanti(int nr_votanti) {
		this.nr_votanti = nr_votanti;
	}

}
