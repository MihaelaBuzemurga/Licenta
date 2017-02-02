package com.example.gabriel.readerlish;

import android.graphics.Bitmap;

/**
 * Created by Gabriel on 19.01.2017.
 */

public class CartiRepository {
    private String m_nume;
    private String m_gen;
    private String nota;
    private String m_autor;
    private Bitmap imagine;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    private int id;
    public CartiRepository()
    {

    }
    public CartiRepository(int id,String m_nume, String m_gen, String nota, String m_autor, Bitmap imagine) {
        this.id=id;
        this.m_nume = m_nume;
        this.m_gen = m_gen;
        this.nota = nota;
        this.m_autor = m_autor;
        this.imagine = imagine;
    }

    public String getM_nume() {
        return m_nume;
    }

    public void setM_nume(String m_nume) {
        this.m_nume = m_nume;
    }

    public String getM_gen() {
        return m_gen;
    }

    public void setM_gen(String m_gen) {
        this.m_gen = m_gen;
    }

    public String getNota() {
        return nota;
    }

    public void setNota(String nota) {
        this.nota = nota;
    }

    public String getM_autor() {
        return m_autor;
    }

    public void setM_autor(String m_autor) {
        this.m_autor = m_autor;
    }

    public Bitmap getImagine() {
        return imagine;
    }

    public void setImagine(Bitmap imagine) {
        this.imagine = imagine;
    }
}
