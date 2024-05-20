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
public class rechazo_solicitud {

    private int id_rechazo;
    private int id_solicitud;
    private int id_rechaza;
    private String razon;
    private Timestamp fecha_creacion;

    public rechazo_solicitud() {
    }

    public rechazo_solicitud(int id_solicitud, int id_rechaza, String razon) {
        this.id_solicitud = id_solicitud;
        this.id_rechaza = id_rechaza;
        this.razon = razon;
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

    public int getId_rechaza() {
        return id_rechaza;
    }

    public void setId_rechaza(int id_rechaza) {
        this.id_rechaza = id_rechaza;
    }

    public String getRazon() {
        return razon;
    }

    public void setRazon(String razon) {
        this.razon = razon;
    }

    public Timestamp getFecha_creacion() {
        return fecha_creacion;
    }

    public void setFecha_creacion(Timestamp fecha_creacion) {
        this.fecha_creacion = fecha_creacion;
    }

    @Override
    public String toString() {
        return "rechazo_solicitud{" + "id_rechazo=" + id_rechazo + ", id_solicitud=" + id_solicitud + ", id_rechaza=" + id_rechaza + ", razon=" + razon + ", fecha_creacion=" + fecha_creacion + '}';
    }

}
