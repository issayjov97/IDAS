/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modules;

import java.sql.Timestamp;

/**
 *
 * @author dzhohar
 */
public class Zprava {
    private int id;
    private Timestamp time;
    private String obsah;
    private String poznamka;
    private String predmet;

    public Zprava(int id, Timestamp time, String obsah, String poznamka, String predmet) {
        this.id = id;
        this.time = time;
        this.obsah = obsah;
        this.poznamka = poznamka;
        this.predmet = predmet;
    }

    public int getId() {
        return id;
    }

    public Timestamp getTime() {
        return time;
    }

    public String getObsah() {
        return obsah;
    }

    public String getPoznamka() {
        return poznamka;
    }

    public String getPredmet() {
        return predmet;
    }

    @Override
    public String toString() {
        String str =time + ", " + obsah + ", " + predmet;
        if (poznamka!=null) {
            str+=", "+poznamka;
        }
        return str;
    }
    
}
