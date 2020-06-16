/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modules;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author dzhohar
 */
public class Predmet {

    private SimpleIntegerProperty idPredmetu;
    private SimpleStringProperty zkratka;
    private SimpleStringProperty nazev;

    public Predmet(int idPredmetu, String zkratka, String nazev) {
        this.idPredmetu = new SimpleIntegerProperty(idPredmetu);
        this.zkratka = new SimpleStringProperty(zkratka);
        this.nazev = new SimpleStringProperty(nazev);
    }

    public int getIdPredmetu() {
        return idPredmetu.get();
    }

    public String getZkratka() {
        return zkratka.get();
    }

    public String getNazev() {
        return nazev.get();
    }

    public void setIdPredmetu(SimpleIntegerProperty idPredmetu) {
        this.idPredmetu = idPredmetu;
    }

    public void setZkratka(SimpleStringProperty zkratka) {
        this.zkratka = zkratka;
    }

    public void setNazev(SimpleStringProperty nazev) {
        this.nazev = nazev;
    }

    @Override
    public String toString() {
        return  nazev.get();
    }
    

}
