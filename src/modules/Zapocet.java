/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modules;

import java.sql.Timestamp;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;

/**
 *
 * @author dzhohar
 */
public class Zapocet {
    
    private SimpleIntegerProperty id;
    private ObjectProperty<Timestamp> date;
    private SimpleIntegerProperty stav; 
    private SimpleIntegerProperty idUzivatele; 

    public Zapocet(int id, Timestamp date, int stav, int idUz) {
        this.id = new SimpleIntegerProperty(id);
        this.date = new SimpleObjectProperty<>(date);
        this.stav = new SimpleIntegerProperty(stav);
        this.idUzivatele = new SimpleIntegerProperty (idUz);
    }

    public SimpleIntegerProperty getId() {
        return id;
    }

    public void setId(SimpleIntegerProperty id) {
        this.id = id;
    }

    public ObjectProperty<Timestamp> getDate() {
        return date;
    }

    public void setDate(ObjectProperty<Timestamp> date) {
        this.date = date;
    }

    public SimpleIntegerProperty getStav() {
        return stav;
    }

    public void setStav(SimpleIntegerProperty stav) {
        this.stav = stav;
    }

    public SimpleIntegerProperty getIdUzivatele() {
        return idUzivatele;
    }

    public void setIdUzivatele(SimpleIntegerProperty idUzivatele) {
        this.idUzivatele = idUzivatele;
    }

    @Override
    public String toString() {
        return  String.valueOf(stav.get()); //To change body of generated methods, choose Tools | Templates.
    }
    
    
    
    
    
    
    

    
}
