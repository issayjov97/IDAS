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
public class ObjektOznameni {
    private int id;
    private int entityTypeId;
    private int entityId;
    private Timestamp time;
    private int status;

    public ObjektOznameni(int id, int entityTypeId, int entityId, Timestamp time, int status) {
        this.id = id;
        this.entityTypeId = entityTypeId;
        this.entityId = entityId;
        this.time = time;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getEntityTypeId() {
        return entityTypeId;
    }

    public void setEntityTypeId(int entityTypeId) {
        this.entityTypeId = entityTypeId;
    }

    public int getEntityId() {
        return entityId;
    }

    public void setEntityId(int entityId) {
        this.entityId = entityId;
    }

    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
    
}
