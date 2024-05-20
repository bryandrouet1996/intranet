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
public class AnulacionSolicitud {

    private int id_anulacion;
    private int id_solicitud;
    private int id_anula;
    private int id_motivo;
    private String motivo;
    private Timestamp fecha_creacion;

    public AnulacionSolicitud() {
    }

    public AnulacionSolicitud(int id_solicitud, int id_anula, int id_motivo) {
        this.id_solicitud = id_solicitud;
        this.id_anula = id_anula;
        this.id_motivo = id_motivo;
    }

    public int getId_anulacion() {
        return id_anulacion;
    }

    public void setId_anulacion(int id_anulacion) {
        this.id_anulacion = id_anulacion;
    }

    public int getId_solicitud() {
        return id_solicitud;
    }

    public void setId_solicitud(int id_solicitud) {
        this.id_solicitud = id_solicitud;
    }

    public int getId_anula() {
        return id_anula;
    }

    public void setId_anula(int id_anula) {
        this.id_anula = id_anula;
    }    

    public Timestamp getFecha_creacion() {
        return fecha_creacion;
    }

    public void setFecha_creacion(Timestamp fecha_creacion) {
        this.fecha_creacion = fecha_creacion;
    }

    public int getId_motivo() {
        return id_motivo;
    }

    public void setId_motivo(int id_motivo) {
        this.id_motivo = id_motivo;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

}
