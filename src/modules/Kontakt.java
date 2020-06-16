/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modules;

import java.sql.Date;

/**
 *
 * @author dzhohar
 */
public class Kontakt {
    
    int id;
    Date platnost;
    String poznamka;
    int idUzivatele;

    public Kontakt(int id, Date platnost, String poznamka, int idUzivatele) {
        this.id = id;
        this.platnost = platnost;
        this.poznamka = poznamka;
        this.idUzivatele = idUzivatele;
    }

    public int getId() {
        return id;
    }

    public Date getPlatnost() {
        return platnost;
    }

    public String getPoznamka() {
        return poznamka;
    }

    public int getIdUzivatele() {
        return idUzivatele;
    }

    @Override
    public String toString() {
        return "Kontakt{" + "id=" + id + ", platnost=" + platnost + ", poznamka=" + poznamka + ", idUzivatele=" + idUzivatele + '}';
    }
   
    
    
}
