/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modules;

import java.util.ArrayList;
import oracle.sql.TIMESTAMP;

/**
 *
 * @author dzhohar
 */
public class Oznameni {
    private int id;
    private int objektOznameniId;
    private  int notifierId;
    private int status;

    public Oznameni(int id, int objektOznameniId, int notifierId, int status) {
        this.id = id;
        this.objektOznameniId = objektOznameniId;
        this.notifierId = notifierId;
        this.status = status;
    }
    
    

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getObjektOznameniId() {
        return objektOznameniId;
    }

    public void setObjektOznameniId(int objektOznameniId) {
        this.objektOznameniId = objektOznameniId;
    }

    public int getNotifierId() {
        return notifierId;
    }

    public void setNotifierId(int notifierId) {
        this.notifierId = notifierId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

}
