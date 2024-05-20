/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import java.sql.Timestamp;

/**
 *
 * @author Don Beta
 */
public class AnulacionSoporte {
    private int idAnulacion;
    private int idSol;
    private int idTec;
    private String motivo;
    private Timestamp fecha;

    public AnulacionSoporte() {
    }

    public AnulacionSoporte(int idSol, int idTec, String motivo) {
        this.idSol = idSol;
        this.idTec = idTec;
        this.motivo = motivo;
    }

    public AnulacionSoporte(int idAnulacion, int idSol, int idTec, String motivo, Timestamp fecha) {
        this.idAnulacion = idAnulacion;
        this.idSol = idSol;
        this.idTec = idTec;
        this.motivo = motivo;
        this.fecha = fecha;
    }   
    
    public int getIdAnulacion() {
        return idAnulacion;
    }

    public void setIdAnulacion(int idAnulacion) {
        this.idAnulacion = idAnulacion;
    }

    public int getIdSol() {
        return idSol;
    }

    public void setIdSol(int idSol) {
        this.idSol = idSol;
    }

    public int getIdTec() {
        return idTec;
    }

    public void setIdTec(int idTec) {
        this.idTec = idTec;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public Timestamp getFecha() {
        return fecha;
    }

    public void setFecha(Timestamp fecha) {
        this.fecha = fecha;
    }
    
    
}
