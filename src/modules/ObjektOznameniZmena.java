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
public class ObjektOznameniZmena {

    private int idObjectNatification;
    private int objektNatifStatus;
    private int objectNatifEntityId;
    private int notifierId;
    private int actorId;
    private String notificationType;
    private String actorName;
    private String actorSurname;

    //Select ob_oznameni.id,ob_oznameni.status,ob_oznameni.entity_id, ozn.notifier_id, ozn_zmena.actor_id ,ent_z.NAZEV,uziv.JMENO,uziv.PRIJMENI
    public ObjektOznameniZmena(int idObjectNatification, int objektNatifStatus, int objectNatifEntityId, int notifierId, int actorId, String notificationType, String actorName, String actorSurname) {
        this.idObjectNatification = idObjectNatification;
        this.objektNatifStatus = objektNatifStatus;
        this.objectNatifEntityId = objectNatifEntityId;
        this.notifierId = notifierId;
        this.actorId = actorId;
        this.notificationType = notificationType;
        this.actorName = actorName;
        this.actorSurname = actorSurname;
    }

    public int getIdObjectNatification() {
        return idObjectNatification;
    }

    public void setIdObjectNatification(int idObjectNatification) {
        this.idObjectNatification = idObjectNatification;
    }

    public int getObjektNatifStatus() {
        return objektNatifStatus;
    }

    public void setObjektNatifStatus(int objektNatifStatus) {
        this.objektNatifStatus = objektNatifStatus;
    }

    public int getObjectNatifEntityId() {
        return objectNatifEntityId;
    }

    public void setObjectNatifEntityId(int objectNatifEntityId) {
        this.objectNatifEntityId = objectNatifEntityId;
    }

    public int getNotifierId() {
        return notifierId;
    }

    public void setNotifierId(int notifierId) {
        this.notifierId = notifierId;
    }

    public int getActorId() {
        return actorId;
    }

    public void setActorId(int actorId) {
        this.actorId = actorId;
    }

    public String getNotificationType() {
        return notificationType;
    }

    public void setNotificationType(String notificationType) {
        this.notificationType = notificationType;
    }

    public String getActorName() {
        return actorName;
    }

    public void setActorName(String actorName) {
        this.actorName = actorName;
    }

    public String getActorSurname() {
        return actorSurname;
    }

    public void setActorSurname(String actorSurname) {
        this.actorSurname = actorSurname;
    }

}
