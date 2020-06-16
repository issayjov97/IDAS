/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modules;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author dzhohar
 */
public class Skupina extends  RecursiveTreeObject<Skupina>{

    private IntegerProperty id;
    private StringProperty nazev;
    private IntegerProperty idKom;

    public Skupina(int id, String nazev, int komentar) {
        this.id = new SimpleIntegerProperty(id);
        this.nazev = new SimpleStringProperty(nazev);
        this.idKom = new SimpleIntegerProperty(komentar);
    }

    public Skupina() {
    }

    public int getId() {
        return id.get();
    }

    public String getNazev() {
        return nazev.get();
    }

    public int getKomentar() {
        return idKom.get();
    }

 

    @Override
    public String toString() {
        return nazev.get() ;
    }

 

}
