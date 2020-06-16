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
public class StudijniObor {

    private SimpleIntegerProperty id;
    private SimpleStringProperty nazev;
    private SimpleStringProperty popis;

    public StudijniObor(int id, String nazev, String popis) {
        this.id = new SimpleIntegerProperty(id);
        this.nazev = new SimpleStringProperty(nazev);
        this.popis = new SimpleStringProperty(popis);
    }

    @Override
    public String toString() {
        return this.nazev.get();
    }

    public int getId() {
        return id.get();
    }

    public String getNazev() {
        return nazev.get();
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

    public void setPopis(SimpleStringProperty popis) {
        this.popis = popis;
    }

}
