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
public class StudijniPlan {

    private SimpleIntegerProperty id;
    private SimpleStringProperty nazev;
    private SimpleIntegerProperty idOboru;
    private SimpleStringProperty popis;

    public StudijniPlan(int id, String nazev, int idOboru, String popis) {
        this.id = new SimpleIntegerProperty(id);
        this.nazev = new SimpleStringProperty(nazev);
        this.idOboru = new SimpleIntegerProperty(idOboru);
        this.popis = new SimpleStringProperty(popis);
    }

    @Override
    public String toString() {
        return  nazev.get();
    }

    public int getId() {
        return id.intValue();
    }

    public String getNazev() {
        return nazev.get();
    }

    public int getIdOboru() {
        return idOboru.get();
    }

    public String getPopis() {
        return popis.get();
    }

    public void setId(SimpleIntegerProperty id) {
        this.id = id;
    }

    public void setNazev(SimpleStringProperty nazev) {
        this.nazev = nazev;
    }

    public void setIdOboru(SimpleIntegerProperty idOboru) {
        this.idOboru = idOboru;
    }

    public void setPopis(SimpleStringProperty popis) {
        this.popis = popis;
    }
    

}
