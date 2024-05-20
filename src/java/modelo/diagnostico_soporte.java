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
public class diagnostico_soporte {
    private int id_diagnostico;
    private int id_solicitud;
    private int id_tecnico;
    private String observacion;
    private int tipoSol;
    private String adjunto;
    private Timestamp fecha_diagnostico;

    public diagnostico_soporte() {
    }

    public diagnostico_soporte(int id_solicitud, int id_tecnico, String observacion, String adjunto, int tipoSol) {
        this.id_solicitud = id_solicitud;
        this.id_tecnico = id_tecnico;
        this.observacion = observacion;
        this.adjunto = adjunto;
        this.tipoSol = tipoSol;
    }

    public int getId_diagnostico() {
        return id_diagnostico;
    }

    public void setId_diagnostico(int id_diagnostico) {
        this.id_diagnostico = id_diagnostico;
    }

    public int getId_solicitud() {
        return id_solicitud;
    }

    public void setId_solicitud(int id_solicitud) {
        this.id_solicitud = id_solicitud;
    }

    public int getId_tecnico() {
        return id_tecnico;
    }

    public void setId_tecnico(int id_tecnico) {
        this.id_tecnico = id_tecnico;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public Timestamp getFecha_diagnostico() {
        return fecha_diagnostico;
    }

    public void setFecha_diagnostico(Timestamp fecha_diagnostico) {
        this.fecha_diagnostico = fecha_diagnostico;
    }
    
    

    @Override
    public String toString() {
        return "diagnostico_soporte{" + "id_diagnostico=" + id_diagnostico + ", id_solicitud=" + id_solicitud + ", id_tecnico=" + id_tecnico + ", observacion=" + observacion + ", fecha_diagnostico=" + fecha_diagnostico + '}';
    }

    public String getAdjunto() {
        return adjunto;
    }

    public void setAdjunto(String adjunto) {
        this.adjunto = adjunto;
    }

    public int getTipoSol() {
        return tipoSol;
    }

    public void setTipoSol(int tipoSol) {
        this.tipoSol = tipoSol;
    }
    
}
