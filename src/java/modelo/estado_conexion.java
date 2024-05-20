/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

/**
 *
 * @author Luis Torres F
 */
public class estado_conexion {
    private int id_estado;
    private String permanente;
    private String antivirus;
    private String cabildo;
    private String sigame;
    private String office365;


    public estado_conexion(int id_estado, String permanente, String antivirus, String cabildo, String sigame, String office365, String dhcp) {
        this.id_estado = id_estado;
        this.permanente = permanente;
        this.antivirus = antivirus;
        this.cabildo = cabildo;
        this.sigame = sigame;
        this.office365 = office365;
        this.dhcp = dhcp;
    }

  

 


    public String getOffice365() {
        return office365;
    }

    public void setOffice365(String office365) {
        this.office365 = office365;
    }

    public estado_conexion(int id_estado, String permanente, String antivirus, String cabildo, String sigame, String dhcp) {
        this.id_estado = id_estado;
        this.permanente = permanente;
        this.antivirus = antivirus;
        this.cabildo = cabildo;
        this.sigame = sigame;
        this.dhcp = dhcp;
    }

    public String getSigame() {
        return sigame;
    }

    public void setSigame(String sigame) {
        this.sigame = sigame;
    }

    public estado_conexion(int id_estado, String permanente, String antivirus, String cabildo, String dhcp) {
        this.id_estado = id_estado;
        this.permanente = permanente;
        this.antivirus = antivirus;
        this.cabildo = cabildo;
        this.dhcp = dhcp;
    }

    public String getCabildo() {
        return cabildo;
    }

    public void setCabildo(String cabildo) {
        this.cabildo = cabildo;
    }

    public estado_conexion(int id_estado, String permanente, String antivirus, String dhcp) {
        this.id_estado = id_estado;
        this.permanente = permanente;
        this.antivirus = antivirus;
        this.dhcp = dhcp;
    }

    public String getAntivirus() {
        return antivirus;
    }

    public void setAntivirus(String antivirus) {
        this.antivirus = antivirus;
    }

    public estado_conexion(int id_estado, String permanente, String dhcp) {
        this.id_estado = id_estado;
        this.permanente = permanente;
        this.dhcp = dhcp;
    }

    public String getPermanente() {
        return permanente;
    }

    public void setPermanente(String permanente) {
        this.permanente = permanente;
    }

    public estado_conexion(int id_estado, String dhcp) {
        this.id_estado = id_estado;
        this.dhcp = dhcp;
    }

    public int getId_estado() {
        return id_estado;
    }

    public void setId_estado(int id_estado) {
        this.id_estado = id_estado;
    }

    public String getDhcp() {
        return dhcp;
    }

    public void setDhcp(String dhcp) {
        this.dhcp = dhcp;
    }
    private String dhcp;
    
    estado_conexion() {
    }
  
}
