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
public class AprobacionHoras {
    private int id_aprobacion;
    private int id_solicitud;
    private int id_aprueba;
    private Timestamp fecha_creacion;
    private Timestamp fecha_update;

    public AprobacionHoras() {
    }

    public AprobacionHoras(int id_solicitud, int id_aprueba) {
        this.id_solicitud = id_solicitud;
        this.id_aprueba = id_aprueba;
    }

    public int getId_aprobacion() {
        return id_aprobacion;
    }

    public void setId_aprobacion(int id_aprobacion) {
        this.id_aprobacion = id_aprobacion;
    }

    public int getId_solicitud() {
        return id_solicitud;
    }

    public void setId_solicitud(int id_solicitud) {
        this.id_solicitud = id_solicitud;
    }

    public int getId_aprueba() {
        return id_aprueba;
    }

    public void setId_aprueba(int id_aprueba) {
        this.id_aprueba = id_aprueba;
    }

    public Timestamp getFecha_creacion() {
        return fecha_creacion;
    }

    public void setFecha_creacion(Timestamp fecha_creacion) {
        this.fecha_creacion = fecha_creacion;
    }

    public Timestamp getFecha_update() {
        return fecha_update;
    }

    public void setFecha_update(Timestamp fecha_update) {
        this.fecha_update = fecha_update;
    }

    @Override
    public String toString() {
        return "aprobacion_solicitud{" + "id_aprobacion=" + id_aprobacion + ", id_solicitud=" + id_solicitud + ", id_aprueba=" + id_aprueba + ", fecha_creacion=" + fecha_creacion + ", fecha_update=" + fecha_update + '}';
    }
 
}
