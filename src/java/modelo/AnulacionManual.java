/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import java.sql.Timestamp;

/**
 *
 * @author Kevin Druet
 */
public class AnulacionManual {

    private int id;
    private int idPermiso;
    private int idAnula;
    private int idMotivo;
    private String motivo;
    private Timestamp creacion;

    public AnulacionManual() {
    }

    public AnulacionManual(int idPermiso, int idAnula, int idMotivo) {
        this.idPermiso = idPermiso;
        this.idAnula = idAnula;
        this.idMotivo = idMotivo;
    }

    public AnulacionManual(int id, int idPermiso, int idAnula, int idMotivo, String motivo, Timestamp creacion) {
        this.id = id;
        this.idPermiso = idPermiso;
        this.idAnula = idAnula;
        this.idMotivo = idMotivo;
        this.motivo = motivo;
        this.creacion = creacion;
    }
    
    

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdPermiso() {
        return idPermiso;
    }

    public void setIdPermiso(int idPermiso) {
        this.idPermiso = idPermiso;
    }

    public int getIdAnula() {
        return idAnula;
    }

    public void setIdAnula(int idAnula) {
        this.idAnula = idAnula;
    }

    public int getIdMotivo() {
        return idMotivo;
    }

    public void setIdMotivo(int idMotivo) {
        this.idMotivo = idMotivo;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public Timestamp getCreacion() {
        return creacion;
    }

    public void setCreacion(Timestamp creacion) {
        this.creacion = creacion;
    }

    

}
