/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modules;

/**
 *
 * @author dzhohar
 */
public class OznameniZmena {
    private int id;
    private int objektOznameniId;
    private  int actorId;
    private int status;

    public OznameniZmena(int id, int objektOznameniId, int actorId, int status) {
        this.id = id;
        this.objektOznameniId = objektOznameniId;
        this.actorId = actorId;
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

    public int getActorId() {
        return actorId;
    }

    public void setActorId(int actorId) {
        this.actorId = actorId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
    
}
