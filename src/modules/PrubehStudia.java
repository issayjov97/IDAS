/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modules;

import java.util.ArrayList;

/**
 *
 * @author dzhohar
 */
public class PrubehStudia {

    private int idZap;
    private int idPod;
     private int pred;
    private String predmet;
    private String stav;
    private String podminka;

    public PrubehStudia(int id, int idPod,int pred, String predmet, String stav, String podminka) {
        this.predmet = predmet;
        this.stav = stav;
        this.podminka = podminka;
        this.idZap = id;
        this.idPod = idPod;
        this.pred = pred;
    }

    public int getIdPod() {
        return idPod;
    }
      public int getPred() {
        return pred;
    }

    public void setIdPod(int idPod) {
        this.idPod = idPod;
    }

    public String getPredmet() {
        return predmet;
    }

    public void setPredmet(String predmet) {
        this.predmet = predmet;
    }

    public String getStav() {
        return stav;
    }

    public void setStav(String stav) {
        this.stav = stav;
    }

    public String getPodminka() {
        return podminka;
    }

    public void setPodminka(String podminka) {
        this.podminka = podminka;
    }

    public int getIdZap() {
        return idZap;
    }

    public void setIdZap(int idZap) {
        this.idZap = idZap;
    }
}
