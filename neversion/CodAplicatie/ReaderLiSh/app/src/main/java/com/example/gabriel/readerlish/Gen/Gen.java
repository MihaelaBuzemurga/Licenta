package com.example.gabriel.readerlish.Gen;

import java.io.Serializable;

/**
 * Created by Gabriel on 31.01.2017.
 */

public class Gen implements Serializable {
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
