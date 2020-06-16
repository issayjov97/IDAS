/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modules;

import java.sql.Date;
import java.sql.Timestamp;

/**
 *
 * @author dzhohar
 */
public class Udalost {

    private int idUdal;
    private Timestamp datum;
    private String obsah;
    private String nazev;
    private int idUziv;
    private String mistoKonani;

    public Udalost(int idUdal, Timestamp datum, String obsah, String nazev, int idUziv, String mistoKonani) {
        this.idUdal = idUdal;
        this.datum = datum;
        this.obsah = obsah;
        this.nazev = nazev;
        this.idUziv = idUziv;
        this.mistoKonani = mistoKonani;
    }

    public int getIdUdal() {
        return idUdal;
    }

    public Timestamp getDatum() {
        return datum;
    }

    public String getObsah() {
        return obsah;
    }

    public String getNazev() {
        return nazev;
    }

    public int getIdUziv() {
        return idUziv;
    }

    public String getMistoKonani() {
        return mistoKonani;
    }

   

    @Override
    public String toString() {
        return datum + ", " + obsah + ", " + nazev + ", " + mistoKonani;
    }
}
