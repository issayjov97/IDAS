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
public class Like {
    private int id;
    private Timestamp cRazitko;
    private int obr_id;
    private int post_id;
    private int id_uziv;

    public Like(int id, Timestamp cRazitko, int obr_id, int post_id, int id_uziv) {
        this.id = id;
        this.cRazitko = cRazitko;
        this.obr_id = obr_id;
        this.post_id = post_id;
        this.id_uziv = id_uziv;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Timestamp getcRazitko() {
        return cRazitko;
    }

    public void setcRazitko(Timestamp cRazitko) {
        this.cRazitko = cRazitko;
    }

    public int getObr_id() {
        return obr_id;
    }

    public void setObr_id(int obr_id) {
        this.obr_id = obr_id;
    }

    public int getPost_id() {
        return post_id;
    }

    public void setPost_id(int post_id) {
        this.post_id = post_id;
    }

    public int getId_uziv() {
        return id_uziv;
    }

    public void setId_uziv(int id_uziv) {
        this.id_uziv = id_uziv;
    }
    
    
    
}
