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
public class asignacion_soporte {
    private int id_asignacion;
    private int id_solicitud;
    private int id_administrador;
    private int id_tecnico;
    private Timestamp fecha_asignacion;

    public asignacion_soporte() {
    }

    public asignacion_soporte(int id_solicitud, int id_administrador, int id_tecnico) {
        this.id_solicitud = id_solicitud;
        this.id_administrador = id_administrador;
        this.id_tecnico = id_tecnico;
    }

    public int getId_asignacion() {
        return id_asignacion;
    }

    public void setId_asignacion(int id_asignacion) {
        this.id_asignacion = id_asignacion;
    }

    public int getId_solicitud() {
        return id_solicitud;
    }

    public void setId_solicitud(int id_solicitud) {
        this.id_solicitud = id_solicitud;
    }

    public int getId_administrador() {
        return id_administrador;
    }

    public void setId_administrador(int id_administrador) {
        this.id_administrador = id_administrador;
    }

    public int getId_tecnico() {
        return id_tecnico;
    }

    public void setId_tecnico(int id_tecnico) {
        this.id_tecnico = id_tecnico;
    }

    public Timestamp getFecha_asignacion() {
        return fecha_asignacion;
    }

    public void setFecha_asignacion(Timestamp fecha_asignacion) {
        this.fecha_asignacion = fecha_asignacion;
    }

    @Override
    public String toString() {
        return "asignacion_soporte{" + "id_asignacion=" + id_asignacion + ", id_solicitud=" + id_solicitud + ", id_administrador=" + id_administrador + ", id_tecnico=" + id_tecnico + ", fecha_asignacion=" + fecha_asignacion + '}';
    }
    
    
}
