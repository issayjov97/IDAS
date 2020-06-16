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
public class Role {

    private SimpleIntegerProperty idRole;
    private SimpleStringProperty nazev;
    private SimpleStringProperty poznamka;
    private SimpleStringProperty opravneni;

    public Role(int idRole, String nazev, String poznamka, String opravneni) {
        this.idRole = new SimpleIntegerProperty(idRole);
        this.nazev = new SimpleStringProperty(nazev);
        this.poznamka = new SimpleStringProperty(poznamka);
        this.opravneni = new SimpleStringProperty(opravneni);
    }

    public int getIdRole() {
        return idRole.get();
    }

    public String getNazev() {
        return nazev.get();
    }

    public String getPoznamka() {
        return poznamka.get();
    }

    public String getOpravneni() {
        return opravneni.get();
    }

    @Override
    public String toString() {
        return nazev.get();
    }

    public void setIdRole(SimpleIntegerProperty idRole) {
        this.idRole = idRole;
    }

    public void setNazev(SimpleStringProperty nazev) {
        this.nazev = nazev;
    }

    public void setPoznamka(SimpleStringProperty poznamka) {
        this.poznamka = poznamka;
    }

    public void setOpravneni(SimpleStringProperty opravneni) {
        this.opravneni = opravneni;
    }
    

}
