/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modules;

import java.sql.Timestamp;
import javafx.scene.image.Image;

/**
 *
 * @author dzhohar
 */
public class Obrazek {

    private int id;
    private String nazev;
    private String popisek;
    private Image image;
    private Timestamp cRazitko;
    private int idUzivatele;
    private int defaultPc;

    public int getDefaultPc() {
        return defaultPc;
    }

    public void setDefaultPc(int defaultPc) {
        this.defaultPc = defaultPc;
    }

    public Obrazek(int id, String nazev, String popisek, Image image, Timestamp cRazitko, int idUz, int defaultPc) {
        this.id = id;
        this.nazev = nazev;
        this.popisek = popisek;
        this.image = image;
        this.cRazitko = cRazitko;
        this.idUzivatele = idUz;
        this.defaultPc = defaultPc;

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNazev() {
        return nazev;
    }

    public void setNazev(String nazev) {
        this.nazev = nazev;
    }

    public String getPopisek() {
        return popisek;
    }

    public void setPopisek(String popisek) {
        this.popisek = popisek;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public Timestamp getcRazitko() {
        return cRazitko;
    }

    public void setcRazitko(Timestamp cRazitko) {
        this.cRazitko = cRazitko;
    }

    public int getIdUzivatele() {
        return idUzivatele;
    }

    public void setIdUzivatele(int idUzivatele) {
        this.idUzivatele = idUzivatele;
    }

    @Override
    public String toString() {
        return "Obrazek{" + "id=" + id + ", nazev=" + nazev + ", popisek=" + popisek + ", image=" + image + ", cRazitko=" + cRazitko + '}';
    }

}
