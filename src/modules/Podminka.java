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
public class Podminka {
    
  private SimpleIntegerProperty idPodminy;
  private SimpleStringProperty popis;

    public Podminka(int idPodminy,String popis) {
        this.idPodminy = new SimpleIntegerProperty(idPodminy);
        this.popis = new SimpleStringProperty(popis);
    }

    public SimpleIntegerProperty getIdPodminy() {
        return idPodminy;
    }

    public void setIdPodminy(SimpleIntegerProperty idPodminy) {
        this.idPodminy = idPodminy;
    }

    public SimpleStringProperty getPopis() {
        return popis;
    }

    public void setPopis(SimpleStringProperty popis) {
        this.popis = popis;
    }

    @Override
    public String toString() {
        return popis.get();
    }
  
  
    
}
