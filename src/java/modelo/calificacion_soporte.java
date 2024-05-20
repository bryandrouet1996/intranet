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
public class calificacion_soporte {
    private int id_calificacion;
    private int id_solicitud;
    private int id_usuario;
    private int id_satisfaccion;
    private String observacion;
    private Timestamp fecha_calificacion;

    public calificacion_soporte() {
    }

    public calificacion_soporte(int id_solicitud, int id_usuario, int id_satisfaccion, String observacion) {
        this.id_solicitud = id_solicitud;
        this.id_usuario = id_usuario;
        this.id_satisfaccion = id_satisfaccion;
        this.observacion = observacion;
    }

    public int getId_calificacion() {
        return id_calificacion;
    }

    public void setId_calificacion(int id_calificacion) {
        this.id_calificacion = id_calificacion;
    }

    public int getId_solicitud() {
        return id_solicitud;
    }

    public void setId_solicitud(int id_solicitud) {
        this.id_solicitud = id_solicitud;
    }

    public int getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(int id_usuario) {
        this.id_usuario = id_usuario;
    }

    public int getId_satisfaccion() {
        return id_satisfaccion;
    }

    public void setId_satisfaccion(int id_satisfaccion) {
        this.id_satisfaccion = id_satisfaccion;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public Timestamp getFecha_calificacion() {
        return fecha_calificacion;
    }

    public void setFecha_calificacion(Timestamp fecha_calificacion) {
        this.fecha_calificacion = fecha_calificacion;
    }

    @Override
    public String toString() {
        return "calificacion_soporte{" + "id_calificacion=" + id_calificacion + ", id_solicitud=" + id_solicitud + ", id_usuario=" + id_usuario + ", id_satisfaccion=" + id_satisfaccion + ", observacion=" + observacion + ", fecha_calificacion=" + fecha_calificacion + '}';
    }
    
}
