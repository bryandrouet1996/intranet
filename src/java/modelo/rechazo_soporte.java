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
public class rechazo_soporte {
    private int id_rechazo;
    private int id_solicitud;
    private int id_tecnico;
    private String motivo;
    private Timestamp fecha_rechazo;

    public rechazo_soporte() {
    }
    
    public rechazo_soporte(int id_solicitud, int id_tecnico, String motivo) {
        this.id_solicitud = id_solicitud;
        this.id_tecnico = id_tecnico;
        this.motivo = motivo;
    }

    public rechazo_soporte(int id_rechazo, int id_solicitud, int id_tecnico, String motivo, Timestamp fecha_rechazo) {
        this.id_rechazo = id_rechazo;
        this.id_solicitud = id_solicitud;
        this.id_tecnico = id_tecnico;
        this.motivo = motivo;
        this.fecha_rechazo = fecha_rechazo;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public int getId_rechazo() {
        return id_rechazo;
    }

    public void setId_rechazo(int id_rechazo) {
        this.id_rechazo = id_rechazo;
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

    public Timestamp getFecha_rechazo() {
        return fecha_rechazo;
    }

    public void setFecha_rechazo(Timestamp fecha_rechazo) {
        this.fecha_rechazo = fecha_rechazo;
    }

    
}
