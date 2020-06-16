/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modules;

import java.sql.Timestamp;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author dzhohar
 */
public class Komentar {

    private IntegerProperty idKom;
    private StringProperty nazev;
    private StringProperty obsah;
    private ObjectProperty<Timestamp> date;
    private IntegerProperty idPodKom;
    private IntegerProperty blokace;
    private IntegerProperty idUzivatele;
    private int idObrazku;

    public Komentar(int idKom, String nazev, String obsah, Timestamp date, int idPodKom, int blokace, int idUziv, int idObrazku) {
        this.idKom = new SimpleIntegerProperty(idKom);
        this.nazev = new SimpleStringProperty(nazev);
        this.obsah = new SimpleStringProperty(obsah);
        this.date = new SimpleObjectProperty<>(date);
        this.idPodKom = new SimpleIntegerProperty(idPodKom);
        this.blokace = new SimpleIntegerProperty(blokace);
        this.idUzivatele = new SimpleIntegerProperty(idUziv);
        this.idObrazku = idObrazku;
    }

    public IntegerProperty getIdKom() {
        return idKom;
    }

    public void setIdKom(int idKom) {
        this.idKom.set(idKom);
    }

    public StringProperty getNazev() {
        return nazev;
    }

    public void setNazev(String nazev) {
        this.nazev.set(nazev);
    }

    public StringProperty getObsah() {
        return obsah;
    }

    public void setObsah(String obsah) {
        this.obsah.set(obsah);
    }

    public ObjectProperty<Timestamp> getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date.set(date);
    }

    public IntegerProperty getIdPodKom() {
        return idPodKom;
    }

    public void setIdPodKom(int idPodKom) {
        this.idPodKom.set(idPodKom);
    }

    public IntegerProperty getBlokace() {
        return blokace;
    }

    public void setBlokace(int blokace) {
        this.blokace.set(blokace);
    }

    public int getIdUzivatele() {
        return idUzivatele.get();
    }

    public int getIdObrazku() {
        return idObrazku;
    }

    public void setIdObrazku(int idObrazku) {
        this.idObrazku = idObrazku;
    }

    @Override
    public String toString() {
        return "Komentar" + "idKom=" + idKom + ", obsah=" + obsah + ", idPodKom=" + idPodKom;
    }

}
