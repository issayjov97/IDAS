/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modules;

import java.sql.Timestamp;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author dzhohar
 */
public class Uzivatel {

    private SimpleIntegerProperty id;
    private SimpleStringProperty login;
    private SimpleStringProperty heslo;
    private SimpleStringProperty jmeno;
    private SimpleStringProperty prijmeni;
    private SimpleStringProperty poznamka;
    private SimpleStringProperty email;
    private SimpleIntegerProperty blokace;

    public Uzivatel(Integer id, String login, String heslo, String jmeno, String prijmeni, String poznamka, String email, Integer blokace) {
        this.id = new SimpleIntegerProperty(id);
        this.jmeno = new SimpleStringProperty(jmeno);
        this.login = new SimpleStringProperty(login);
        this.heslo = new SimpleStringProperty(heslo);
        this.prijmeni = new SimpleStringProperty(prijmeni);
        this.poznamka = new SimpleStringProperty(poznamka);
        this.email = new SimpleStringProperty(email);
        this.blokace = new SimpleIntegerProperty(blokace);

    }

    public String getPoznamka() {
        return poznamka.get();
    }

    public Integer getId() {
        return id.get();
    }

    public String getJmeno() {
        return jmeno.get();
    }

    public String getPrijmeni() {
        return prijmeni.get();
    }

    public String getEmail() {
        return email.get();
    }

    public String getLogin() {
        return login.get();
    }

    public String getHeslo() {
        return heslo.get();
    }

    public int getBlokace() {
        return blokace.get();
    }

    public void setId(SimpleIntegerProperty id) {
        this.id = id;
    }

    public void setLogin(SimpleStringProperty login) {
        this.login = login;
    }

    public void setHeslo(SimpleStringProperty heslo) {
        this.heslo = heslo;
    }

    public void setJmeno(SimpleStringProperty jmeno) {
        this.jmeno = jmeno;
    }

    public void setPrijmeni(SimpleStringProperty prijmeni) {
        this.prijmeni = prijmeni;
    }

    public void setPoznamka(SimpleStringProperty poznamka) {
        this.poznamka = poznamka;
    }

    public void setEmail(SimpleStringProperty email) {
        this.email = email;
    }

    public void setBlokace(SimpleIntegerProperty blokace) {
        this.blokace = blokace;
    }

    @Override
    public String toString() {
        String s = "";
        s += jmeno.get() + ", " + prijmeni.get() + ", " + email.get();
        if (poznamka.get() != null) {
            s += ", " + poznamka.get();
        }
        return s;
    }

}
